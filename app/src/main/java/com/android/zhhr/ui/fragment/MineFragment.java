package com.android.zhhr.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.MineTitle;
import com.android.zhhr.presenter.MinePresenter;
import com.android.zhhr.ui.adapter.MineAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;
import com.android.zhhr.ui.fragment.base.BaseFragment;
import com.android.zhhr.ui.view.IMineView;
import com.android.zhhr.utils.GlideImageLoader;
import com.android.zhhr.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhhr on 2018/3/13.
 */

public class MineFragment  extends BaseFragment<MinePresenter> implements IMineView<List<MineTitle>> {
    @Bind(R.id.rv_mine)
    RecyclerView mRecycle;
    @Bind(R.id.iv_cover)
    ImageView mCover;

    private MineAdapter mineAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new MinePresenter(getActivity(),this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mineAdapter = new MineAdapter(getActivity(),R.layout.item_mine);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.setAdapter(mineAdapter);
        GlideImageLoader.loadRoundImage(getActivity().getApplicationContext(),"https://avatars3.githubusercontent.com/u/21301998?s=460&v=4",mCover);
        mineAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                mPresenter.onItemClick(position);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<MineTitle> data) {
        mineAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {
        mineAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.rl_information)
    public void toGithub(){
        IntentUtil.toUrl(getActivity().getApplicationContext(),"https://github.com/zhhr1122/z_comic_new");
    }

    @Override
    public void SwitchSkin(boolean isNight) {
        mineAdapter.setNight(isNight);
        mActivity.switchModel();
    }
}
