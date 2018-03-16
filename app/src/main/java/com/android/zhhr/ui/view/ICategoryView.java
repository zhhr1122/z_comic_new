package com.android.zhhr.ui.view;

import com.android.zhhr.data.entity.Type;

import java.util.List;
import java.util.Map;

/**
 * Created by zhhr on 2018/3/16.
 */

public interface ICategoryView<T> extends ILoadDataView<T>{
    void fillSelectData(List<Type> mList, Map<String,Integer> mMap);
    void setMap(Map<String,Integer> mMap);
}
