package com.android.zhhr.ui.fragment.bookshelf;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.CollectionPresenter;
import com.android.zhhr.ui.adapter.BookShelfAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.DividerGridItemDecoration;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
import com.android.zhhr.ui.fragment.base.BaseFragment;
import com.android.zhhr.ui.view.ICollectionView;
import com.android.zhhr.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by 皓然 on 2017/8/7.
 */

public class CollectionFragment extends BaseFragment<CollectionPresenter> implements ICollectionView<List<Comic>>,BaseRecyclerAdapter.OnItemClickListener {
    @Bind(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    private BookShelfAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new CollectionPresenter(mActivity,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collection;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity,3);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new BookShelfAdapter(mActivity,R.layout.item_collection);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }
    //切换到该fragment做的操作
   public void onHiddenChanged(boolean hidden) {
       super.onHiddenChanged(hidden);
       if (!hidden) {// 不在最前端界面显示
           mPresenter.loadData();
       }
   }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
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
            //ShowToast("未取到数据");
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
