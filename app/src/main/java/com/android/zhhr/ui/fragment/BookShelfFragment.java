package com.android.zhhr.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.BookShelfPresenter;
import com.android.zhhr.ui.adapter.BookShelfAdapter;
import com.android.zhhr.ui.custom.DividerGridItemDecoration;
import com.android.zhhr.ui.custom.ElasticRecycleView;
import com.android.zhhr.ui.view.IBookShelfView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

import butterknife.Bind;

/**
 * Created by 皓然 on 2017/8/7.
 */

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements IBookShelfView<List<Comic>> {
    @Bind(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    private BookShelfAdapter mAdapter;

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;


    @Override
    protected void initPresenter() {
        mPresenter = new BookShelfPresenter(mActivity,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {


        GridLayoutManager layoutManager = new GridLayoutManager(mActivity,3);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new BookShelfAdapter(mActivity,R.layout.item_bookshelf);


        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        ImageView foot = new ImageView(mActivity);
        foot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        foot.setImageResource(R.mipmap.no_more);
        foot.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mHeaderAndFooterWrapper.addFootView(foot);

        mRecycleView.setAdapter(mHeaderAndFooterWrapper);
        mPresenter.loadData();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void getDataFinish() {

    }


    @Override
    public void showErrorView(String throwable) {
        mPresenter.loadData();
        ShowToast("重新加载");
    }

    @Override
    public void fillData(List<Comic> data) {
        if(data!=null&&data.size()!=0){
            mAdapter.updateWithClear(data);
            mRecycleView.smoothScrollToPosition(3);
        }else {
            ShowToast("未取到数据");
        }
    }

    @Override
    public void showEmptyView() {

    }
}
