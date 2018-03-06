package com.android.zhhr.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.utils.DisplayUtil;

/**
 * Created by 张皓然 on 2018/3/5.
 */

public class FloatEditLayout extends RelativeLayout{
    RelativeLayout mSelect;
    RelativeLayout mDelete;
    ImageView mSelectedIcon;
    TextView mSelectText;
    boolean isSelected;

    onClickListener listener;
    private static final int  ANIMATION_TIME = 200;

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    public FloatEditLayout(Context context) {
        this(context,null);
    }

    public FloatEditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onFinishInflate() {
        mSelect = (RelativeLayout) this.findViewById(R.id.rl_select);
        mDelete = (RelativeLayout) this.findViewById(R.id.rl_delete);
        mSelectedIcon = (ImageView) this.findViewById(R.id.iv_select);
        mSelectText = (TextView) this.findViewById(R.id.tv_select_all);
        mSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.OnClickSelect();
                }
            }
        });
        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.OnDelete();
                }
            }
        });
    }

    public void addAll(){
        mSelectText.setText("取消全选");
        mSelectedIcon.setImageResource(R.mipmap.btn_cancel_select);
        isSelected = true;
    }

    public void removeAll(){
        mSelectText.setText("全选");
        mSelectedIcon.setImageResource(R.mipmap.btn_select);
        isSelected = false;
    }


    public FloatEditLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public void setVisibility(int visibility) {
        int height = DisplayUtil.dip2px(getContext(),60);
        switch (visibility){
            case View.GONE:
                super.setVisibility(View.GONE);
                removeAll();
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(1,0);
                AnimationSet animationSet2 = new AnimationSet(true);
                TranslateAnimation tran2 = new TranslateAnimation(0, 0 ,0 , height);
                animationSet2.addAnimation(tran2);
                animationSet2.addAnimation(alphaAnimation1);
                animationSet2.setDuration(ANIMATION_TIME);
                startAnimation(animationSet2);
                break;
            case View.VISIBLE:
                super.setVisibility(View.VISIBLE);
                AlphaAnimation alphaAnimation3 = new AlphaAnimation(0,1);
                AnimationSet animationSet4 = new AnimationSet(true);
                TranslateAnimation tran4 = new TranslateAnimation(0, 0 , height,0 );
                animationSet4.addAnimation(tran4);
                animationSet4.addAnimation(alphaAnimation3);
                animationSet4.setDuration(ANIMATION_TIME);
                startAnimation(animationSet4);
                break;
            case View.INVISIBLE:
                super.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public  interface  onClickListener{
        void OnClickSelect();
        void OnDelete();
    }
}
