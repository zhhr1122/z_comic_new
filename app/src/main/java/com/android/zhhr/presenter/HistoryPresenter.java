package com.android.zhhr.presenter;

import android.app.Activity;

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

public class HistoryPresenter extends BasePresenter<ICollectionView>{

    private ComicModule mModel;
    private List<Comic> mLists;
    int page = 1;
    private boolean isloadingdata;

    public HistoryPresenter(Activity context, ICollectionView view) {
        super(context, view);
        this.mModel = new ComicModule(context);
        mLists = new ArrayList<>();
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
                mLists.clear();
                mLists.addAll(comics);
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
                    mLists.addAll(comics);
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
        for(int i=0;i<mLists.size();i++){
            long millisecond  = mLists.get(i).getClickTime();
            //间隔秒
            Long spaceSecond = (currentMillisecond - millisecond) / 1000;
            if (spaceSecond / (60 * 60) < 24) {
                todays.add(mLists.get(i));

            } else if (spaceSecond/(60*60*24)<3){
                treedays.add(mLists.get(i));

            } else if (spaceSecond/(60*60*24)<7){
                weekenddays.add(mLists.get(i));

            } else {
                earlierdays.add(mLists.get(i));
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
        if(mLists.size()>=12){
            if(comics.size()==0){
                history.add(new LoadingItem(false));
            }else{
                history.add(new LoadingItem(true));
            }
        }

        return history;
    }
}
