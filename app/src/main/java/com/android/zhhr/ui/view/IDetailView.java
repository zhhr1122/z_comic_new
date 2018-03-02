package com.android.zhhr.ui.view;

import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/13.
 */

public interface IDetailView<T> extends  ILoadDataView<T>{
    void OrderData(int ResId);
    void setCollect(boolean isCollect);
    void setCurrent(int current);
}
