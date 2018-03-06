package com.android.zhhr.ui.fragment.bookshelf;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.presenter.DownloadPresenter;
import com.android.zhhr.ui.adapter.CollectAdapter;
import com.android.zhhr.ui.adapter.DownloadAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.DividerGridItemDecoration;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
import com.android.zhhr.ui.fragment.base.BaseBookShelfFragment;
import com.android.zhhr.ui.fragment.base.BaseFragment;
import com.android.zhhr.ui.view.ICollectionView;
import com.android.zhhr.utils.IntentUtil;
import com.android.zhhr.utils.LogUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by 皓然 on 2017/8/7.
 */

public class DownloadFragment extends BaseBookShelfFragment<DownloadPresenter> implements ICollectionView<List<Comic>>,BaseRecyclerAdapter.OnItemClickListener {
    @Bind(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    private DownloadAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new DownloadPresenter(mActivity,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_download;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity,1);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new DownloadAdapter(mActivity,R.layout.item_download);
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
        LogUtil.d("fragment onresume");
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
        mAdapter.updateWithClear(data);

    }

    @Override
    public void showEmptyView() {
        mAdapter.updateWithClear(null);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if(!mAdapter.isEditing()){
            Comic comic = mAdapter.getItems(position);
            IntentUtil.ToDownloadListActivity(mActivity,new HashMap<Integer, Integer>(),comic);
        }else{
            mPresenter.uptdateToSelected(position);
        }
    }

    @Override
    public void addAll() {
        mainActivity.getmEditBottom().addAll();
    }

    @Override
    public void removeAll() {
        mainActivity.getmEditBottom().removeAll();
    }

    public void OnEditList(boolean isEdit){
        if(mAdapter!=null&&mAdapter.isEditing()!=isEdit){
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
    public void quitEdit() {
        mainActivity.quitEdit();
    }
}
