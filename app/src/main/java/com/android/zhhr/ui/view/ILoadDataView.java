package com.android.zhhr.ui.view;

/**
 * Created by 皓然 on 2017/8/1.
 */

public interface ILoadDataView<T> extends IBaseView{
    void showErrorView(String throwable);

    void fillData(T data);

    void getDataFinish();

}
