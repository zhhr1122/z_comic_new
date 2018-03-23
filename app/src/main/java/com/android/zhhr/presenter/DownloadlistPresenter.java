package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.net.download.HttpDownManager;
import com.android.zhhr.ui.view.IDownloadlistView;

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

public class DownloadlistPresenter extends BasePresenter<IDownloadlistView>{
    private Comic mComic;
    private HashMap<Integer,Integer> mMap;
    private ComicModule mModel;
    private ArrayList<DownInfo> mLists;
    private HttpDownManager manager;

    public DownloadlistPresenter(Activity context, IDownloadlistView view, Intent intent) {
        super(context, view);
        mComic= (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
        manager = HttpDownManager.getInstance(mContext.getApplicationContext());
        this.mModel = new ComicModule(mContext);
    }

    /**
     * 初始化按照章節下載
     */
    public void initData() {
        mLists = new ArrayList<>();
        DownInfo item;
        //把hashmap進行排序操作
        List<Map.Entry<Integer,Integer>> list = new ArrayList<>(mMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        for(Map.Entry<Integer,Integer> mapping:list){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
            if(mapping.getValue() != Constants.CHAPTER_FREE){
                item = new DownInfo("http://sw.bos.baidu.com/sw-search-sp/software/f84db5c1e9853/QQPhoneManager_5.8.1.5162.exe");
                item.setId(mapping.getKey());
                item.setState(DownState.START);
                item.setSavePath(mapping.getKey()+"--"+mComic.getChapters().get(mapping.getKey()));
                mLists.add(item);
            }
        }
        if(mLists!=null&&mLists.size()!=0){
            mView.fillData(mLists);
        }
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
                mModel.getDownloadChaptersList(mComic,mapping.getKey(),new Observer<ArrayList<DownInfo>>(){

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

    public void getComic(){

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
