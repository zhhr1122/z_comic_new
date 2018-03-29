package com.android.zhhr.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;

/**
 * Created by zhhr on 2018/3/13.
 */

public class CustomTab extends LinearLayout {
    private LinearLayout mTab;
    private RelativeLayout mTabs[] = new RelativeLayout[4];
    public CustomTab(Context context) {
        this(context,null);
    }

    public CustomTab(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mTextView.setTextAppearance(R.style.colorTextColorLight);
                }else{
                    mTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextColorLight));
                }
                ImageView mBottom = (ImageView) mTabs[i].getChildAt(1);
                mBottom.setVisibility(View.GONE);
            }else{
                TextView mTextView  = (TextView) mTabs[i].getChildAt(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mTextView.setTextAppearance(R.style.colorTextBlack);
                }else{
                    mTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTextColorDark));
                }
                ImageView mBottom = (ImageView) mTabs[i].getChildAt(1);
                mBottom.setVisibility(View.VISIBLE);
            }
        }
    }
}
