package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.db.helper.DaoHelper;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IDownloadlistView;
import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.IntentUtil;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    private DaoHelper helper;
    //下载图片队列
    private LinkedHashMap<String, DownloadComicDisposableObserver> subMap;
    //下载章节队列
    private TreeMap<Integer,DBDownloadItems> downloadMap;
    //下载章节数，同时允许存在四个
    private final static int downloadNum = 4;
    //已经下载完成的个数
    int downloadedNum = 0;
    /**
     * 0 下载中
     * 1 停止下载
     * 2 下载完成
     */
    public static final int DOWNLOADING = 0;
    public static final int STOP_DOWNLOAD = 1;
    public static final int FINISH = 2;
    public int isAllDownload = DOWNLOADING;


    public DownloadChapterlistPresenter(Activity context, IDownloadlistView view, Intent intent) {
        super(context, view);
        mComic= (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
        this.mModel = new ComicModule(mContext);
        helper = new DaoHelper(context);
        mLists = new ArrayList<>();
        subMap = new LinkedHashMap<>();
        downloadMap = new TreeMap<>();
    }

    public Comic getmComic() {
        return mComic;
    }

    /**
     * 初始化按照章節下載
     */
    public void initData() {
        //把数据存入数据库/从数据库拉取数据
        mModel.getDownloadItemsFromDB(mComic,mMap, new DisposableObserver<List<DBDownloadItems>>() {
            @Override
            public void onNext(@NonNull List<DBDownloadItems> items) {
                if(items!=null&&items.size()!=0){
                    //刷新列表
                    mLists.addAll(items);
                    mView.fillData(mLists);
                    //判断有多少是之前已经下载过的
                    for(int i=0;i<items.size();i++){
                        if(items.get(i).getState() == DownState.FINISH){
                            downloadedNum++;
                        }
                    }
                    //判断是否全部下载完了
                    if(downloadedNum == mLists.size()){
                        mView.onDownloadFinished();
                    }
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
                LogUtil.e(mComic.getTitle()+"从数据库中拉取本地下载列表数据失败"+e.toString());
            }

            @Override
            public void onComplete() {
                //拉取之后把新添加的数据整理一下
            }
        });
    }


    /**
     * 开始所有下载
     */
    public void startAll() {
        //找出最前面四个可以下载的bean
        for(int i=0;i<mLists.size();i++){
            if(mLists.get(i).getState()==DownState.NONE){
                if(downloadMap.size()<downloadNum){
                    startDown(mLists.get(i),i);
                }
            }
        }
    }

    /**
     * 重新下载
     */
    public void ReStartAll() {
        //找出最前面四个可以下载的bean
        for(int i=0;i<mLists.size();i++){
            if(mLists.get(i).getState()!=DownState.FINISH){
                mLists.get(i).setState(DownState.NONE);
                if(downloadMap.size()<downloadNum){
                    startDown(mLists.get(i),i);
                }else{
                    mView.getDataFinish();
                }
            }
        }
    }

    /**
     * 暂停某个下载
     * @param info
     */
    public void pause(DBDownloadItems info,int position) {
        LogUtil.d("testA","点击了暂停");
        if (info == null) return;
        info.setState(DownState.PAUSE);
        String url = info.getChapters_url().get(info.getCurrent_num()+1);
        if (subMap.containsKey(url)){
            DownloadComicDisposableObserver subscriber = subMap.get(url);
            subscriber.dispose();//解除请求
            subMap.remove(url);
            LogUtil.v(url+":暂停下载");
        }
        helper.update(info);
        mView.updateView(position);
    }

    /**
     * 暂停某个下载
     * @param info
     * @param position
     * @param isContinue 是否继续下载,自动去寻找下一个能下载的
     */
    public void stop(DBDownloadItems info,int position,boolean isContinue) {
        if (info == null) return;
        info.setState(DownState.STOP);
        //停止单张图片的下载
        if(info.getChapters_url()!=null&&info.getCurrent_num()+1<info.getChapters_url().size()){
            String url = info.getChapters_url().get(info.getCurrent_num()+1);
            if (subMap.containsKey(url)){
                DownloadComicDisposableObserver subscriber = subMap.get(url);
                subscriber.dispose();//解除请求
                subMap.remove(url);
                LogUtil.v(url+":停止下载");
            }
        }
        //中断整个章节的下载，并且切换章节
        if (downloadMap.containsKey(position)&&isContinue){
            downloadMap.remove(position);
            for(int i=0;i<mLists.size();i++){
                if(mLists.get(i).getState()==DownState.NONE){
                    downloadMap.put(i,mLists.get(i));
                    startDown(mLists.get(i),i);
                    break;
                }
            }
        }
        helper.update(info);
        mView.updateView(position);
    }

    /**
     * 开始某个下载
     * @param info
     */
    public void startDown(final DBDownloadItems info, final int position) {
        //加入到下载队列中
        downloadMap.put(position,info);
        //首先判断是否已经获取过下载地址
        if(info.getNum()==0){
            //获取下载地址
            mModel.getDownloadChaptersList(mComic.getId()+"",info.getChapters(),new Observer<ArrayList<String>>(){

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    //修改状态
                    info.setState(DownState.START);
                    mView.updateView(position);
                }

                @Override
                public void onNext(@NonNull ArrayList<String> mLists) {
                    //修改状态
                   if(info.getState()!=DownState.STOP){
                       info.setState(DownState.DOWN);
                       //设置下载地址
                       info.setChapters_url(mLists);
                       info.setNum(mLists.size());
                       info.setCurrent_num(0);
                       //把获取到的下载地址存进数据库
                       helper.update(info);
                       mView.updateView(position);
                       if(mLists!=null&&mLists.size()!=0){
                           DownloadChapter(info,info.getCurrent_num(),position);
                       }
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
            mView.updateView(position);
            DownloadChapter(info,info.getCurrent_num(),position);
        }
    }

    /**
     * 递归下载每一话的所有图片
     * @param info
     * @param page
     */
    private void DownloadChapter(final DBDownloadItems info, final int page,int postion) {
        DownloadComicDisposableObserver observer = new DownloadComicDisposableObserver(page,postion);
        //mModel.download(info.getChapters_url().get(page), FileUtil.SDPATH + FileUtil.COMIC + mComic.getId() + "/" + info.getChapters()+"/", page+".png", observer);
        mModel.download(info, page, observer);
        subMap.put(info.getChapters_url().get(page),observer);
    }

    /**
     * 设置为可以下载的等待状态
     * @param items
     * @param position
     */
    public void ready(DBDownloadItems items, int position) {
        if(downloadMap.size()<downloadNum){
            startDown(items,position);
        }else{
            items.setState(DownState.NONE);
            helper.update(items);
            mView.updateView(position);
        }
    }

    public void ToComicChapter(DBDownloadItems info) {
        //IntentUtil.ToComicChapter();
    }

    /**
     * 下载图片的回调
     */
    public class DownloadComicDisposableObserver extends DisposableObserver<DBDownloadItems> {
        int page;
        DBDownloadItems info;
        int postion;
        public DownloadComicDisposableObserver(int page,int position){
            this.page = page;
            this.postion = position;
        }

        @Override
        public void onNext(@NonNull DBDownloadItems dbDownloadItems) {
            info = dbDownloadItems;
            LogUtil.d(page+"/"+info.getNum()+"下载完成");
            //从队列中移除
            subMap.remove(info.getChapters_url().get(page));
            //写一个递归继续去下载这一话的下一张图片
            if(page<info.getNum()-1){
                if(info.getState() == DownState.DOWN){
                    DownloadChapter(info,page+1,postion);
                }
            }else {
                //如果这一话下载完成
                downloadedNum++;
                //修改状态
                info.setState(DownState.FINISH);
                downloadMap.remove(postion);
                //遍历去寻找下一话
                for(int i=0;i<mLists.size();i++){
                    if(mLists.get(i).getState()==DownState.NONE){
                        downloadMap.put(i,mLists.get(i));
                        //开始下载下一话
                        startDown(mLists.get(i),i);
                        break;
                    }
                }
                if(downloadedNum == mLists.size()){
                    mView.ShowToast(mComic.getTitle()+"下载完成,共下载"+downloadedNum+"话");
                    mView.onDownloadFinished();
                    isAllDownload = FINISH;
                }
            }
            //把已经下载完成的写入
            info.setCurrent_num(page+1);
            //更新数据库
            helper.update(info);
            //为了防止点击了stop之后，仍然刷新UI，即使图片下载已经完成，仍不刷新UI
            if(info.getState() !=DownState.STOP){
                mView.updateView(postion);
            }

        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

    /**
     * 暂停所有下载
     */
    public void pauseAll() {
        mModel.updateDownloadItemsList(mLists, new DisposableObserver<Boolean>() {

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if(aBoolean){
                    LogUtil.d("所有状态保存在数据库成功");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtil.e(e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 停止所有下载
     */
    public void stopAll() {
        for(int i=0;i<mLists.size();i++){
            DBDownloadItems items = mLists.get(i);
            if (items.getState() != DownState.FINISH){
                items.setState(DownState.STOP);
                if(items.getState() == DownState.DOWN){
                    stop(items,i,false);
                }
                downloadMap.clear();
            }
        }
        mView.getDataFinish();
    }
}
