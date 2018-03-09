package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IIndexView;
import com.android.zhhr.ui.view.IRankView;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by 张皓然 on 2018/3/9.
 */

public class RankPresenter extends BasePresenter<IRankView>{
    private ComicModule mModel;
    private int page;

    public RankPresenter(Activity context, IRankView view) {
        super(context, view);
        mModel = new ComicModule(context);
    }

    public void loadData(String type) {
        page = 2;
        mModel.getRankList(getCurrentTime(),type,page, new DisposableObserver<List<Comic>>() {
            @Override
            public void onNext(@NonNull List<Comic> comics) {
                mView.fillData(comics);
                //mView.ShowToast(comics.get(0).getTitle());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onComplete() {
                mView.getDataFinish();
            }
        });

    }
}
