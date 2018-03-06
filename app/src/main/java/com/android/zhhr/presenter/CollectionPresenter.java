package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.view.ICollectionView;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by 皓然 on 2017/8/14.
 */

public class CollectionPresenter extends SelectPresenter<ICollectionView>{

    public CollectionPresenter(Activity context, ICollectionView view) {
        super(context, view);
    }


    public void loadData(){
        mModel.getCollectedComicList(new DisposableObserver<List<Comic>>() {

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
                mComics= comics;
                mView.fillData(comics);
                resetSelect();
            }
        });
    }
}
