package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.presenter.IndexPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.DetailAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
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
    private DetailAdapter mAdapter;
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

    private Intent intent;
    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new IndexPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_index;
    }

    @Override
    protected void initView() {
        intent = getIntent();
        mAdapter = new DetailAdapter(this,R.layout.item_chapter);
        mRecycleView.setAdapter(mAdapter);
        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(this,1);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter.updateWithClear(intent.getStringArrayListExtra(Constants.COMIC_CHAPER_TITLE));
        mAdapter.setOnItemClickListener(this);
        mTitle.setText(intent.getStringExtra(Constants.COMIC_TITLE));
        mChapterNum.setText("共"+intent.getStringArrayListExtra(Constants.COMIC_CHAPER_TITLE).size()+"话");
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
            position = intent.getStringArrayListExtra(Constants.COMIC_CHAPER_TITLE).size()-position-1;
        }
        IntentUtil.ToComicChapter(IndexActivity.this,position,intent.getLongExtra(Constants.COMIC_ID,0),intent.getStringExtra(Constants.COMIC_TITLE),intent.getStringArrayListExtra(Constants.COMIC_CHAPER_TITLE),intent.getIntExtra(Constants.COMIC_READ_TYPE,Constants.LEFT_TO_RIGHT));
    }
}
