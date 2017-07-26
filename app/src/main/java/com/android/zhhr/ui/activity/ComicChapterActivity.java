package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.presenter.ComicChapterPresenter;
import com.android.zhhr.ui.adapter.ChapterViewpagerAdapter;
import com.android.zhhr.ui.custom.ComicReaderViewpager;
import com.android.zhhr.ui.custom.ReaderMenuLayout;
import com.android.zhhr.ui.view.IChapterView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 皓然 on 2017/7/20.
 */

public class ComicChapterActivity extends BaseActivity<ComicChapterPresenter> implements IChapterView<PreloadChapters>{

    @Bind(R.id.vp_chapters)
    ComicReaderViewpager mViewpager;
    @Bind(R.id.rl_top)
    RelativeLayout mTop;
    @Bind(R.id.rl_bottom)
    RelativeLayout mBottom;
    @Bind(R.id.rl_menu)
    ReaderMenuLayout menuLayout;
    @Bind(R.id.iv_back)
    ImageView mBack;
    @Bind(R.id.tv_title)
    TextView mTitle;

    ChapterViewpagerAdapter mAdapter;

    @Override
    protected void initPresenter() {
        Intent intent = getIntent();
        mPresenter = new ComicChapterPresenter(this,this);
        mPresenter.init(intent.getStringExtra(Constants.COMIC_ID),intent.getIntExtra(Constants.COMIC_CHAPERS,0),intent.getStringArrayListExtra(Constants.COMIC_CHAPER_TITLE));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chapter;
    }

    @Override
    protected void initView() {
        setNavigation();
        mAdapter = new ChapterViewpagerAdapter(this);
        mAdapter.setDirection(Constants.LEFT_TO_RIGHT);
        mViewpager.setOffscreenPageLimit(4);
        mAdapter.setListener(new ChapterViewpagerAdapter.OnceClickListener() {
            @Override
            public void onClick(View view, float x, float y) {
                mPresenter.clickScreen(x,y);
            }
        });
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.loadMoreData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                   /* case  ViewPager.SCROLL_STATE_DRAGGING:
                        mIsScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        mIsScrolled = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if(!mIsScrolled){
                            if(!isLoadingdata){
                                //mPresenter.loadMoreData(comic_id,comic_chapters,comic_postion,mAdapter.getDirection());
                                isLoadingdata = true;
                            }
                        }
                        mIsScrolled = true;
                        break;*/
                }

            }
        });
        mPresenter.loadData();
    }

    @Override
    public void setTitle(String name) {
        mTitle.setText(name);
    }


    /**
     * 设置setNavigation()
     */
    private void setNavigation() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorView(Throwable throwable) {

    }

    @Override
    public void fillData(PreloadChapters datas) {
        mPresenter.setmPreloadChapters(datas);
        mAdapter.setDatas(datas);
      /*  if(mAdapter.getDirection() == Constants.RIGHT_TO_LEFT){
            mViewpager.setCurrentItem(datas.size()-1,false);//关闭切换动画
        }*/
        mViewpager.setCurrentItem(datas.getPrelist().size(),false);
    }

    @Override
    public void showMenu() {
        if(!menuLayout.isShow()){
            menuLayout.setVisibility(View.VISIBLE);
        }else{
            menuLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void nextChapter(PreloadChapters data, int loadingPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(data.getPrelist().size()+loadingPosition,false);
        //showToast("完成了预加载");
    }

    @Override
    public void preChapter(PreloadChapters data, int loadingPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(data.getPrelist().size()+data.getNowlist().size()+loadingPosition-1,false);
        //showToast("完成了之前的预加载");
    }

    @Override
    public void SwitchModel(int a) {

    }

    @Override
    public void prePage() {
        int postion = mViewpager.getCurrentItem()-1;
        if(postion>=0){
            mViewpager.setCurrentItem(postion);
        }else{

        }
    }

    @Override
    public void nextPage() {
        int postion = mViewpager.getCurrentItem()+1;
        if(postion<mAdapter.getCount()){
            mViewpager.setCurrentItem(postion);
        }else{

        }
    }



    @OnClick(R.id.iv_back)
    public void finish(View view){
        this.finish();
    }
}
