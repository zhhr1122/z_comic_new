package com.android.zhhr.ui.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.HomePresenter;
import com.android.zhhr.ui.adapter.MainAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.DividerGridItemDecoration;
import com.android.zhhr.ui.custom.NoScrollStaggeredGridLayoutManager;
import com.android.zhhr.ui.custom.ZElasticRefreshScrollView;
import com.android.zhhr.ui.view.IHomeView;
import com.android.zhhr.utils.GlideImageLoader;
import com.android.zhhr.utils.IntentUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 皓然 on 2017/8/6.
 */

public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView<Comic>,BaseRecyclerAdapter.OnItemClickListener {

    @Bind(R.id.recycle_view)
    RecyclerView mRecycleView;

    @Bind(R.id.sv_comic)
    ZElasticRefreshScrollView mScrollView;
    @Bind(R.id.rl_error_view)
    RelativeLayout mErrorView;
    @Bind(R.id.iv_error)
    ImageView mReload;
    @Bind(R.id.iv_loading_top)
    ImageView mLoadingTop;
    @Bind(R.id.B_banner)
    Banner mBanner;
    private MainAdapter mAdapter;



    private int i=3;

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter(getActivity(),this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initAnimation();
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        NoScrollStaggeredGridLayoutManager layoutManager = new NoScrollStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(mActivity,R.layout.item_image);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mPresenter.LoadData();
        mScrollView.setRefreshListener(new ZElasticRefreshScrollView.RefreshListener() {
            @Override
            public void onActionDown() {
                mBanner.stopAutoPlay();
            }

            @Override
            public void onActionUp() {
                mBanner.startAutoPlay();
            }

            @Override
            public void onRefresh() {
                mPresenter.refreshData();
            }

            @Override
            public void onRefreshFinish() {
                mBanner.startAutoPlay();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData(i);
                i++;
            }
        });
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Comic comic = mPresenter.getmBanners().get(position);
                IntentUtil.ToComicDetail(mActivity,comic.getId()+"",comic.getTitle());
            }
        });

    }


    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mScrollView.scrollTo(0,0);
        }
    }

    //初始化动画
    private void initAnimation() {
        mLoadingTop.setImageResource(R.drawable.loading_top);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingTop.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void ShowToast(String t) {
        Toast.makeText(mActivity,t,Toast.LENGTH_LONG).show();;
    }

    @Override
    public void showErrorView(String throwable) {
        mScrollView.setRefreshing(false);
        mErrorView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
    }


    @Override
    public void getDataFinish() {
        mScrollView.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
        if(mErrorView.isShown()){
            mErrorView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefreshFinish() {
        mScrollView.setRefreshing(false);
        if(mErrorView.isShown()){
            mErrorView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fillBanner(List<Comic> data) {
        //设置图片集合
        mBanner.setImages(data);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
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
    public void appendMoreDataToView(List<Comic> data) {
        if(data!=null&&data.size()!=0){
            mAdapter.update(data);
        }else {
            ShowToast("未取到数据");
        }
    }

    @Override
    public void hasNoMoreData() {
        ShowToast("没有数据了");
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Comic comic = mAdapter.getItems(position);
        IntentUtil.ToComicDetail(mActivity,comic.getId()+"",comic.getTitle());
    }

    @OnClick(R.id.iv_error)
    public void ReloadData(View view){
        mErrorView.setVisibility(View.GONE);
        mPresenter.refreshData();
    }
}
