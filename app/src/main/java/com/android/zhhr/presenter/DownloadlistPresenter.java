package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.ui.view.IDownloadlistView;
import com.android.zhhr.ui.view.IIndexView;

/**
 * Created by DELL on 2018/2/12.
 */

public class DownloadlistPresenter extends BasePresenter<IDownloadlistView>{

    public DownloadlistPresenter(Activity context, IDownloadlistView view, Intent intent) {
        super(context, view);
    }
}
