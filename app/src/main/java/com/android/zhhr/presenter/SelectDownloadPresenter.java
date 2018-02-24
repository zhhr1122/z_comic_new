package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.ISelectDownloadView;
import com.android.zhhr.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by 张皓然 on 2018/1/24.
 */

public class SelectDownloadPresenter extends BasePresenter<ISelectDownloadView>{
    /*//已经选中的章节
    private ArrayList<Integer> SelectedChapters;
    //已经下载的章节
    private ArrayList<Integer> DownloadedChapters;*/

    private ArrayList<String> mChapters;

    private HashMap<Integer,Integer> map;
    private boolean isSelectedAll ;
    private int SelectedNum = 0;
    private Comic mComic;
    private ComicModule mModel;

    public int getSelectedNum() {
        return SelectedNum;
    }

    public HashMap<Integer,Integer> getMap(){
        return map;
    }

    public ArrayList<String> getmChapters() {
        return mChapters;
    }

    public void setmChapters(ArrayList<String> mChapters) {
        this.mChapters = mChapters;
    }

    public Comic getmComic() {
        return mComic;
    }

    public void setmComic(Comic mComic) {
        this.mComic = mComic;
    }

    public SelectDownloadPresenter(Activity context, ISelectDownloadView view, Intent intent) {
        super(context, view);
        this.mComic = (Comic) intent.getSerializableExtra(Constants.COMIC);
        this.mChapters = (ArrayList<String>) mComic.getChapters();
        this.mModel = new ComicModule(mContext);
        this.isSelectedAll  = false;
        initData();
    }

    private void initData() {
        /*SelectedChapters = new ArrayList<>();
        DownloadedChapters = new ArrayList<>();*/
        map = new HashMap<>();
        if(mChapters!=null&&mChapters.size()!=0){
            for(int i=0;i<mChapters.size();i++){
                map.put(i, Constants.CHAPTER_FREE);
            }
        }
    }

    public void getDataFormDb(){
        mModel.getDownloadItemsFromDB(mComic.getId(), new Observer<List<DBDownloadItems>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<DBDownloadItems> items) {
                for(int i=0;i<items.size();i++){
                    map.put(items.get(i).getChapters(), Constants.CHAPTER_DOWNLOAD);
                    mView.updateDownloadList(map);
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

    public void uptdateToSelected(int position){
        //SelectedChapters.add(position);

        if(map.get(position)!=null&&map.get(position).equals(Constants.CHAPTER_FREE)){
            SelectedNum++;
            map.put(position,Constants.CHAPTER_SELECTED);
        }else if(map.get(position)!=null&&map.get(position).equals(Constants.CHAPTER_SELECTED)){
            map.put(position,Constants.CHAPTER_FREE);
            SelectedNum--;
            isSelectedAll = false;
            mView.removeAll();
        }
        mView.updateDownloadList(map);
    }

    public void SelectOrMoveAll(){
        if(!isSelectedAll){
            if(mChapters!=null&&mChapters.size()!=0){
                for(int i=0;i<mChapters.size();i++){
                    if(map.get(i) == Constants.CHAPTER_FREE){
                        map.put(i, Constants.CHAPTER_SELECTED);
                        SelectedNum++;
                    }
                }
                mView.addAll();
            }
        }else{
            if(mChapters!=null&&mChapters.size()!=0){
                for(int i=0;i<mChapters.size();i++){
                    if(map.get(i) == Constants.CHAPTER_SELECTED){
                        map.put(i, Constants.CHAPTER_FREE);
                    }
                }
                SelectedNum = 0;
                mView.removeAll();
            }
        }
        isSelectedAll = !isSelectedAll;
        mView.updateDownloadList(map);
    }

    public void startDownload() {
        IntentUtil.ToDownloadListActivity(mContext,map,mComic);
    }
}
