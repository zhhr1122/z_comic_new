package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.presenter.ComicChapterPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.ChapterViewpagerAdapter;
import com.android.zhhr.ui.custom.ComicReaderViewpager;
import com.android.zhhr.ui.custom.ReaderMenuLayout;
import com.android.zhhr.ui.custom.SwitchRelativeLayout;
import com.android.zhhr.ui.custom.ZBubbleSeekBar;
import com.android.zhhr.ui.view.IChapterView;
import com.android.zhhr.utils.IntentUtil;
import com.xw.repo.BubbleSeekBar;

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
    @Bind(R.id.sb_seekbar)
    ZBubbleSeekBar mSeekbar;

    ChapterViewpagerAdapter mAdapter;

    @Bind(R.id.iv_loading)
    ImageView mLoading;
    @Bind(R.id.rl_loading)
    RelativeLayout mRLloading;
    @Bind(R.id.tv_loading)
    TextView mLoadingText;
    @Bind(R.id.iv_error)
    ImageView mReload;
    @Bind(R.id.tv_loading_title)
    TextView mLoadingTitle;
    @Bind(R.id.rl_switch_model)
    SwitchRelativeLayout mSwitchModel;
    @Bind(R.id.iv_normal_model)
    Button mNormal;
    @Bind(R.id.iv_j_comic_model)
    Button mJcomic;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new ComicChapterPresenter(this,this);
        mPresenter.init(intent.getLongExtra(Constants.COMIC_ID,0),intent.getIntExtra(Constants.COMIC_CHAPERS,0),intent.getStringArrayListExtra(Constants.COMIC_CHAPER_TITLE),intent.getIntExtra(Constants.COMIC_READ_TYPE,Constants.LEFT_TO_RIGHT));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chapter;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mRLloading.setVisibility(View.VISIBLE);
        mLoadingText.setText("正在加载,请稍后...");
        initPresenter(intent);
        initView();
    }

    @Override
    protected void initView() {
        setNavigation();
        mLoading.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();

        mAdapter = new ChapterViewpagerAdapter(this);
        mAdapter.setDirection(mPresenter.getmDirect());
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
                if(menuLayout.isShow()){
                    menuLayout.setVisibility(View.GONE);
                }
                if(mSwitchModel.isShow()){
                    mSwitchModel.setVisibility(View.GONE);
                }
                if(mAdapter.getDirection() == Constants.LEFT_TO_RIGHT){
                    mSeekbar.setProgress(position-mPresenter.getmPreloadChapters().getPrelist().size()+1);
                }else{
                    mSeekbar.setProgress(position-mPresenter.getmPreloadChapters().getNextlist().size()+1);
                }

                mPresenter.loadMoreData(position,mAdapter.getDirection());
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
        mSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                if(mAdapter.getDirection() == Constants.RIGHT_TO_LEFT){
                    mViewpager.setCurrentItem(progress+mPresenter.getmPreloadChapters().getNextlist().size()-1);
                }else{
                    mViewpager.setCurrentItem(progress+mPresenter.getmPreloadChapters().getPrelist().size()-1);
                }
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
        //设置加载时候的标题
        mLoadingTitle.setText(mPresenter.getComic_chapter_title().get(mPresenter.getComic_chapters()));
        if(Constants.isAD){
            mPresenter.loadDataforAd();
        }else{
            mPresenter.loadData();
        }
        initReaderModule(mPresenter.getmDirect());
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

    /**
     * 初始化阅读模式做一些处理
     */
    private void initReaderModule(int mDirect) {
        if(mDirect==Constants.LEFT_TO_RIGHT){
            mSeekbar.setSeekBarColor(true);
            mNormal.setBackgroundResource(R.drawable.normal_model_press);
        }else{
            mSeekbar.setSeekBarColor(false);
            mJcomic.setBackgroundResource(R.drawable.j_comic_model_press);
        }
        if(mDirect == Constants.LEFT_TO_RIGHT){
            mNormal.setBackgroundResource(R.drawable.normal_model_press);
            mJcomic.setBackgroundResource(R.drawable.btn_switchmodel_j_comic);
        }else{
            mNormal.setBackgroundResource(R.drawable.btn_switchmodel_normal);
            mJcomic.setBackgroundResource(R.drawable.j_comic_model_press);
        }
    }

    @Override
    public void fillData(PreloadChapters datas) {
        mPresenter.setmPreloadChapters(datas);
        mAdapter.setDatas(datas);
        mSeekbar.setmMax(datas.getNowlist().size());
        if(mAdapter.getDirection() == Constants.RIGHT_TO_LEFT){
            mViewpager.setCurrentItem(datas.getNextlist().size()+datas.getNowlist().size()-1,false);//关闭切换动画
        }else{
            mViewpager.setCurrentItem(datas.getPrelist().size(),false);
        }
        mPresenter.updateComicCurrentChapter();
    }

    @Override
    public void setTitle(String name) {
        mTitle.setText(name);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }
    //数据加载成功
    @Override
    public void getDataFinish() {
        mRLloading.setVisibility(View.GONE);
    }

    //数据加载失败
    @Override
    public void showErrorView(String error) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        mLoadingText.setText(error);
    }


    @Override
    public void showMenu() {
        if(!menuLayout.isShow()){
            menuLayout.setVisibility(View.VISIBLE);
        }else{
            menuLayout.setVisibility(View.GONE);
        }
        if(mSwitchModel.isShow()){
            mSwitchModel.setVisibility(View.GONE);
        }
    }


    @Override
    public void nextChapter(PreloadChapters data, int mPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(mPosition,false);
        mSeekbar.setmMax(data.getNowlist().size());
        //为什么第一页的时候需要单独再设置Progress?因为adapter的LIST并未发生改变，所以调用刷新方法后没有调用onPageSelected方法，故没有设置Progress
        if(mPresenter.getComic_chapters()==1){
            if(mAdapter.getDirection()==Constants.LEFT_TO_RIGHT){
                mSeekbar.setProgress(1);
            }else{
                mSeekbar.setProgress(data.getNowlist().size());
            }
        }
        mPresenter.updateComicCurrentChapter();
    }

    @Override
    public void preChapter(PreloadChapters data, int mPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(mPosition,false);
        mSeekbar.setmMax(data.getNowlist().size());
        //为什么第一页的时候需要单独再设置Progress?因为adapter的LIST并未发生改变，所以调用刷新方法后没有调用onPageSelected方法，故没有设置Progress
        if(mPresenter.getComic_chapters()==0){
            if(mAdapter.getDirection()==Constants.LEFT_TO_RIGHT){
                mSeekbar.setProgress(data.getNowlist().size());
            }else{
                mSeekbar.setProgress(1);
            }
        }
        mPresenter.updateComicCurrentChapter();
    }

    @Override
    public void SwitchModel(int mDirect) {
        if(mAdapter.getDirection()!=mDirect){
            int nowposition = mViewpager.getCurrentItem();
            //必须重新new 一个adapter 否则在接近页面的时候，会有缓存，图片切换会有问题
            mAdapter = new ChapterViewpagerAdapter(this,mPresenter.getmPreloadChapters(),mDirect);
            mAdapter.setListener(new ChapterViewpagerAdapter.OnceClickListener() {
                @Override
                public void onClick(View view, float x, float y) {
                    mPresenter.clickScreen(x,y);
                }
            });
            mViewpager.setAdapter(mAdapter);
            mPresenter.setmDirect(mDirect);
            mSwitchModel.setVisibility(View.GONE,mDirect);
            initReaderModule(mDirect);
            mViewpager.setCurrentItem(mPresenter.getmPreloadChapters().getDataSize()-nowposition-1,false);
        }
    }

    @Override
    public void prePage() {
        int postion = mViewpager.getCurrentItem()-1;
        if(postion>=0){
            mViewpager.setCurrentItem(postion);
        }else{
            showToast("没有了");
        }
    }

    @Override
    public void nextPage() {
        int postion = mViewpager.getCurrentItem()+1;
        if(postion<mAdapter.getCount()){
            mViewpager.setCurrentItem(postion);
        }else{
            showToast("没有了");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_back,R.id.iv_back_color})
    public void finish(View view){
        this.finish();
    }

    @OnClick(R.id.iv_error)
    public void reload(View view){
        mPresenter.loadData();
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("正在重新加载，请稍后");
    }

    @OnClick(R.id.iv_index)
    public void toIndex(View view){
        IntentUtil.ToIndex(ComicChapterActivity.this,mPresenter.getComic_id(),mPresenter.getComic_chapter_title(),getIntent().getStringExtra(Constants.COMIC_TITLE),mPresenter.getmDirect());
    }

    @OnClick(R.id.iv_settiong)
    public void toSwitch(View view){
        menuLayout.setVisibility(View.GONE);
        if(!mSwitchModel.isShow()){
            mSwitchModel.setVisibility(View.VISIBLE);
        }else {
            mSwitchModel.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.iv_normal_model)
    public void SwitchToLeftToRight(View view){
        mPresenter.setReaderModuel(Constants.LEFT_TO_RIGHT);
    }

    @OnClick(R.id.iv_j_comic_model)
    public void SwitchToRightToLeft(View view){
        mPresenter.setReaderModuel(Constants.RIGHT_TO_LEFT);
    }
}
