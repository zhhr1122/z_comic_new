package com.android.zhhr.ui.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.adapter.IndexAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.utils.DisplayUtil;
import com.android.zhhr.utils.IntentUtil;

/**
 * Created by 皓然 on 2017/8/3.
 */

public class ToastLayout extends RelativeLayout{
    private static final int  ANIMATION_TIME = 200;
    private TextView mContent;
    private View view;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public ToastLayout(Context context) {
        this(context, null);
    }

    public ToastLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToastLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = LayoutInflater.from(getContext()).inflate(R.layout.layout_toast, null);
        addView(view);
        mContent = (TextView) view.findViewById(R.id.tv_content);
    }

    public void setContent(String content){
        if(mContent!=null){
            mContent.setText(content);
        }
    }

    public void showToast(long time){
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation trans1 = new TranslateAnimation(0, 0 ,-DisplayUtil.dip2px(getContext(),60) ,0);
        TranslateAnimation trans2 = new TranslateAnimation(0,0 , 0 , -DisplayUtil.dip2px(getContext(),60));
        trans1.setDuration(ANIMATION_TIME);
        trans2.setStartOffset(ANIMATION_TIME+time);
        trans2.setDuration(ANIMATION_TIME);
        animationSet.addAnimation(trans1);
        animationSet.addAnimation(trans2);
        this.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isShow = true;
                ToastLayout.this.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isShow = false;
                ToastLayout.this.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
