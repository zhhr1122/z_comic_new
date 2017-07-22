package com.android.zhhr.ui.custom;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/***
 * 捕获缩小的时候photoview报得异常，使程序不至于崩溃
 */

public class ComicReaderViewpager extends ViewPager
{
    public ComicReaderViewpager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
        }
        return false;
    }


}
