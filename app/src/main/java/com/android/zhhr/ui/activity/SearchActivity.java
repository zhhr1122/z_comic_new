package com.android.zhhr.ui.activity;

import android.content.Intent;

import com.android.zhhr.presenter.SearchPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.view.ISearchView;

import java.util.List;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView{
    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SearchPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void clearText() {

    }

    @Override
    public void fillDynamicResult(Object o) {

    }

    @Override
    public void fillResult(Object o) {

    }

    @Override
    public void fillHotRank(List ranks) {

    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(Object data) {

    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void ShowToast(String t) {

    }
}
