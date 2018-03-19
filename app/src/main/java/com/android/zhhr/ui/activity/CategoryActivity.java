package com.android.zhhr.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.Type;
import com.android.zhhr.presenter.CategoryPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.CategoryAdapter;
import com.android.zhhr.ui.adapter.CategoryListAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.ElasticHeadScrollView;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
import com.android.zhhr.ui.view.ICategoryView;
import com.android.zhhr.utils.IntentUtil;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhhr on 2018/3/16.
 */

public class CategoryActivity extends BaseActivity<CategoryPresenter> implements ICategoryView<List<Comic>>{
    @Bind(R.id.rv_select)
    RecyclerView mSelectRecyclerView;

    @Bind(R.id.rv_bookshelf)
    RecyclerView mSelectListRecyclerView;
    @Bind(R.id.ev_scrollview)
    ElasticHeadScrollView mScrollView;

    CategoryAdapter mSelectAdapter;

    CategoryListAdapter mCategoryAdapter;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new CategoryPresenter(this,this,intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_category;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        initStatusBar(false);
        mSelectAdapter = new CategoryAdapter(this,R.layout.item_categroy_select);
        mCategoryAdapter = new CategoryListAdapter(this,R.layout.item_homepage_three,R.layout.item_loading);

        NoScrollGridLayoutManager gridLayoutManager = new NoScrollGridLayoutManager(this,7);
        gridLayoutManager.setScrollEnabled(false);
        mSelectRecyclerView.setLayoutManager(gridLayoutManager);
        mSelectRecyclerView.setAdapter(mSelectAdapter);


        NoScrollGridLayoutManager ListgridLayoutManager = new NoScrollGridLayoutManager(this,3);
        ListgridLayoutManager.setScrollEnabled(false);
        mSelectListRecyclerView.setLayoutManager(ListgridLayoutManager);
        mSelectListRecyclerView.setAdapter(mCategoryAdapter);


        mSelectAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Type type = mSelectAdapter.getItems(position);
                mPresenter.onItemClick(type,position);
            }
        });

        mScrollView.setListener(new ElasticHeadScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadCategoryList();
            }
        });

        mCategoryAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mCategoryAdapter.getItems(position);
                IntentUtil.ToComicDetail(CategoryActivity.this,comic.getId()+"",comic.getTitle());
            }
        });

        mPresenter.loadData();
    }

    @Override
    public void fillSelectData(List<Type> mList,Map<String, Integer> mMap) {
        mSelectAdapter.setSelectMap(mMap);
        mSelectAdapter.updateWithClear(mList);
        mSelectAdapter.notifyDataSetChanged();
        mScrollView.setInnerHeight();
    }

    @Override
    public void setMap(Map<String, Integer> mMap) {
        mSelectAdapter.setSelectMap(mMap);
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {
        mCategoryAdapter.updateWithClear(data);
        mCategoryAdapter.notifyDataSetChanged();
        mScrollView.setInnerHeight();
    }

    @Override
    public void getDataFinish() {

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
}
