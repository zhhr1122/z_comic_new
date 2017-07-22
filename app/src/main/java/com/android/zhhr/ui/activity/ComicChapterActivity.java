package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Subject;
import com.android.zhhr.presenter.ComicChapterPresenter;
import com.android.zhhr.ui.adapter.ChapterViewpagerAdapter;
import com.android.zhhr.ui.custom.ComicReaderViewpager;
import com.android.zhhr.ui.custom.ReaderMenuLayout;
import com.android.zhhr.ui.view.IChapterView;
import com.android.zhhr.utils.DisplayUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 皓然 on 2017/7/20.
 */

public class ComicChapterActivity extends BaseActivity<ComicChapterPresenter> implements IChapterView<Subject>{
    String comic_id;
    String comic_chapters;
    String comic_chapter_title;
    int comic_size = 0;

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
        mPresenter = new ComicChapterPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chapter;
    }

    @Override
    protected void initView() {
        setNavigation();
        Intent intent = getIntent();
        comic_id = intent.getStringExtra(Constants.COMIC_ID);
        comic_chapters = intent.getStringExtra(Constants.COMIC_CHAPERS);
        comic_chapter_title = intent.getStringExtra(Constants.COMIC_CHAPER_TITLE);
        mAdapter = new ChapterViewpagerAdapter(this);
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
               mPresenter.setTitle(comic_chapter_title,comic_size,position,mAdapter.getDirection());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPresenter.loadData(comic_id,comic_chapters);
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
    public void fillData(Subject data) {
        mAdapter.setDatas(data.getComiclist());
        mAdapter.setDirection(Constants.RIGHT_TO_LEFT);
        if(mAdapter.getDirection() == Constants.RIGHT_TO_LEFT){
            mViewpager.setCurrentItem(data.getComiclist().size()-1,false);//关闭切换动画
        }
        comic_size = data.getComiclist().size();
        setTitle(comic_chapter_title+"-1/"+comic_size);
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
    public void nextChapter() {

    }

    @Override
    public void preChapter() {

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
            preChapter();
        }
    }

    @Override
    public void nextPage() {
        int postion = mViewpager.getCurrentItem()+1;
        if(postion<mAdapter.getCount()){
            mViewpager.setCurrentItem(postion);
        }else{
            nextChapter();
        }
    }



    @OnClick(R.id.iv_back)
    public void finish(View view){
        this.finish();
    }
}
