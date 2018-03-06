package com.android.zhhr.ui.view;

import java.util.HashMap;

/**
 * Created by 皓然 on 2017/8/1.
 */

public interface ISelectDataView<T> extends ILoadDataView<T>{
    void updateList(HashMap map);
    void updateListItem(HashMap map,int position);
    void addAll();
    void removeAll();
    void quitEdit();
}
