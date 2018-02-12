package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.presenter.SelectDownloadPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.SelectDownloadAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.DividerGridItemDecoration;
import com.android.zhhr.ui.view.ISelectDownloadView;
import com.android.zhhr.utils.IntentUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 张皓然 on 2018/1/24.
 */

public class SelectDownloadActivity extends BaseActivity<SelectDownloadPresenter> implements ISelectDownloadView {
    @Bind(R.id.tv_chapters_num)
    TextView mChapterNum;
    @Bind(R.id.rv_index)
    RecyclerView mRecycleView;
    @Bind(R.id.iv_order)
    ImageView mOrder;

    @Bind(R.id.tv_select_all)
    TextView mSelected;

    @Bind(R.id.iv_select)
    ImageView mSelectedIcon;
    @Bind(R.id.tv_selected)
    TextView mSelectedNum;


    private SelectDownloadAdapter mAdapter;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SelectDownloadPresenter(this,this,intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_download;
    }

    @Override
    protected void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new SelectDownloadAdapter(this,R.layout.item_select_download);
        mAdapter.updateWithClear(mPresenter.getmChapters());
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if(!mAdapter.isOrder){
                    position = mPresenter.getmChapters().size()-position-1;
                }
                mPresenter.uptdateToSelected(position);
            }
        });
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(this,R.drawable.decorationlist_dark));
        mChapterNum.setText("共"+mPresenter.getmChapters().size()+"话");
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(Object data) {

    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void ShowToast(String t) {

    }
    @Override
    @OnClick(R.id.rl_download)
    public void startDownload() {
       mPresenter.startDownload();
    }

    @Override
    public void updateDownloadList(HashMap map) {
        mAdapter.setMap(map);
        mAdapter.notifyDataSetChanged();
        mSelectedNum.setText("已选择"+mPresenter.getSelectedNum()+"话");
    }

    @Override
    public void addAll() {
        mSelected.setText("取消全选");
        mSelectedIcon.setImageResource(R.mipmap.btn_cancel_select);
    }

    @Override
    public void removeAll() {
        mSelected.setText("全选");
        mSelectedIcon.setImageResource(R.mipmap.btn_select);
    }

    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        this.finish();
    }

    @OnClick({R.id.iv_order })
    public void OrderList(ImageView Order) {
        mAdapter.setOrder(!mAdapter.isOrder());
        if(!mAdapter.isOrder()){
            mOrder.setImageResource(R.mipmap.daoxu);
        }else{
            mOrder.setImageResource(R.mipmap.zhengxu);
        }
    }

    @OnClick(R.id.rl_select)
    public void SelectAll(View view){
        mPresenter.SelectOrMoveAll();
    }
}
