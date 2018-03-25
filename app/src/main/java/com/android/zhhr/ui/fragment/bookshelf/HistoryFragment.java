package com.android.zhhr.ui.fragment.bookshelf;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.HomeTitle;
import com.android.zhhr.presenter.HistoryPresenter;
import com.android.zhhr.ui.adapter.HistoryAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.ElasticScrollView;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
import com.android.zhhr.ui.fragment.base.BaseBookShelfFragment;
import com.android.zhhr.ui.fragment.base.BaseFragment;
import com.android.zhhr.ui.view.ICollectionView;
import com.android.zhhr.utils.DisplayUtil;
import com.android.zhhr.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by 皓然 on 2017/8/7.
 */

public class HistoryFragment extends BaseBookShelfFragment<HistoryPresenter> implements ICollectionView<List<Comic>>,BaseRecyclerAdapter.OnItemClickListener {
    @Bind(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    private HistoryAdapter mAdapter;
    @Bind(R.id.ev_scrollview)
    ElasticScrollView mScrollView;

    @Bind(R.id.rl_empty_view)
    RelativeLayout mEmptyView;


    @Override
    protected void initPresenter() {
        mPresenter = new HistoryPresenter(mActivity,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        final NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity,1);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new HistoryAdapter(mActivity,R.layout.item_history,R.layout.item_history_title,R.layout.item_loading);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mScrollView.setListener(new ElasticScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadMoreData();
            }
        });
       /* mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                    if(lastVisiblePosition >= layoutManager.getItemCount() - 1){
                        mPresenter.loadData();
                    }
                }
            }
        });*/

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
        mEmptyView.setVisibility(View.GONE);
        mAdapter.updateWithClear(data);
        //mScrollView.setInnerHeight();
    }

    @Override
    public void showEmptyView() {
        mAdapter.updateWithClear(new ArrayList<Comic>());
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if(mAdapter.getItems(position) instanceof HomeTitle){

        }else if(!mAdapter.isEditing()){
            Comic comic = mAdapter.getItems(position);
            IntentUtil.ToComicChapter(mActivity,comic.getCurrentChapter(),comic);
        }else{
            mPresenter.uptdateToSelected(position);
        }
    }

    public void OnEditList(boolean isEdit){
        if(mAdapter.isEditing()!=isEdit){
            mPresenter.clearSelect();
            mAdapter.setEditing(isEdit);
        }
    }

    @Override
    public void OnDelete() {
        mPresenter.ShowDeteleDialog();
    }

    @Override
    public void OnSelect() {
        mPresenter.SelectOrMoveAll();
    }

    @Override
    public void updateList(HashMap map) {
        mAdapter.setmMap(map);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateListItem(HashMap map, int position) {
        mAdapter.setmMap(map);
        mAdapter.notifyItemChanged(position,"isNotNull");
    }

    @Override
    public void addAll() {
        mainActivity.getmEditBottom().addAll();
    }

    @Override
    public void removeAll() {
        mainActivity.getmEditBottom().removeAll();
    }

    @Override
    public void quitEdit() {
            mainActivity.quitEdit();
    }
}
