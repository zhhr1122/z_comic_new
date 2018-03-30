package com.android.zhhr.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.NewListPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.NewListAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.ElasticImageScrollView;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
import com.android.zhhr.ui.view.INewView;
import com.android.zhhr.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhhr on 2018/3/19.
 */

public class NewListActivity extends BaseActivity<NewListPresenter> implements INewView<List<Comic>> {

    @Bind(R.id.rv_bookshelf)
    RecyclerView mRecyclerView;
    @Bind(R.id.ev_scrollview)
    ElasticImageScrollView mScrollView;
    @Bind(R.id.rl_title)
    RelativeLayout mTitle;

    private NewListAdapter mAdapter;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new NewListPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_new;
    }

    @Override
    protected void initView() {
        mAdapter= new NewListAdapter(this,R.layout.item_newlist,R.layout.item_loading);

        NoScrollGridLayoutManager gridLayoutManager = new NoScrollGridLayoutManager(this,1);
        gridLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mScrollView.setListener(new ElasticImageScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadData();
            }

            @Override
            public void onAlphaActionBar(float a) {
                mTitle.setAlpha(a);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(a!=0){
                        initStatusBar(false);
                    }else{
                        initStatusBar(true);
                    }
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if(position!=mAdapter.getItemCount()-1&&position>=0){
                    Comic comic = mAdapter.getItems(position);
                    IntentUtil.ToComicDetail(NewListActivity.this,comic.getId()+"",comic.getTitle());
                }
            }
        });

        mPresenter.loadData();

    }


    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {
        mAdapter.updateWithClear(data);
        mAdapter.notifyDataSetChanged();
        mScrollView.setInnerHeight();
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void ShowToast(String t) {

    }

    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        super.finish();
    }
}
