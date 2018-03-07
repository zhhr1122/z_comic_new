package com.android.zhhr.ui.view;

import com.android.zhhr.data.entity.BaseBean;

import java.util.List;

/**
 * Created by 皓然 on 2017/6/15.
 */

public interface IHomeView<T extends BaseBean> extends IBaseView {
    /**
     * 添加更多数据（用于刷新）
     * @param data
     */
    void appendMoreDataToView(List<T> data);

    /**
     * 没有更多
     */
    void hasNoMoreData();

    void showErrorView(String throwable);

    void fillData(List<T> data);

    void getDataFinish();

    void onRefreshFinish();

    void fillBanner(List<T> data);

    void fillRecent(String str);

}
