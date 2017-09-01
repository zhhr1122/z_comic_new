package com.android.zhhr.ui.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.utils.DisplayUtil;

/**
 * Created by 皓然 on 2017/7/30.
 */

public class IndexItemView extends LinearLayout{
    private TextView mTitle;
    private Drawable img_location;
    //TextView mPosition;
    //LinearLayout ll;
    private onItemClickLinstener listener;

    public onItemClickLinstener getListener() {
        return listener;
    }

    public void setListener(onItemClickLinstener listener) {
        this.listener = listener;
    }

    public IndexItemView(Context context, String title, final int position,int Current) {
        super(context);
        this.setOrientation(VERTICAL);
        img_location = getResources().getDrawable(R.mipmap.location);
        img_location.setBounds(0, 0, img_location.getMinimumWidth(), img_location.getMinimumHeight());
        /*ll = new LinearLayout(context);
        ll.setOrientation(HORIZONTAL);*/
        mTitle = new TextView(context);
        //mPosition = new TextView(context);
        mTitle.setText((position+1)+" - "+title);
        //mPosition.setText((position+1)+" - ");
        View mBottom = new View(context);
        mBottom.setBackgroundColor(Color.parseColor("#e2e2e2"));
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(context,60));
        lp.setMargins(DisplayUtil.dip2px(context,10),0,0,0);
        mTitle.setGravity(Gravity.CENTER_VERTICAL);
        mTitle.setTextSize(12);
        if(Current == position+1){
            mTitle.setTextColor(Color.parseColor("#ff9a6a"));
            mTitle.setCompoundDrawables(null, null, img_location, null);
            mTitle.setCompoundDrawablePadding(DisplayUtil.dip2px(getContext(),10));
        }else{
            mTitle.setTextColor(Color.parseColor("#666666"));
        }
       /* mPosition.setGravity(Gravity.CENTER_VERTICAL);
        mPosition.setTextSize(13);
        mPosition.setTextColor(Color.parseColor("#333333"));*/
        /*ll.addView(mPosition,lp);*/
        //ll.addView(mTitle,lp);
        addView(mTitle, lp);
        addView(mBottom, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context,0.5f)));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,position);
            }
        });
    }

    public interface onItemClickLinstener{
        public void onItemClick(View view,int position);
    }

    public void setCurrentColor(boolean isCurrent){
        if(isCurrent){
            mTitle.setTextColor(Color.parseColor("#ff9a6a"));
            mTitle.setCompoundDrawables(null, null, img_location, null);
            mTitle.setCompoundDrawablePadding(DisplayUtil.dip2px(getContext(),10));
        }else{
            mTitle.setTextColor(Color.parseColor("#666666"));
            mTitle.setCompoundDrawables(null, null, null, null);
        }
    }

}
