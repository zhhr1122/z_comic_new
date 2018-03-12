package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.RankPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.RankAdapter;
import com.android.zhhr.ui.custom.ElasticScrollView;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
import com.android.zhhr.ui.view.IRankView;
import com.android.zhhr.utils.IntentUtil;
import com.android.zhhr.utils.LogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 张皓然 on 2018/3/9.
 */

public class RankActivity extends BaseActivity<RankPresenter> implements IRankView<List<Comic>>{
    @Bind(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    RankAdapter mAdapter;
    @Bind(R.id.ev_scrollview)
    ElasticScrollView mScrollView;
    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new RankPresenter(this,this);
        mAdapter = new RankAdapter(this,R.layout.item_rank,R.layout.item_loading);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_rank;
    }

    @Override
    protected void initView() {
        mPresenter.loadData("pgv");
        final NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(this,1);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mAdapter);
        mScrollView.setListener(new ElasticScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadData("pgv");
            }
        });
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {
        mAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        this.finish();
    }

    @OnClick(R.id.iv_search)
    public void ToSearch(View view){
        IntentUtil.ToSearch(this);
    }
}
