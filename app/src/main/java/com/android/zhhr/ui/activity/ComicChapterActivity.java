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
import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.data.entity.Subject;
import com.android.zhhr.presenter.ComicChapterPresenter;
import com.android.zhhr.ui.adapter.ChapterViewpagerAdapter;
import com.android.zhhr.ui.custom.ComicReaderViewpager;
import com.android.zhhr.ui.custom.ReaderMenuLayout;
import com.android.zhhr.ui.view.IChapterView;
import com.android.zhhr.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 皓然 on 2017/7/20.
 */

public class ComicChapterActivity extends BaseActivity<ComicChapterPresenter> implements IChapterView<Chapters>{
    String comic_id;
    int comic_chapters;
    ArrayList<String> comic_chapter_title;
    boolean mIsScrolled = false;
    int comic_postion = 0;
    int comic_size = 0;
    boolean isLoadingdata = false;
    Chapters mChapters;

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
        comic_chapters = intent.getIntExtra(Constants.COMIC_CHAPERS,0);
        comic_chapter_title = intent.getStringArrayListExtra(Constants.COMIC_CHAPER_TITLE);
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
                String chapter_title = null;
                int now_postion =0;
                if(position<mChapters.getPrelist().size()){
                    chapter_title = comic_chapter_title.get(comic_chapters-1);
                    comic_size = mChapters.getPrelist().size();
                    now_postion =position;
                }else if(position>=mChapters.getPrelist().size()+mChapters.getNowlist().size()){
                    comic_size = mChapters.getNextlist().size();
                    chapter_title = comic_chapter_title.get(comic_chapters+1);
                    now_postion = position - mChapters.getPrelist().size() - mChapters.getNowlist().size();
                }else {
                    comic_size = mChapters.getNowlist().size();
                    chapter_title = comic_chapter_title.get(comic_chapters);
                    now_postion = position - mChapters.getPrelist().size();
                }
                comic_postion = position;
                mPresenter.setTitle(chapter_title,comic_size,now_postion,mAdapter.getDirection());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case  ViewPager.SCROLL_STATE_DRAGGING:
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
                        break;
                }

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
    public void fillData(Chapters datas) {
        mChapters = datas;
        mAdapter.setDatas(datas);
      /*  if(mAdapter.getDirection() == Constants.RIGHT_TO_LEFT){
            mViewpager.setCurrentItem(datas.size()-1,false);//关闭切换动画
        }*/
        comic_size = datas.getNextlist().size();
        mViewpager.setCurrentItem(datas.getPrelist().size(),false);
        setTitle(comic_chapter_title.get(comic_chapters)+"-1/"+comic_size);
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
    public void nextChapter(Chapters data) {
        /*mAdapter.setNextDatas(data.getComiclist());
        comic_size = data.getComiclist().size();
        comic_chapters++;
        isLoadingdata = false;*/
    }

    @Override
    public void preChapter(Chapters data) {
       /* mAdapter.setPreDatas(data.getComiclist());
        comic_size = data.getComiclist().size();
        mViewpager.setCurrentItem(comic_size,false);
        comic_chapters--;
        isLoadingdata = false;*/
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
