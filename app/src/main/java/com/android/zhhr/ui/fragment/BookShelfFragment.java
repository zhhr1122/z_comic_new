package com.android.zhhr.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.BookShelfPresenter;
import com.android.zhhr.ui.adapter.BookShelfAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.DividerGridItemDecoration;
import com.android.zhhr.ui.view.IBookShelfView;
import com.android.zhhr.utils.IntentUtil;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

import butterknife.Bind;

/**
 * Created by 皓然 on 2017/8/7.
 */

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements IBookShelfView<List<Comic>>,BaseRecyclerAdapter.OnItemClickListener {
    @Bind(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    private BookShelfAdapter mAdapter;

    //private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Override
    protected void initPresenter() {
        mPresenter = new BookShelfPresenter(mActivity,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {


        GridLayoutManager layoutManager = new GridLayoutManager(mActivity,3);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new BookShelfAdapter(mActivity,R.layout.item_bookshelf);

       /* mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        ImageView foot = new ImageView(mActivity);
        foot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        foot.setImageResource(R.mipmap.no_more);
        foot.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mHeaderAndFooterWrapper.addFootView(foot);*/

        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mPresenter.loadData();
    }
    //切换到该fragment做的操作
   public void onHiddenChanged(boolean hidden) {
       super.onHiddenChanged(hidden);
       if (!hidden) {// 不在最前端界面显示
           mPresenter.loadData();
       }
   }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void getDataFinish() {

    }


    @Override
    public void showErrorView(String throwable) {
        ShowToast("重新加载");
    }

    @Override
    public void fillData(List<Comic> data) {
        if(data!=null&&data.size()!=0){
            mAdapter.updateWithClear(data);
        }else {
            ShowToast("未取到数据");
        }
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Comic comic = mAdapter.getItems(position);
        IntentUtil.ToComicDetail(mActivity,comic.getId()+"",comic.getTitle());
    }
}
