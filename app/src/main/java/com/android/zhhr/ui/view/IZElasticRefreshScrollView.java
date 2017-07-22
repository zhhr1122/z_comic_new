package com.android.zhhr.ui.view;

import com.android.zhhr.data.entity.BaseBean;

import java.util.List;

/**
 * Created by 皓然 on 2017/6/21.
 */

public interface IZElasticRefreshScrollView extends IBaseView{

    void getDataFinish();

    void showEmptyView();

    void showErrorView(Throwable throwable);

    void showRefresh();

    void hideRefresh();
}
