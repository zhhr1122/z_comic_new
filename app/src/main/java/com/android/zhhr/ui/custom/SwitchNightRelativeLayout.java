package com.android.zhhr.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.zhhr.R;
import com.android.zhhr.utils.DisplayUtil;
import com.android.zhhr.utils.ZToast;

/**
 * Created by zhhr on 2018/3/14.
 */

public class SwitchNightRelativeLayout extends RelativeLayout{
    private ImageView mImageView;
    private View view;
    private Context mContext;

    public SwitchNightRelativeLayout(Context context) {
        this(context,null);
    }

    public SwitchNightRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwitchNightRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext= context;
        view = LayoutInflater.from(getContext()).inflate(R.layout.layout_switch_night, null);
        mImageView = (ImageView) view.findViewById(R.id.tv_icon);
        addView(view);
    }

    public void setVisibility(int visibility,boolean isNight) {
        int id = 0;
        if(!isNight){
            id = R.mipmap.icon_switch_night;
        }else{
            id = R.mipmap.icon_switch_day;
        }
        switch (visibility){
            case View.GONE:
                super.setVisibility(View.VISIBLE);
                AnimationSet animationSet1 = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                TranslateAnimation transAnimation = new TranslateAnimation(0, 0, DisplayUtil.getScreenHeight(mContext)/2,0);
                int pivotType = Animation.RELATIVE_TO_SELF; // 相对于自己
                float pivotX = .5f; // 取自身区域在X轴上的中心点
                float pivotY = .5f; // 取自身区域在Y轴上的中心点
                int rotate = 0;
                if(isNight){
                    rotate = -120;
                }
                RotateAnimation rotateAnimation = new RotateAnimation(0,rotate,pivotType, pivotX, pivotType, pivotY);
                animationSet1.addAnimation(alphaAnimation);
                animationSet1.addAnimation(transAnimation);
                animationSet1.addAnimation(rotateAnimation);

                alphaAnimation.setDuration(300);
                transAnimation.setDuration(300);
                rotateAnimation.setStartOffset(300);
                rotateAnimation.setDuration(1000);
                mImageView.setImageResource(id);
                animationSet1.setAnimationListener(new Animation.AnimationListener() {
                    //监听动画开始时弹出TOAST
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }                   //动画结束时开启第二个
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        SwitchNightRelativeLayout.super.setVisibility(View.GONE);
                        ZToast.makeText((ViewGroup) getParent(),"切换成功",1000).show();
                    }
                });
                mImageView.startAnimation(animationSet1);
                break;
            case View.VISIBLE:
                super.setVisibility(View.VISIBLE);
                break;
            case View.INVISIBLE:
                super.setVisibility(View.INVISIBLE);
                break;
        }
    }


}
