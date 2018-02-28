package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IBookShelfView;
import com.android.zhhr.ui.view.ICollectionView;

/**
 * Created by 张皓然 on 2018/2/28.
 */

public class BookShelfPresenter extends BasePresenter<IBookShelfView> {
    public BookShelfPresenter(Activity context, IBookShelfView view) {
        super(context, view);
    }
}
