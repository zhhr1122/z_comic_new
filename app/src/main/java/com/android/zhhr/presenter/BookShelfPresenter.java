package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IBookShelfView;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.List;

import rx.Subscriber;

/**
 * Created by 皓然 on 2017/8/14.
 */

public class BookShelfPresenter extends BasePresenter<IBookShelfView>{

    private ComicModule mModel;

    public BookShelfPresenter(Activity context, IBookShelfView view) {
        super(context, view);
        this.mModel = new ComicModule(context);
    }


    public void loadData(){
        mModel.getMoreComicList(1,new Subscriber<List<Comic>>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onNext(List<Comic> comics) {
                mView.fillData(comics);
            }
        });
    }
}
