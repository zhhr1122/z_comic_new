package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.db.helper.DaoHelper;
import com.android.zhhr.ui.view.IIndexView;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/28.
 */

public class IndexPresenter extends BasePresenter<IIndexView>{

    public Comic getmComic() {
        return mComic;
    }

    private Comic mComic;
    public IndexPresenter(Activity context, IIndexView view, Intent intent) {
        super(context, view);
        mComic = (Comic) intent.getSerializableExtra(Constants.COMIC);
    }

}
