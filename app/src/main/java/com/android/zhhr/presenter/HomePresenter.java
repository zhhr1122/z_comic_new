package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.db.helper.DaoHelper;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IHomeView;
import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.IntentUtil;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.PermissionUtils;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by 皓然 on 2017/6/15.
 */

public class HomePresenter extends BasePresenter<IHomeView> {
    private int num = 20;
    private ComicModule mModel;
    private List<Comic> mBanners, mDatas;
    private DaoHelper mHelper;
    private Comic recentComic;

    public List<Comic> getmBanners() {
        return mBanners;
    }

    public List<Comic> getmDatas() {
        return mDatas;
    }

    public HomePresenter(Activity context, IHomeView view) {
        super(context, view);
        this.mModel = new ComicModule(context);
        mHelper= new DaoHelper(context);
        mBanners = new ArrayList<>();
        mDatas = new ArrayList<>();
    }

    public void LoadData() {
        mModel.getData(new DisposableObserver<List<Comic>>() {
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

    public void toRecentComic() {
        IntentUtil.ToComicChapter(mContext,recentComic.getCurrentChapter(),recentComic);
    }

    public void getRecent() {
        recentComic = mHelper.findRecentComic();
        if(recentComic!=null){
            String recent = "续看:"+recentComic.getTitle()+" 第"+(recentComic.getCurrentChapter()+1)+"话";
            mView.fillRecent(recent);
        }else{
            mView.fillRecent(null);
        }
    }
}
