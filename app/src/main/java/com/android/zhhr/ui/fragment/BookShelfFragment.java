package com.android.zhhr.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.presenter.BookShelfPresenter;
import com.android.zhhr.ui.activity.MainActivity;
import com.android.zhhr.ui.adapter.BookShelfFragmentAdapter;
import com.android.zhhr.ui.custom.FloatEditLayout;
import com.android.zhhr.ui.fragment.base.BaseBookShelfFragment;
import com.android.zhhr.ui.fragment.base.BaseFragment;
import com.android.zhhr.ui.fragment.bookshelf.CollectionFragment;
import com.android.zhhr.ui.fragment.bookshelf.DownloadFragment;
import com.android.zhhr.ui.fragment.bookshelf.HistoryFragment;
import com.android.zhhr.ui.view.IBookShelfView;
import com.android.zhhr.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 皓然 on 2017/8/7.
 */

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements IBookShelfView {
    @Bind(R.id.vp_bookshelf)
    ViewPager mViewpager;

    BookShelfFragmentAdapter mAdapter;
    protected FragmentManager fragmentManager;
    protected List<BaseBookShelfFragment> fragments;
    @Bind(R.id.tv_download)
    TextView mDownload;
    @Bind(R.id.tv_history)
    TextView mHistory;
    @Bind(R.id.tv_collect)
    TextView mCollect;
    @Bind(R.id.iv_bottom_collect)
    ImageView mBottomCollect;
    @Bind(R.id.iv_bottom_history)
    ImageView mBottomHistory;
    @Bind(R.id.iv_bottom_download)
    ImageView mBottomDownload;
    @Bind(R.id.iv_edit)
    ImageView mEdit;

    private boolean isEditing;

    private MainActivity mainActivity;

    private CollectionFragment collectionFragment;
    private HistoryFragment historyFragment;
    private DownloadFragment downloadFragment;



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
        fragments = new ArrayList<>();
        mainActivity = (MainActivity) this.getActivity();

        collectionFragment = new CollectionFragment();
        historyFragment = new HistoryFragment();
        downloadFragment = new DownloadFragment();


        fragments.add(collectionFragment);
        fragments.add(historyFragment);
        fragments.add(downloadFragment);

        fragmentManager = getActivity().getSupportFragmentManager();
        mAdapter = new BookShelfFragmentAdapter(fragmentManager,fragments);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(isEditing){
                    mEdit.setImageResource(R.mipmap.edit);
                    mainActivity.setEditBottomVisible(View.GONE);
                    showEditModel(fragments.get(mViewpager.getCurrentItem()),false);
                    isEditing = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        ToCollect();
                        break;
                    case 1:
                        ToHistory();
                        break;
                    case 2:
                        ToDownload();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }
    @OnClick(R.id.rl_collect)
    public void ToCollect(){
        ResetTitle();
        mCollect.setTextColor(Color.parseColor("#333333"));
        mViewpager.setCurrentItem(0);
        mBottomCollect.setVisibility(View.VISIBLE);

    }
    @OnClick(R.id.rl_history)
    public void ToHistory(){
        ResetTitle();
        mHistory.setTextColor(Color.parseColor("#333333"));
        mViewpager.setCurrentItem(1);
        mBottomHistory.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.rl_download)
    public void ToDownload(){
        ResetTitle();
        mDownload.setTextColor(Color.parseColor("#333333"));
        mViewpager.setCurrentItem(2);
        mBottomDownload.setVisibility(View.VISIBLE);
    }

    public void ResetTitle(){
        mDownload.setTextColor(Color.parseColor("#999999"));
        mCollect.setTextColor(Color.parseColor("#999999"));
        mHistory.setTextColor(Color.parseColor("#999999"));
        mBottomCollect.setVisibility(View.GONE);
        mBottomDownload.setVisibility(View.GONE);
        mBottomHistory.setVisibility(View.GONE);
    }
    @OnClick(R.id.iv_edit)
    public void toEdit(){
        if(!isEditing){
            mEdit.setImageResource(R.mipmap.closed);
            mainActivity.setEditBottomVisible(View.VISIBLE);
            showEditModel(fragments.get(mViewpager.getCurrentItem()),true);
        }else{
            mEdit.setImageResource(R.mipmap.edit);
            mainActivity.setEditBottomVisible(View.GONE);
            showEditModel(fragments.get(mViewpager.getCurrentItem()),false);
        }
        isEditing = !isEditing;
    }

    private void showEditModel(BaseBookShelfFragment fragment, boolean isEdit) {
        fragment.OnEditList(isEdit);
    }
}
