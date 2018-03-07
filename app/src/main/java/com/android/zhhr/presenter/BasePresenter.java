package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

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


    protected BasePresenter() {
    }

    public long getCurrentTime(){
        java.util.Date date = new java.util.Date();
        long datetime = date.getTime();
        return datetime;
    }



}
