package com.android.zhhr.ui.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by zhhr on 2018/3/13.
 */

public class CustomTab extends LinearLayout{
    private LinearLayout mTab;
    private RelativeLayout mTabs[] = new RelativeLayout[4];
    public CustomTab(Context context) {
        this(context,null);
    }

    public CustomTab(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTab(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public CustomTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        mTab = (LinearLayout) getChildAt(1);
        for(int i=0;i<4;i++){
            mTabs[i] = (RelativeLayout) mTab.getChildAt(i);
        }
        super.onFinishInflate();
    }

    public void setCurrentPosition(int postion){
        for(int i=0;i<4;i++){
            if(i!=postion){
                TextView mTextView  = (TextView) mTabs[i].getChildAt(0);
                mTextView.setTextColor(Color.parseColor("#999999"));
                ImageView mBottom = (ImageView) mTabs[i].getChildAt(1);
                mBottom.setVisibility(View.GONE);
            }else{
                TextView mTextView  = (TextView) mTabs[i].getChildAt(0);
                mTextView.setTextColor(Color.parseColor("#333333"));
                ImageView mBottom = (ImageView) mTabs[i].getChildAt(1);
                mBottom.setVisibility(View.VISIBLE);
            }
        }
    }
}
