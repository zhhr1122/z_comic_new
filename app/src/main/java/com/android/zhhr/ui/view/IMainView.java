package com.android.zhhr.ui.view;

import com.android.zhhr.data.entity.BaseBean;

import java.util.List;

/**
 * Created by 皓然 on 2017/6/15.
 */

public interface IMainView<T extends BaseBean> extends IZElasticRefreshScrollView {
    /**
     *  填充数据
     */
    void fillData(List<T> data);

    /**
     * 添加更多数据（用于刷新）
     * @param data
     */
    void appendMoreDataToView(List<T> data);

    /**
     * 没有更多
     */
    void hasNoMoreData();

}
