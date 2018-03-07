package com.android.zhhr.ui.view;

import com.android.zhhr.data.entity.Comic;

import java.util.HashMap;

/**
 * Created by DELL on 2018/2/12.
 */

public interface IDownloadlistView<T> extends ILoadDataView<T>{
    void onLoadMoreData(T t);
    void updateView(int postion);
    void onDownloadFinished();
    void onPauseOrStartAll();


    void updateList(HashMap map);
    void updateListItem(HashMap map,int position);
    void addAll();
    void removeAll();
    void quitEdit();
}
