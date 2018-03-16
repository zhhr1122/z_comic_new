package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.RankPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.RankAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.CustomTab;
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
    @Bind(R.id.ll_actionbar)
    CustomTab mTab;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new RankPresenter(this,this);
        mAdapter = new RankAdapter(this,R.layout.item_rank,R.layout.item_loading);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_rank;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        initStatusBar(false);
        mPresenter.loadData();
        final NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(this,1);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mAdapter);
        mScrollView.setListener(new ElasticScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadData();
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mAdapter.getItems(position);
                IntentUtil.ToComicDetail(RankActivity.this,comic.getId()+"",comic.getTitle());
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
        super.finish();
    }

    @OnClick(R.id.iv_search)
    public void ToSearch(View view){
        IntentUtil.ToSearch(this);
    }

    @OnClick({ R.id.rl_update, R.id.rl_sellgood, R.id.rl_hot,R.id.rl_mouth})
    public void getType(View view) {
        switch (view.getId()) {
            case R.id.rl_update:
                mPresenter.setType("upt");
                break;
            case R.id.rl_sellgood:
                mPresenter.setType("pay");
                break;
            case R.id.rl_hot:
                mPresenter.setType("pgv");
                break;
            case R.id.rl_mouth:
                mPresenter.setType("mt");
                break;
        }
    }

    @Override
    public void setType(int position) {
        mTab.setCurrentPosition(position);
    }
}
