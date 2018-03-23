package com.android.zhhr.ui.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.activity.IndexActivity;
import com.android.zhhr.ui.adapter.IndexAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.utils.DisplayUtil;
import com.android.zhhr.utils.IntentUtil;

import org.w3c.dom.Text;

/**
 * Created by 皓然 on 2017/8/3.
 */

public class IndexLayout extends RelativeLayout implements BaseRecyclerAdapter.OnItemClickListener {
    private static final int  ANIMATION_TIME = 200;
    private TextView mTitle;
    private RelativeLayout mStatusLayout;
    private TextView mStatusText;
    private RecyclerView mIndexRecycle;
    private Comic mComic;
    private ImageView mOrderImage;
    private TextView mOrder;

    private IndexAdapter mAdapter;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public IndexLayout(Context context) {
        this(context, null);
    }

    public IndexLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onFinishInflate() {
        mTitle = (TextView) getChildAt(0);
        mStatusLayout = (RelativeLayout) getChildAt(1);
        mIndexRecycle = (RecyclerView) getChildAt(2);

        mStatusText = (TextView) findViewById(R.id.tv_status);
        mOrder = (TextView) findViewById(R.id.tv_order);
        mOrderImage = (ImageView) findViewById(R.id.iv_order);

        mAdapter = new IndexAdapter(getContext(),R.layout.item_index);
        mIndexRecycle.setAdapter(mAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        mIndexRecycle.setLayoutManager(layoutManager);
        mAdapter.setOnItemClickListener(this);

        mStatusLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.setOrder(!mAdapter.isOrder());
                if(mAdapter.isOrder()){
                    mOrder.setText("正序");
                    mOrderImage.setImageResource(R.mipmap.zhengxu);
                }else{
                    mOrder.setText("倒叙");
                    mOrderImage.setImageResource(R.mipmap.daoxu);
                }

            }
        });
    }

    public void setData(Comic comic,int Current){
        mComic = comic;
        mAdapter.updateWithClear(mComic.getChapters());
        mAdapter.setCurrent(Current);
        mAdapter.notifyDataSetChanged();
        mTitle.setText(mComic.getTitle());
        mStatusText.setText("共"+mComic.getChapters().size()+"话");
    }

    public void setVisibility(int visibility) {
        switch (visibility){
            case View.GONE:
                super.setVisibility(View.GONE);
                AnimationSet animationSet = new AnimationSet(true);
                TranslateAnimation trans = new TranslateAnimation(0, DisplayUtil.dip2px(getContext(),300) ,0 ,0);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
                animationSet.addAnimation(trans);
                animationSet.addAnimation(alphaAnimation);
                animationSet.setDuration(ANIMATION_TIME);
                this.startAnimation(animationSet);
                isShow =false;
                break;
            case View.VISIBLE:
                super.setVisibility(View.VISIBLE);
                AnimationSet animationSet1 = new AnimationSet(true);
                TranslateAnimation trans1 = new TranslateAnimation(DisplayUtil.dip2px(getContext(),300), 0 ,0 ,0);
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(0,1);
                animationSet1.addAnimation(trans1);
                animationSet1.addAnimation(alphaAnimation1);
                animationSet1.setDuration(ANIMATION_TIME);
                this.startAnimation(animationSet1);
                isShow =true;
                break;
            case View.INVISIBLE:
                super.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if(!mAdapter.isOrder()){
            position = mComic.getChapters().size()-position-1;
        }
        IntentUtil.ToComicChapter(getContext(),position,mComic);
        setVisibility(View.GONE);
    }

}
