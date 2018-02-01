package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.ui.view.ISearchView;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchPresenter extends BasePresenter<ISearchView>{
    public SearchPresenter(Activity context, ISearchView view) {
        super(context, view);
    }
}
