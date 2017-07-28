package com.android.zhhr.ui.view;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/13.
 */

public interface IDetailView<T> extends  IBaseView{
    void getDataFinish();

    void showErrorView(Throwable throwable);

    void fillData(T data);
}
