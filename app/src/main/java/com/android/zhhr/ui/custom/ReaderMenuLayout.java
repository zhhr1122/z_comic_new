package com.android.zhhr.ui.custom;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.android.zhhr.R;
import com.android.zhhr.utils.DisplayUtil;
import com.xw.repo.BubbleSeekBar;

import butterknife.Bind;

/**
 * Created by 皓然 on 2017/7/22.
 */

public class ReaderMenuLayout extends RelativeLayout{
    private RelativeLayout mTop;
    private RelativeLayout mBottom;
    private boolean isShow;
    private static final int  ANIMATION_TIME = 200;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public ReaderMenuLayout(Context context) {
        this(context, null);
    }

    public ReaderMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        mTop = (RelativeLayout) getChildAt(2);
        mBottom = (RelativeLayout) getChildAt(3);
    }

    @Override
    public void setVisibility(int visibility) {
        int Topheight = DisplayUtil.dip2px(getContext(),110);
        int height = DisplayUtil.dip2px(getContext(),60);
        switch (visibility){
            case View.GONE:
                mTop.setVisibility(View.GONE);
                mBottom.setVisibility(View.GONE);
                AnimationSet animationSet1 = new AnimationSet(true);
                TranslateAnimation trans1 = new TranslateAnimation(0, 0 , 0 ,-Topheight);
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(1,0);
                animationSet1.addAnimation(trans1);
                animationSet1.addAnimation(alphaAnimation1);
                animationSet1.setDuration(ANIMATION_TIME);
                mTop.startAnimation(animationSet1);

                AnimationSet animationSet2 = new AnimationSet(true);
                TranslateAnimation tran2 = new TranslateAnimation(0, 0 ,0 , height);
                animationSet2.addAnimation(tran2);
                animationSet2.addAnimation(alphaAnimation1);
                animationSet2.setDuration(ANIMATION_TIME);
                mBottom.startAnimation(animationSet2);
                isShow =false;

                break;
            case View.VISIBLE:
                mTop.setVisibility(View.VISIBLE);
                mBottom.setVisibility(View.VISIBLE);
                AnimationSet animationSet3 = new AnimationSet(true);
                TranslateAnimation trans = new TranslateAnimation(0, 0 , -Topheight,0 );
                AlphaAnimation alphaAnimation3 = new AlphaAnimation(0,1);
                animationSet3.addAnimation(trans);
                animationSet3.addAnimation(alphaAnimation3);
                animationSet3.setDuration(ANIMATION_TIME);
                mTop.startAnimation(animationSet3);

                AnimationSet animationSet4 = new AnimationSet(true);
                TranslateAnimation tran4 = new TranslateAnimation(0, 0 , height,0 );
                animationSet4.addAnimation(tran4);
                animationSet4.addAnimation(alphaAnimation3);
                animationSet4.setDuration(ANIMATION_TIME);
                mBottom.startAnimation(animationSet4);
                isShow =true;
                break;
            case View.INVISIBLE:
                super.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
