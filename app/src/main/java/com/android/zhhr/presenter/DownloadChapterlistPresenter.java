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
import com.android.zhhr.ui.view.IDownloadlistView;
import com.android.zhhr.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

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

    public DownloadChapterlistPresenter(Activity context, IDownloadlistView view, Intent intent) {
        super(context, view);
        mComic= (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
        manager = HttpDownManager.getInstance(mContext.getApplicationContext());
        this.mModel = new ComicModule(mContext);
        helper = new DaoHelper(context);
        mLists = new ArrayList<>();
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
                        item.setState(DownState.START);
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

    /**
     * 获取每一张图片的下载地址
     */
    public void initDownInfoData(){
        //把hashmap進行排序操作
        List<Map.Entry<Integer,Integer>> list = new ArrayList<>(mMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        for(Map.Entry<Integer,Integer> mapping:list){
            //System.out.println(mapping.getKey()+":"+mapping.getValue());
            if(mapping.getValue() != Constants.CHAPTER_FREE){
                mModel.getDownloadChaptersList(mComic.getId()+"",mapping.getKey(),new Observer<ArrayList<DownInfo>>(){

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<DownInfo> mLists) {
                        if(mLists!=null&&mLists.size()!=0){
                            mView.onLoadMoreData(mLists);
                        }
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }
    }

    /**
     * 从数据库中拉取信息
     */
    public void initDbData() {
        mModel.getDownItemFromDB(mComic.getId(), new Observer<List<DownInfo>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<DownInfo> mLists) {
                if(mLists!=null&&mLists.size()!=0){
                    mView.fillData(mLists);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

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
    public void pause(DownInfo info) {
        manager.pause(info);
    }

    /**
     * 开始某个下载
     * @param info
     */
    public void startDown(DownInfo info) {
        manager.startDown(info,mContext.getApplicationContext());
    }

    /**
     * 暂停所有下载
     */
    public void paseAll() {
    }

}
