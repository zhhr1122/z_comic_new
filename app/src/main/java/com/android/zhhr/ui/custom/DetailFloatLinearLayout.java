package com.android.zhhr.ui.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by 皓然 on 2017/9/9.
 */

public class DetailFloatLinearLayout extends LinearLayout {
    private RelativeLayout mLocation;
    private RelativeLayout mScroll;
    private FloatButtomOnclickListener mListener;

    public void setOnFloatBottomClickListener(FloatButtomOnclickListener mListener) {
        this.mListener = mListener;
    }

    public DetailFloatLinearLayout(Context context) {
        this(context, null);
    }

    public DetailFloatLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailFloatLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLocation = (RelativeLayout) getChildAt(0);
        mScroll = (RelativeLayout) getChildAt(1);
        mLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onClickLocation(v);
                }
            }
        });
        mScroll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onClickScroll(v);
                }
            }
        });

    }

    public interface FloatButtomOnclickListener {
        void onClickLocation(View view);

        void onClickScroll(View view);
    }
}
