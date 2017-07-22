package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.net.ComicService;
import com.android.zhhr.net.MainFactory;
import com.android.zhhr.ui.view.IBaseView;

/**
 * Created by 皓然 on 2017/6/15.
 */

public abstract class BasePresenter<GV extends IBaseView> {

    protected GV mView;
    protected Activity mContext;

    public static final ComicService comicService = MainFactory.getComicServiceInstance();

    public BasePresenter(Activity context, GV view) {
        mContext = context;
        mView = view;
    }
}
