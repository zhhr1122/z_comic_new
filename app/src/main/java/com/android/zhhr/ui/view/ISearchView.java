package com.android.zhhr.ui.view;

import java.util.List;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public interface ISearchView<T> extends ILoadDataView<T>{
    void clearText();
    //动态搜索
    void fillDynamicResult(T t);
    //搜索结果
    void fillResult(T t);

    void fillHotRank(List<String> ranks);
}
