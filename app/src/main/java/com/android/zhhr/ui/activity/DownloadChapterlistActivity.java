package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.presenter.DownloadChapterlistPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.DownloadChapterlistAdapter;
import com.android.zhhr.ui.adapter.DownloadlistAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.view.IDownloadlistView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/2/12.
 */

public class DownloadChapterlistActivity extends BaseActivity<DownloadChapterlistPresenter> implements IDownloadlistView<List<DBDownloadItems>>{
    @Bind(R.id.tv_title)
    TextView mTitle;
    @Bind(R.id.rv_downloadlist)
    RecyclerView mRecyclerview;

    private DownloadChapterlistAdapter mAdapter;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new DownloadChapterlistPresenter(this,this,intent);
        mTitle.setText(((Comic)intent.getSerializableExtra(Constants.COMIC)).getTitle());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_downloadlist;
    }

    @Override
    protected void initView() {
        //mPresenter.getComic();
        mAdapter = new DownloadChapterlistAdapter(this,R.layout.item_downloadlist);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutmanager);
        mRecyclerview.setAdapter(mAdapter);
        //mPresenter.initDbData();
        mPresenter.initData();
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoadMoreData(List<DBDownloadItems> downInfos) {

    }

    @Override
    public void onStartAll() {
        mPresenter.startAll();
    }

    @Override
    public void onPauseAll() {
        mPresenter.paseAll();
    }

    @Override
    public void onSelectALL() {

    }

    @Override
    public void onDeleteAll() {

    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        this.finish();
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<DBDownloadItems> data) {
        mAdapter.updateWithClear(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataFinish() {

    }
}
