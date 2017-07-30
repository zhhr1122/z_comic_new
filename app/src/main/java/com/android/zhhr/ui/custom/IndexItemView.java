package com.android.zhhr.ui.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zhhr.utils.DisplayUtil;

/**
 * Created by 皓然 on 2017/7/30.
 */

public class IndexItemView extends LinearLayout{
    TextView mTitle;
    //TextView mPosition;
    //LinearLayout ll;
    private onItemClickLinstener listener;

    public onItemClickLinstener getListener() {
        return listener;
    }

    public void setListener(onItemClickLinstener listener) {
        this.listener = listener;
    }

    public IndexItemView(Context context, String title, final int position) {
        super(context);
        this.setOrientation(VERTICAL);
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
        mTitle.setTextColor(Color.parseColor("#666666"));
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

}
