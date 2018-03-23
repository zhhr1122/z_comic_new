package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.db.DBChapters;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.custom.CustomDialog;
import com.android.zhhr.ui.view.ISelectDownloadView;
import com.android.zhhr.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by 张皓然 on 2018/1/24.
 */

public class SelectDownloadPresenter extends BasePresenter<ISelectDownloadView>{
    private ArrayList<String> mChapters;
    private HashMap<Integer,Integer> map;
    private boolean isSelectedAll ;
    private int SelectedNum = 0;
    private Comic mComic;
    private ComicModule mModel;
    private static final int DEFAULT_SELECTED_NUM = 20;

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
        this.mChapters = new ArrayList<>(mComic.getChapters());
        this.mModel = new ComicModule(mContext);
        this.isSelectedAll  = false;
        this.map = new HashMap<>();
    }

    /**
     * 自动选择
     */
    private void AutoSelected() {
        int defaultSelectNum = 0;
        if(mChapters!=null&&mChapters.size()!=0){
            for(int i=0;i<mChapters.size();i++){
                if(!map.containsKey(i)){
                    if(defaultSelectNum<DEFAULT_SELECTED_NUM){
                        map.put(i, Constants.CHAPTER_SELECTED);
                        defaultSelectNum++;
                        SelectedNum++;
                        isSelectedAll = true;
                    }else{
                        map.put(i, Constants.CHAPTER_FREE);
                        isSelectedAll = false;
                    }
                }else{
                    if(map.get(i) == Constants.CHAPTER_SELECTED ){
                        SelectedNum++;
                    }
                }
            }
            if(isSelectedAll){
                mView.addAll();
            }
        }
    }

    /**
     * 从数据库获取数据
     */
    public void getDataFormDb(){
        SelectedNum = 0;
        map.clear();
        mModel.getDownloadItemsFromDB(mComic.getId(), new DisposableObserver<List<DBChapters>>() {
            @Override
            public void onNext(@NonNull List<DBChapters> items) {
                for(int i=0;i<items.size();i++){
                    if(items.get(i).getState() == DownState.FINISH){
                        map.put(items.get(i).getChapters(), Constants.CHAPTER_DOWNLOAD);
                    }else{
                        map.put(items.get(i).getChapters(), Constants.CHAPTER_DOWNLOADING);
                    }
                }
                //自动选择
                AutoSelected();
                mView.updateList(map);
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
        if(map.get(position)!=null&&map.get(position).equals(Constants.CHAPTER_FREE)){
            SelectedNum++;
            map.put(position,Constants.CHAPTER_SELECTED);
            if(SelectedNum == map.size()){
                mView.addAll();
                isSelectedAll = true;
            }
        }else if(map.get(position)!=null&&map.get(position).equals(Constants.CHAPTER_SELECTED)){
            map.put(position,Constants.CHAPTER_FREE);
            SelectedNum--;
            isSelectedAll = false;
            mView.removeAll();
        }
        mView.updateListItem(map,position);
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
        mView.updateList(map);
    }

    public void startDownload() {
        if(SelectedNum>0){
            final CustomDialog customDialog = new CustomDialog(mContext,mComic.getTitle(),"共"+SelectedNum+"话，确定下载？");
            customDialog.setListener(new CustomDialog.onClickListener() {
                @Override
                public void OnClickConfirm() {
                    IntentUtil.ToDownloadListActivity(mContext,map,mComic);
                    if (customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                }

                @Override
                public void OnClickCancel() {
                    if (customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                }
            });
            customDialog.show();
        }else{
            mView.ShowToast("请选择下载章节");
        }

    }
}
