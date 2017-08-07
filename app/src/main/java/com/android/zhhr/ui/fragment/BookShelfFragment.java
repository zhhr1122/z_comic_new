package com.android.zhhr.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.HomePresenter;
import com.android.zhhr.ui.view.IHomeView;

import java.util.List;

/**
 * Created by 皓然 on 2017/8/7.
 */

public class BookShelfFragment extends BaseFragment<HomePresenter> implements IHomeView<Comic>{


    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter(mActivity,this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    public void ShowToast(String t) {

    }

    @Override
    public void getDataFinish() {

    }



    @Override
    public void fillData(List<Comic> data) {

    }

    @Override
    public void appendMoreDataToView(List<Comic> data) {

    }

    @Override
    public void hasNoMoreData() {

    }

    @Override
    public void showErrorView(String throwable) {

    }
}
