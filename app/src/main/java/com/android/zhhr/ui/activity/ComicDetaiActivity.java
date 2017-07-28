package com.android.zhhr.ui.activity;

import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.presenter.ComicDetailPresenter;
import com.android.zhhr.ui.adapter.DetailAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.DetailScrollView;
import com.android.zhhr.ui.custom.NoScrollStaggeredGridLayoutManager;
import com.android.zhhr.ui.view.IDetailView;
import com.android.zhhr.utils.DisplayUtil;
import com.android.zhhr.utils.IntentUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by 皓然 on 2017/7/12.
 */

public class ComicDetaiActivity extends BaseActivity<ComicDetailPresenter> implements IDetailView<Comic>,BaseRecyclerAdapter.OnItemClickListener,View.OnClickListener {
    private String comic_id;
    @Bind(R.id.iv_image)
    ImageView mHeaderView;

    @Bind(R.id.recycle_view)
    RecyclerView mRecycleView;

    @Bind(R.id.sv_comic)
    DetailScrollView mScrollView;
    @Bind(R.id.tv_title)
    TextView mTitle;
    @Bind(R.id.ll_text)
    LinearLayout mText;
    @Bind(R.id.iv_image_bg)
    ImageView mHeaderViewBg;
    @Bind(R.id.tv_author_tag)
    TextView mAuthorTag;
    @Bind(R.id.tv_collects)
    TextView mCollects;
    @Bind(R.id.tv_describe)
    TextView mDescribe;
    @Bind(R.id.tv_popularity)
    TextView mPopularity;
    @Bind(R.id.tv_status)
    TextView mStatus;
    @Bind(R.id.tv_update)
    TextView mUpdate;
    @Bind(R.id.tv_point)
    TextView mPoint;
    @Bind(R.id.ll_detail)
    RelativeLayout mDetail;
    @Bind(R.id.tv_tab)
    TextView mTab;
    @Bind(R.id.iv_order)
    ImageView mOrder;
    @Bind(R.id.tv_actionbar_title)
    TextView mActionBarTitle;
    @Bind(R.id.iv_back)
    ImageView mBack;
    @Bind(R.id.iv_oreder2)
    ImageView mOrder2;
    @Bind(R.id.btn_read)
    Button mRead;

    @Bind(R.id.iv_loading)
    ImageView mLoading;
    @Bind(R.id.rl_loading)
    RelativeLayout mRLloading;
    @Bind(R.id.tv_loading)
    TextView mLoadingText;
    @Bind(R.id.iv_error)
    ImageView mReload;


    DetailAdapter mAdapter;

    private float mScale = 1.0f;
    private float Dy = 0;
    private Rect normal = new Rect();
    private Comic mComic;

    @Override
    protected void initPresenter() {
        mPresenter = new ComicDetailPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_comic_detail;
    }

    @Override
    protected void initView() {
        NoScrollStaggeredGridLayoutManager layoutManager = new NoScrollStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new DetailAdapter(this,R.layout.item_chapter);
        mRecycleView.setAdapter(mAdapter);
        comic_id = getIntent().getStringExtra(Constants.COMIC_ID);
        mPresenter.getDetail(comic_id);
        mBack.setOnClickListener(this);
        mScrollView.setScaleTopListener(new MyScaleTopListener());
        mAdapter.setOnItemClickListener(this);
        mLoading.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void showErrorView(Throwable throwable) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        if(throwable instanceof ConnectException){
            mLoadingText.setText("无法访问服务器接口");
        }else{
            mLoadingText.setText("未知错误"+throwable.toString());
        }

    }

    @Override
    public void fillData(Comic comic) {
        mRLloading.setVisibility(View.GONE);
        mComic = comic;
        Glide.with(this)
                .load(comic.getCover())
                .placeholder(R.mipmap.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CenterCrop(this))
                .into(mHeaderView);
        Glide.with(this)
                .load(comic.getCover())
                .placeholder(R.mipmap.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(this, 10), new CenterCrop(this))
                .into(mHeaderViewBg);
        mActionBarTitle.setText(comic.getTitle());
        mTitle.setText(comic.getTitle());
        mAuthorTag.setText(comic.getAuthor()+comic.getTags().toString());
        mCollects.setText(comic.getCollections());
        mDescribe.setText(comic.getDescribe());
        mStatus.setText(comic.getStatus());
        mPopularity.setText(comic.getPopularity());
        mUpdate.setText(comic.getUpdates());
        mPoint.setText(comic.getPoint());
        //showToast(comic.getCollections());
        normal.set(mText.getLeft(),mText.getTop(),getMobileWidth(),mText.getBottom());
        List<String> datas = comic.getChapters();
        if(datas!=null&&datas.size()!=0){
            mAdapter.updateWithClear(datas);
        }else {
            showToast("未取到数据");
        }
    }



    public class MyScaleTopListener implements DetailScrollView.ScaleTopListener {
        @Override
        public void isScale(float y) {
            int height = DisplayUtil.dip2px(ComicDetaiActivity.this,200);
            mScale = y/ height;
            float width = getMobileWidth()*mScale;
            float dx= (width - getMobileWidth())/2;
            mHeaderView.layout((int) (0-dx),0, (int) (getMobileWidth()+dx), (int) y);
            Dy = y - height;
            mText.layout(normal.left,(int)(normal.top+Dy),normal.right,(int)(normal.bottom+Dy));
            Log.d("DetailActivity","Dy="+Dy+",normal="+normal.toString());
        }

