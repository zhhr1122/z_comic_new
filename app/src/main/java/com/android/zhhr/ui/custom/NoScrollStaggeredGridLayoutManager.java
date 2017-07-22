package com.android.zhhr.ui.custom;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by 皓然 on 2017/6/16.
 */

public class NoScrollStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private boolean isScrollEnabled = true;

    public NoScrollStaggeredGridLayoutManager(int num,int Orientation) {
        super(num,Orientation);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
