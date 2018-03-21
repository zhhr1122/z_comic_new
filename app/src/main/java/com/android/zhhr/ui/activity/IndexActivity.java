package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.presenter.IndexPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.IndexAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.view.IIndexView;
import com.android.zhhr.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 皓然 on 2017/7/28.
 */

public class IndexActivity extends BaseActivity<IndexPresenter> implements IIndexView,BaseRecyclerAdapter.OnItemClickListener{
    private List<String> comic_chapter_titles;
    private IndexAdapter mAdapter;
    @Bind(R.id.rv_index)
    RecyclerView mRecycleView;
    @Bind(R.id.iv_order)
    ImageView mOrder;
    @Bind(R.id.tv_loading_title)
    TextView mTitle;
    @Bind(R.id.tv_downloaded)
    TextView mDownload;
    @Bind(R.id.tv_chapters_num)
    TextView mChapterNum;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new IndexPresenter(this,this,intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_index;
    }

    @Override
    protected void initView() {
        mAdapter = new IndexAdapter(this,R.layout.item_chapter);
        mRecycleView.setAdapter(mAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter.updateWithClear(mPresenter.getmComic().getChapters());
        mAdapter.setOnItemClickListener(this);
        mTitle.setText(mPresenter.getmComic().getTitle());
        mChapterNum.setText("共"+mPresenter.getmComic().getChapters().size()+"话");
        mDownload.setVisibility(View.VISIBLE);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
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

    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        this.finish();
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if(!mAdapter.isOrder()){
            position = mPresenter.getmComic().getChapters().size()-position-1;
        }
        IntentUtil.ToComicChapter(IndexActivity.this,position,mPresenter.getmComic());
    }
}
