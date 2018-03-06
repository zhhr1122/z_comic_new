package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.custom.CustomDialog;
import com.android.zhhr.ui.view.IBaseView;
import com.android.zhhr.ui.view.ICollectionView;
import com.android.zhhr.ui.view.ILoadDataView;
import com.android.zhhr.ui.view.ISelectDataView;
import com.android.zhhr.utils.IntentUtil;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by 张皓然 on 2018/3/6.
 */

public abstract class SelectPresenter<T extends ISelectDataView> extends BasePresenter<T>{
    protected ComicModule mModel;
    protected HashMap<Integer,Integer> mMap;
    protected boolean isSelectedAll ;
    protected int SelectedNum = 0;
    protected List<Comic> mComics;

    public SelectPresenter(Activity context, T view) {
        super(context, view);
        this.mModel = new ComicModule(context);
        this.mMap = new HashMap<>();
        this.isSelectedAll  = false;
        this.mComics = new ArrayList<>();
    }

    /**
     * 加载数据
     */
    public abstract void loadData();

    /**
     * 选择或者取消选择
     * @param position
     */
    public void uptdateToSelected(int position){
        if(mMap.get(position)!=null&&mMap.get(position).equals(Constants.CHAPTER_FREE)){
            SelectedNum++;
            mMap.put(position,Constants.CHAPTER_SELECTED);
            if(SelectedNum == mComics.size()){
                mView.addAll();
                isSelectedAll = true;
            }
        }else if(mMap.get(position)!=null&&mMap.get(position).equals(Constants.CHAPTER_SELECTED)){
            mMap.put(position,Constants.CHAPTER_FREE);
            SelectedNum--;
            isSelectedAll = false;
            mView.removeAll();
        }
        mView.updateListItem(mMap,position);
    }

    /**
     * 全选或全部删除
     */
    public void SelectOrMoveAll(){
        if(!isSelectedAll){
            if(mComics!=null&&mComics.size()!=0){
                for(int i=0;i<mComics.size();i++){
                    if(mMap.get(i) == Constants.CHAPTER_FREE){
                        mMap.put(i, Constants.CHAPTER_SELECTED);
                        SelectedNum++;
                    }
                }
                mView.addAll();
            }
        }else{
            if(mComics!=null&&mComics.size()!=0){
                for(int i=0;i<mComics.size();i++){
                    if(mMap.get(i) == Constants.CHAPTER_SELECTED){
                        mMap.put(i, Constants.CHAPTER_FREE);
                    }
                }
                SelectedNum = 0;
                mView.removeAll();
            }
        }
        isSelectedAll = !isSelectedAll;
        mView.updateList(mMap);
    }



    /**
     * 重置选择信息
     */
    public void resetSelect(){
        for(int i=0;i<mComics.size();i++){
            if(!mMap.containsKey(i)){
                if(isSelectedAll){
                    mMap.put(i,Constants.CHAPTER_SELECTED);
                }else{
                    mMap.put(i,Constants.CHAPTER_FREE);
                }
            }
        }
    }

    /**
     * 清除map信息
     */
    public void clearSelect(){
        SelectedNum = 0;
        isSelectedAll = false;
        for(int i=0;i<mComics.size();i++){
            mMap.put(i,Constants.CHAPTER_FREE);
        }
        mView.updateList(mMap);
    }

    /**
     * 删除漫画
     */
    public abstract void deleteComic();

    public void ShowDeteleDialog(){
        if(SelectedNum>0){
            final CustomDialog customDialog = new CustomDialog(mContext,"提示","确认删除选中的漫画？");
            customDialog.setListener(new CustomDialog.onClickListener() {
                @Override
                public void OnClickConfirm() {
                    deleteComic();
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
            mView.ShowToast("请选择需要删除的作品");
        }
    }
}