        @Override
        public void isFinished() {
            Log.d("DetailScrollView","Dy="+Dy);
            Interpolator in = new DecelerateInterpolator();
            ScaleAnimation ta = new ScaleAnimation(mScale, 1.0f, mScale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            //第一个参数fromX为动画起始时 X坐标上的伸缩尺寸
            //第二个参数toX为动画结束时 X坐标上的伸缩尺寸
            //第三个参数fromY为动画起始时Y坐标上的伸缩尺寸
            //第四个参数toY为动画结束时Y坐标上的伸缩尺寸
            //第五个参数pivotXType为动画在X轴相对于物件位置类型
            //第六个参数pivotXValue为动画相对于物件的X坐标的开始位置
            //第七个参数pivotXType为动画在Y轴相对于物件位置类型
            //第八个参数pivotYValue为动画相对于物件的Y坐标的开始位置
            ta.setInterpolator(in);
            ta.setDuration(300);
            mHeaderView.startAnimation(ta);
            mHeaderView.layout(0,0,getMobileWidth(), DisplayUtil.dip2px(ComicDetaiActivity.this,200));
            //设置文字活动的动画
            TranslateAnimation trans = new TranslateAnimation(0, 0 , Dy, 0 );
            trans.setInterpolator(in);
            trans.setDuration(300);
            mText.startAnimation(trans);
            mText.layout(normal.left,normal.top,normal.right,normal.bottom);
            //统统重置
            mScale = 1.0f;
            Dy = 0;
        }

        @Override
        public void isBlurTransform(float y) {
            //Log.d("DetailActivity","y="+y+",gettopview="+y);
            mHeaderViewBg.setAlpha(1-y);
        }

        @Override
        public void isShowTab(int a) {
            setTab(a);
        }
    }

    public void setTab(int a){
        switch (a){
            case DetailScrollView.SHOW_CHAPER_TAB:
                if(mDetail.getVisibility() == View.GONE){
                    mDetail.setVisibility(View.VISIBLE);
                }else {
                    mTab.setText("详情");
                    mOrder.setVisibility(View.GONE);
                }
                break;
            case DetailScrollView.SHOW_DETAIL_TAB:
                mTab.setText("目录");
                mOrder.setVisibility(View.VISIBLE);
                break;
            case DetailScrollView.SHOW_ACTIONBAR_TITLE:
                if(mDetail.getVisibility() == View.VISIBLE) {
                    mOrder.setVisibility(View.GONE);
                    mDetail.setVisibility(View.GONE);
                    mTab.setText("详情");
                }
                if(mActionBarTitle.getVisibility() == View.GONE){
                    mActionBarTitle.setVisibility(View.VISIBLE);
                    AnimationSet animationSet = new AnimationSet(true);
                    TranslateAnimation trans = new TranslateAnimation(0, 0 , DisplayUtil.dip2px(getApplicationContext(),12), 0 );
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                    animationSet.addAnimation(trans);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.setDuration(200);
                    mActionBarTitle.startAnimation(animationSet);
                }
                break;
            case DetailScrollView.SHOW_NONE:
                if(mDetail.getVisibility() == View.VISIBLE){
                    mTab.setText("详情");
                    mOrder.setVisibility(View.GONE);
                    mDetail.setVisibility(View.GONE);
                }
                if(mActionBarTitle.getVisibility() == View.VISIBLE){
                    mActionBarTitle.setVisibility(View.GONE);
                    AnimationSet animationSet = new AnimationSet(true);
                    TranslateAnimation trans = new TranslateAnimation(0, 0 , 0, DisplayUtil.dip2px(getApplicationContext(),12) );
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
                    animationSet.addAnimation(trans);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.setDuration(200);
                    mActionBarTitle.startAnimation(animationSet);
                }
                break;
        }
    }

    @Override
    public void ShowToast(String toast) {
        showToast(toast);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if(!mAdapter.isOrder()){
            position = mComic.getChapters().size()-position-1;
            Log.d("ComicDetailActivity","position="+position);
        }
        IntentUtil.ToComicChapter(this,comic_id,position, (ArrayList<String>) mComic.getChapters());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.iv_back){
            finish();
        }
    }

    /***
     * 点击事件
     * @param Order
     */

    @OnClick({ R.id.iv_oreder2, R.id.iv_order })
    public void OrderList(ImageView Order) {
        mAdapter.setOrder(!mAdapter.isOrder());
        if(!mAdapter.isOrder()){
            mOrder2.setImageResource(R.mipmap.daoxu);
            mOrder.setImageResource(R.mipmap.daoxu);
        }else{
            mOrder2.setImageResource(R.mipmap.zhengxu);
            mOrder.setImageResource(R.mipmap.zhengxu);
        }
    }

    @OnClick(R.id.btn_read)
    public void StartRead(View view){
        IntentUtil.ToComicChapter(this,comic_id,0, (ArrayList<String>) mComic.getChapters());
    }

    @OnClick(R.id.iv_error)
    public void reload(View view){
        mPresenter.getDetail(comic_id);
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("正在重新加载，请稍后");
    }

}
