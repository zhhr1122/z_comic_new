package com.android.zhhr.ui.view;

import java.util.HashMap;

/**
 * Created by 张皓然 on 2018/1/24.
 */

public interface ISelectDownloadView<T> extends ILoadDataView<T>{
    void startDownload();
    void addToDownloadList(HashMap map);
    void addAll();
    void removeAll();
}
