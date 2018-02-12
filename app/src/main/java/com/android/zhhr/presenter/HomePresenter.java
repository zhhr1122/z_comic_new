package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IHomeView;
import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.PermissionUtils;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by 皓然 on 2017/6/15.
 */

public class HomePresenter extends BasePresenter<IHomeView> {
    private int num = 20;
    private ComicModule mModel;
    private List<Comic> mBanners, mDatas;

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

    public void LoadData() {
        mModel.getData(new Observer<List<Comic>>() {
            @Override
            public void onError(Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onComplete() {
                mView.getDataFinish();
            }

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(List<Comic> comics) {
                if (comics.size() > 12) {
                    mView.fillData(comics);
                    mDatas = comics;
                } else {
                    mView.fillBanner(comics);
                    mBanners = comics;
                }
            }

        });
    }

    public void refreshData() {
        mModel.refreshData(new Observer<List<Comic>>() {

            @Override
            public void onError(Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(List<Comic> comics) {
                mView.fillData(comics);
                mDatas = comics;
            }
        });
    }


    public void loadMoreData(int page) {
        mModel.getMoreComicList(page, new Observer<List<Comic>>() {

            @Override
            public void onError(Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onComplete() {
                mView.getDataFinish();
            }

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(List<Comic> comics) {
                mView.appendMoreDataToView(comics);
            }
        });
    }

}
