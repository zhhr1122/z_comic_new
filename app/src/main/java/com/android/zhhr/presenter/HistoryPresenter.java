package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.HomeTitle;
import com.android.zhhr.data.entity.LoadingItem;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.ICollectionView;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by 皓然 on 2017/8/14.
 */

public class HistoryPresenter extends SelectPresenter<ICollectionView>{
    int page = 1;
    private boolean isloadingdata;
    private List<Comic> mHistoryList;

    public HistoryPresenter(Activity context, ICollectionView view) {
        super(context, view);
        mHistoryList = new ArrayList<>();
    }

    public void SelectOrMoveAll(){
        if(!isSelectedAll){
            if(mHistoryList!=null&&mHistoryList.size()!=0){
                for(int i=0;i<mHistoryList.size();i++){
                    if(mMap.get(i) == Constants.CHAPTER_FREE){
                        mMap.put(i, Constants.CHAPTER_SELECTED);
                        SelectedNum++;
                    }
                }
                mView.addAll();
            }
        }else{
            if(mHistoryList!=null&&mHistoryList.size()!=0){
                for(int i=0;i<mHistoryList.size();i++){
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


    public void loadData(){
        page = 1;
        mModel.getHistoryComicList(0,new DisposableObserver<List<Comic>>() {

            @Override
            public void onError(Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onComplete() {
                mView.getDataFinish();
            }


            @Override
            public void onNext(List<Comic> comics) {
                mComics.clear();
                mComics.addAll(comics);
                mView.fillData(addTitle(comics));
            }
        });
    }

    public void loadMoreData(){
        if(!isloadingdata){
            mModel.getHistoryComicList(page,new DisposableObserver<List<Comic>>() {
                @Override
                protected void onStart() {
                    super.onStart();
                    isloadingdata = true;
                }

                @Override
                public void onError(Throwable e) {
                    mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
                }

                @Override
                public void onComplete() {
                    mView.getDataFinish();
                }


                @Override
                public void onNext(List<Comic> comics) {
                    page++;
                    mComics.addAll(comics);
                    mView.fillData(addTitle(comics));
                    isloadingdata = false;
                }
            });
        }
    }

    private List<Comic> addTitle(List<Comic> comics) {
        List<Comic> todays = new ArrayList<>();
        List<Comic> treedays = new ArrayList<>();
        List<Comic> weekenddays = new ArrayList<>();
        List<Comic> earlierdays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Long currentMillisecond = calendar.getTimeInMillis();
        for(int i=0;i<mComics.size();i++){
            long millisecond  = mComics.get(i).getClickTime();
            //间隔秒
            Long spaceSecond = (currentMillisecond - millisecond) / 1000;
            if (spaceSecond / (60 * 60) < 24) {
                todays.add(mComics.get(i));

            } else if (spaceSecond/(60*60*24)<3){
                treedays.add(mComics.get(i));

            } else if (spaceSecond/(60*60*24)<7){
                weekenddays.add(mComics.get(i));

            } else {
                earlierdays.add(mComics.get(i));
            }
        }
        List<Comic> history = new ArrayList<>();
        if(todays.size()!=0){
            history.add(new HomeTitle("今天"));
            history.addAll(todays);
        }
        if(treedays.size()!=0){
            history.add(new HomeTitle("过去三天"));
            history.addAll(treedays);
        }
        if(weekenddays.size()!=0){
            history.add(new HomeTitle("过去一周"));
            history.addAll(weekenddays);
        }
        if(earlierdays.size()!=0){
            history.add(new HomeTitle("更早"));
            history.addAll(earlierdays);
        }
        if(mComics.size()>=12){
            if(comics.size()==0){
                history.add(new LoadingItem(false));
            }else{
                history.add(new LoadingItem(true));
            }
        }
        mHistoryList = history;
        resetSelect(history);
        return history;
    }
}
