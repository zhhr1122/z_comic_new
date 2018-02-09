package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.ui.view.IIndexView;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/28.
 */

public class IndexPresenter extends BasePresenter<IIndexView>{
    private List<String > comic_chapter_titles;
    public IndexPresenter(Activity context, IIndexView view) {
        super(context, view);
    }

}
