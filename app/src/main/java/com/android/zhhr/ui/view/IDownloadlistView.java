package com.android.zhhr.ui.view;

import com.android.zhhr.data.entity.Comic;

/**
 * Created by DELL on 2018/2/12.
 */

public interface IDownloadlistView<T> extends ILoadDataView<T>{
    void onStartDownload(int chapters);
    void onPausedDownload(int chapters);
    void onStartAll();
    void onPauseAll();
    void onSelectALL();
    void onDeleteAll();
}
