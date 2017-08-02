package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.MainPresenter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.MainAdapter;
import com.android.zhhr.ui.custom.DividerGridItemDecoration;
import com.android.zhhr.ui.custom.NoScrollStaggeredGridLayoutManager;
import com.android.zhhr.ui.custom.ZElasticRefreshScrollView;
import com.android.zhhr.ui.view.IMainView;
import com.android.zhhr.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainView<Comic>,BaseRecyclerAdapter.OnItemClickListener{
    @Bind(R.id.recycle_view)
    RecyclerView mRecycleView;

    @Bind(R.id.sv_comic)
    ZElasticRefreshScrollView mScrollView;
    @Bind(R.id.rl_error_view)
    RelativeLayout mErrorView;
    @Bind(R.id.iv_error)
    ImageView mReload;
    private MainAdapter mAdapter;



    private int i=3;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new MainPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        NoScrollStaggeredGridLayoutManager layoutManager = new NoScrollStaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(this,R.layout.item_image);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(this));
        mPresenter.refreshData();
        mScrollView.setRefreshListener(new ZElasticRefreshScrollView.RefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshData();
            }

            @Override
            public void onRefreshFinish() {
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData(i);
                i++;
            }
        });
    }




    @Override
    public void getDataFinish() {
        mScrollView.setRefreshing(false);
        if(mErrorView.isShown()){
            mErrorView.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorView(Throwable throwable) {
        mScrollView.setRefreshing(false);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void hideRefresh() {

    }

    public void fillData(List<Comic> data) {
        if(data!=null&&data.size()!=0){
            mAdapter.updateWithClear(data);
        }else {
            showToast("未取到数据");
        }
    }

    @Override
    public void appendMoreDataToView(List<Comic> data) {
        if(data!=null&&data.size()!=0){
            mAdapter.update(data);
        }else {
            showToast("未取到数据");
        }
    }

    @Override
    public void hasNoMoreData() {

    }

    @Override
    public void ShowToast(String toast) {
        showToast(toast);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Comic comic = mAdapter.getItems(position);
        IntentUtil.ToComicDetail(this,comic.getId(),comic.getTitle());
    }

    @OnClick(R.id.iv_error)
    public void ReloadData(View view){
        mErrorView.setVisibility(View.GONE);
        mPresenter.refreshData();
    }
}
