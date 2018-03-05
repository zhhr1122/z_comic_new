package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.ICollectionView;
import com.android.zhhr.utils.ShowErrorTextUtil;

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

    public HistoryPresenter(Activity context, ICollectionView view) {
        super(context, view);
        this.mModel = new ComicModule(context);
    }


    public void loadData(){
        mModel.getHistoryComicList(getCurrentTime(),new DisposableObserver<List<Comic>>() {

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
                mView.fillData(comics);
            }
        });
    }
}
