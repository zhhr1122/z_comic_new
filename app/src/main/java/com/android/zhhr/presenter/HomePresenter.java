package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IHomeView;
import com.android.zhhr.utils.ShowErrorTextUtil;
import com.android.zhhr.utils.TencentComicAnalysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 皓然 on 2017/6/15.
 */

public class HomePresenter extends BasePresenter<IHomeView>{
    private int num =20;
    private ComicModule mModel;
    private List<Comic> mBanners,mDatas;

    public List<Comic> getmBanners() {
        return mBanners;
    }

    public List<Comic> getmDatas() {
        return mDatas;
    }

    public HomePresenter(Activity context, IHomeView view) {
        super(context, view);
        this.mModel = new ComicModule(context);
        mBanners = new ArrayList<>();
        mDatas = new ArrayList<>();
    }
    public void refreshData(){
        mModel.getData(new Subscriber<List<Comic>>() {
            @Override
            public void onCompleted() {
                //
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onNext(List<Comic> comics) {
                if(comics.size()>12){
                    mView.fillData(comics);
                    mDatas = comics;
                    mView.getDataFinish();
                }else{
                    mView.fillBanner(comics);
                    mBanners = comics;
                }
            }

        });
    }


    public void loadMoreData(int page){
        mModel.getMoreComicList(page,new Subscriber<List<Comic>>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onNext(List<Comic> movieEntities) {
                mView.appendMoreDataToView(movieEntities);
            }
        });
    }

}
