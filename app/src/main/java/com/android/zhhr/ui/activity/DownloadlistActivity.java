package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.net.download.HttpDownManager;
import com.android.zhhr.presenter.DownloadlistPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.DownloadlistAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.view.IDownloadlistView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/2/12.
 */

public class DownloadlistActivity extends BaseActivity<DownloadlistPresenter> implements IDownloadlistView<List<DownInfo>>{
    @Bind(R.id.tv_title)
    TextView mTitle;
    @Bind(R.id.rv_downloadlist)
    RecyclerView mRecyclerview;

    private DownloadlistAdapter mAdapter;
    private HttpDownManager manager;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new DownloadlistPresenter(this,this,intent);
        mTitle.setText(((Comic)intent.getSerializableExtra(Constants.COMIC)).getTitle());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_downloadlist;
    }

    @Override
    protected void initView() {
        //mPresenter.getComic();
        manager = HttpDownManager.getInstance();
        mAdapter = new DownloadlistAdapter(this,R.layout.item_downloadlist);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutmanager);
        mRecyclerview.setAdapter(mAdapter);
        //mPresenter.initData();
        mPresenter.initDownInfoData();
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                DownInfo info = mAdapter.getItems(position);
                if(info.getState() == DownState.DOWN){
                    manager.pause(info);
                }else{
                    manager.startDown(info);
                }

            }
        });
    }

    @Override
    public void onStartDownload(int chapters) {

    }

    @Override
    public void onPausedDownload(int chapters) {

    }

    @Override
    public void onLoadMoreData(List<DownInfo> downInfos) {
        mAdapter.update(downInfos);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartAll() {

    }

    @Override
    public void onPauseAll() {

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
    public void fillData(List<DownInfo> data) {
        mAdapter.updateWithClear(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataFinish() {

    }
}
