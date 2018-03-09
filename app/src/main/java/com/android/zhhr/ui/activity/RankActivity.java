package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.RankPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.RankAdapter;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
import com.android.zhhr.ui.view.IRankView;
import com.android.zhhr.utils.IntentUtil;

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
    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new RankPresenter(this,this);
        mAdapter = new RankAdapter(this,R.layout.item_homepage_full);
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
