package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.db.helper.DaoHelper;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.net.download.HttpDownManager;
import com.android.zhhr.net.download.ProgressDownSubscriber;
import com.android.zhhr.ui.view.IDownloadlistView;
import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.LogUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by DELL on 2018/2/12.
 */

public class DownloadChapterlistPresenter extends BasePresenter<IDownloadlistView>{
    private Comic mComic;
    private HashMap<Integer,Integer> mMap;
    private ComicModule mModel;
    private ArrayList<DBDownloadItems> mLists;
    private HttpDownManager manager;
    private DaoHelper helper;
    private HashMap<String, DownloadComicDisposableObserver> subMap;

    public DownloadChapterlistPresenter(Activity context, IDownloadlistView view, Intent intent) {
        super(context, view);
        mComic= (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
        manager = HttpDownManager.getInstance(mContext.getApplicationContext());
        this.mModel = new ComicModule(mContext);
        helper = new DaoHelper(context);
        mLists = new ArrayList<>();
        subMap = new HashMap<>();
    }

    /**
     * 初始化按照章節下載
     */
    public void initData() {
        //从数据库拉取数据
        mModel.getDownloadItemsFromDB(mComic.getId(), new Observer<List<DBDownloadItems>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                DBDownloadItems item;
                //把hashmap進行排序操作
                List<Map.Entry<Integer,Integer>> list = new ArrayList<>(mMap.entrySet());
                Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
                    public int compare(Map.Entry<Integer, Integer> o1,
                                       Map.Entry<Integer, Integer> o2) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                });
                //遍历map
                for(Map.Entry<Integer,Integer> mapping:list){
                    if(mapping.getValue() == Constants.CHAPTER_SELECTED){
                        item = new DBDownloadItems();
                        item.setId(mComic.getId()+mapping.getKey());
                        item.setChapters_title(mComic.getChapters().get(mapping.getKey()));
                        item.setComic_id(mComic.getId());
                        item.setChapters(mapping.getKey());
                        item.setState(DownState.NONE);
                        try{
                            //把数据先存入数据库
                            helper.insert(item);
                        }catch (SQLiteConstraintException exception){
                            LogUtil.e("请不要插入重复值");
                        }
                    }
                }
            }

            @Override
            public void onNext(@NonNull List<DBDownloadItems> items) {
                if(items!=null&&items.size()!=0){
                    mLists.addAll(items);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtil.e(mComic.getTitle()+"从数据库中拉取本地下载列表数据失败"+e.toString());
            }

            @Override
            public void onComplete() {
                //拉取之后把新添加的数据整理一下
                if(mLists!=null&&mLists.size()!=0){
                    mView.fillData(mLists);
                }
            }
        });
    }


    public void update(DownInfo info) {
        manager.upDateToDb(info);
    }

    /**
     * 开始所有下载
     */
    public void startAll() {

    }

    /**
     * 暂停某个下载
     * @param info
     */
    public void pause(DBDownloadItems info) {
        if (info == null) return;
        info.setState(DownState.PAUSE);
        LogUtil.v("暂停下载");
        String url = info.getChapters_url().get(info.getCurrent_num());
        if (subMap.containsKey(url)){
            DownloadComicDisposableObserver subscriber = subMap.get(url);
            subscriber.dispose();
            subMap.remove(url);
        }
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
        helper.update(info);
    }

    /**
     * 开始某个下载
     * @param info
     */
    public void startDown(final DBDownloadItems info) {
        //首先判断是否已经获取过下载地址
        if(info.getNum()==0){
            //获取下载地址
            mModel.getDownloadChaptersList(mComic.getId()+"",info.getChapters(),new Observer<ArrayList<String>>(){

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    //修改状态
                    info.setState(DownState.START);
                    mView.getDataFinish();
                }

                @Override
                public void onNext(@NonNull ArrayList<String> mLists) {
                    //修改状态
                    info.setState(DownState.DOWN);
                    //设置下载地址
                    info.setChapters_url(mLists);
                    info.setNum(mLists.size()-1);
                    info.setCurrent_num(0);
                    //把获取到的下载地址存进数据库
                    helper.update(info);
                    mView.getDataFinish();
                    if(mLists!=null&&mLists.size()!=0){
                        DownloadChapter(info,info.getCurrent_num());
                    }
                }


                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }else{
            //修改状态
            info.setState(DownState.DOWN);
            helper.update(info);
            mView.getDataFinish();
            DownloadChapter(info,info.getCurrent_num());
        }
    }

    /**
     * 递归下载每一话的所有图片
     * @param info
     * @param page
     */
    private void DownloadChapter(final DBDownloadItems info, final int page) {
        DownloadComicDisposableObserver observer = new DownloadComicDisposableObserver(info,page);
        mModel.download(info.getChapters_url().get(page), FileUtil.SDPATH + FileUtil.COMIC + mComic.getId() + "/" + info.getChapters()+"/", page+".png", observer);
        subMap.put(info.getChapters_url().get(page),observer);
    }

    public class DownloadComicDisposableObserver extends DisposableObserver<InputStream> {
        int page;
        DBDownloadItems info;
        public DownloadComicDisposableObserver(DBDownloadItems info,int page){
            this.page = page;
            this.info = info;
        }

        @Override
        public void onNext(@NonNull InputStream inputStream) {

        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {
            LogUtil.d(page+"/"+info.getNum()+"下载完成");
            //从队列中移除
            subMap.remove(info.getChapters_url().get(page));
            //写一个递归继续去下载
            if(page<info.getNum()){
                DownloadChapter(info,page+1);
            }else {
                info.setState(DownState.FINISH);
            }
            info.setCurrent_num(page);
            helper.update(info);
            mView.getDataFinish();

        }
    }

    /**
     * 暂停所有下载
     */
    public void pauseAll() {
        for(int i=0;i<mLists.size();i++){
            DBDownloadItems items = mLists.get(i);
            if (items.getState() == DownState.DOWN){
                items.setState(DownState.PAUSE);
                helper.update(items);
            }
        }
    }

}
