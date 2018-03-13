package com.android.zhhr.ui.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.utils.DisplayUtil;

import java.util.concurrent.locks.Lock;

/**
 * Created by 皓然 on 2017/7/30.
 */

public class IndexItemView extends RelativeLayout{
    private TextView mTitle;

    private String title;
    private int postion;
    private View view;
    private Drawable img_location;
    private onItemClickLinstener listener;

    public onItemClickLinstener getListener() {
        return listener;
    }

    public void setListener(onItemClickLinstener listener) {
        this.listener = listener;
    }

    public TextView getmTitle() {
        return mTitle;
    }

    public IndexItemView(Context context, String title, final int position,int size) {
        super(context);
        this.title = title;
        this.postion  = position;
        img_location = context.getResources().getDrawable(R.mipmap.location);
        img_location.setBounds(0, 0, img_location.getMinimumWidth(), img_location.getMinimumHeight());
        view = LayoutInflater.from(getContext()).inflate(R.layout.item_detail, null);
        addView(view);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mTitle.setText((position+1)+"-"+title);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onItemClick(view,position);
                }
            }
        });
    }

    public interface onItemClickLinstener{
        void onItemClick(View view, int position);
    }

    public void setCurrentColor(boolean isCurrent){
        if(isCurrent){
            mTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
            mTitle.setCompoundDrawables(null, null, img_location, null);
            mTitle.setCompoundDrawablePadding(DisplayUtil.dip2px(getContext(),10));
        }else{
            mTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTextBlack));
            mTitle.setCompoundDrawables(null, null, null, null);
        }
    }

}
