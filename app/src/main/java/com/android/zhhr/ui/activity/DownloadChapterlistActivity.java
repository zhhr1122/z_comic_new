package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.db.DBChapters;
import com.android.zhhr.presenter.DownloadChapterlistPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.DownloadChapterlistAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.FloatEditLayout;
import com.android.zhhr.ui.view.IDownloadlistView;
import com.android.zhhr.utils.IntentUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/2/12.
 */

public class DownloadChapterlistActivity extends BaseActivity<DownloadChapterlistPresenter> implements IDownloadlistView<List<DBChapters>>{
    @Bind(R.id.tv_title)
    TextView mTitle;
    @Bind(R.id.rv_downloadlist)
    RecyclerView mRecyclerview;
    @Bind(R.id.rl_loading)
    RelativeLayout mRLloading;
    @Bind(R.id.tv_loading)
    TextView mLoadingText;
    @Bind(R.id.iv_error)
    ImageView mReload;
    @Bind(R.id.iv_loading)
    ImageView mLoading;
    @Bind(R.id.tv_download)
    TextView mDownloadText;
    @Bind(R.id.iv_download)
    ImageView mDownloadImage;

    @Bind(R.id.iv_edit)
    ImageView mEdit;
    @Bind(R.id.rl_edit_bottom)
    FloatEditLayout mEditLayout;


    private DownloadChapterlistAdapter mAdapter;
    private int recycleState = 0;


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
        //动画初始化
        mLoading.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();
        //
        mAdapter = new DownloadChapterlistAdapter(this,R.layout.item_downloadlist);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutmanager);
        mRecyclerview.setAdapter(mAdapter);
        //mPresenter.initDbData();
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //实时监测滑动状态
                recycleState = newState;
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                DBChapters info = mAdapter.getItems(position);
                mPresenter.onItemClick(info,position);
            }
        });
        mEditLayout.setListener(new FloatEditLayout.onClickListener() {
            @Override
            public void OnClickSelect() {
                mPresenter.SelectOrMoveAll();
            }

            @Override
            public void OnDelete() {
                mPresenter.ShowDeteleDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPresenter(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.pauseAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.updateComic();
    }

    @Override
    public void onLoadMoreData(List<DBChapters> downInfos) {

    }

    @Override
    public void updateView(int postion) {
        //如果是静止状态，则刷新局部，滑动则全局刷新
        if(recycleState == 0){
            //刷新局部，不然会影响点击事件
            mAdapter.notifyItemChanged(postion,"isNotEmpty");
        }else{
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDownloadFinished() {
        mDownloadText.setText("下载完成");
        mDownloadImage.setVisibility(View.GONE);
        mPresenter.isAllDownload = DownloadChapterlistPresenter.FINISH;
    }


    @OnClick(R.id.rl_all)
    @Override
    public void onPauseOrStartAll() {
        switch (mPresenter.isAllDownload){
            case DownloadChapterlistPresenter.DOWNLOADING:
                mPresenter.stopAll();
                mDownloadText.setText("全部开始");
                mDownloadImage.setImageResource(R.mipmap.pasue);
                mPresenter.isAllDownload = DownloadChapterlistPresenter.STOP_DOWNLOAD;
                break;
            case DownloadChapterlistPresenter.STOP_DOWNLOAD:
                mPresenter.ReStartAll();
                mDownloadText.setText("全部停止");
                mDownloadImage.setImageResource(R.mipmap.pasue_select);
                mPresenter.isAllDownload = DownloadChapterlistPresenter.DOWNLOADING;
                break;
            default:
                break;
        }
    }

    @Override
    public void updateList(HashMap map) {
        mAdapter.setmMap(map);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateListItem(HashMap map, int position) {
        mAdapter.setmMap(map);
        mAdapter.notifyItemChanged(position,"isNotEmpty");
    }

    @Override
    public void addAll() {
        mEditLayout.addAll();
    }

    @Override
    public void removeAll() {
        mEditLayout.removeAll();
    }

    @Override
    public void quitEdit() {
        mEdit.setImageResource(R.mipmap.edit);
        mEditLayout.setVisibility(View.GONE);
        mPresenter.setEditing(false);
        mAdapter.setEditing(mPresenter.isEditing());
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

    @Override
    public void showErrorView(String throwable) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        mLoadingText.setText(throwable);
    }

    @Override
    public void fillData(List<DBChapters> mLists) {
        if(mLists!=null&&mLists.size()!=0){
            mAdapter.updateWithClear(mLists);
            mAdapter.notifyDataSetChanged();
            //开始自动下载
            mPresenter.startAll();
        }
        mRLloading.setVisibility(View.GONE);

    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.iv_error)
    public void reload(View view){
        mPresenter.initData();
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("正在重新加载，请稍后");
    }
    @OnClick(R.id.rl_order)
    public void ToSelectDownload(){
        IntentUtil.ToSelectDownload(this,mPresenter.getmComic());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.getResultComic(resultCode,data);
    }

    @OnClick(R.id.iv_edit)
    public void toEdit(){
        if(!mPresenter.isEditing()){
            mEdit.setImageResource(R.mipmap.closed);
            mEditLayout.setVisibility(View.VISIBLE);
        }else{
            mEdit.setImageResource(R.mipmap.edit);
            mEditLayout.setVisibility(View.GONE);
        }
        mPresenter.setEditing(!mPresenter.isEditing());
        mAdapter.setEditing(mPresenter.isEditing());
        mAdapter.notifyDataSetChanged();
    }
}
