package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.view.IBaseView;
import com.android.zhhr.ui.view.IIndexView;
import com.android.zhhr.utils.TencentComicAnalysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 皓然 on 2017/7/28.
 */

public class IndexPresenter extends BasePresenter<IIndexView>{
    private List<String > comic_chapter_titles;
    public IndexPresenter(Activity context, IIndexView view) {
        super(context, view);
    }

}
