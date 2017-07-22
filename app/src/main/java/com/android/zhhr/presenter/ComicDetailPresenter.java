package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.view.IDetailView;
import com.android.zhhr.ui.view.IMainView;
import com.android.zhhr.utils.TencentComicAnalysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 皓然 on 2017/7/12.
 */

public class ComicDetailPresenter extends  BasePresenter<IDetailView>{
    private String mComicId;
    public ComicDetailPresenter(Activity context, IDetailView view) {
        super(context, view);
    }

    /**
     * 加载详情
     * @param comic_id
     */
    public void getDetail(String comic_id){
        if(comic_id==null){
            mView.ShowToast("获取ID失败");
        }else{
            mComicId = comic_id;
            Subscriber subscriber = new Subscriber<Comic>() {
                @Override
                public void onCompleted() {
                    mView.getDataFinish();
                }

                @Override
                public void onError(Throwable e) {
                    mView.showErrorView(e);
                }

                @Override
                public void onNext(Comic comic) {
                    mView.fillData(comic);
                }


            };
            Observable observable = Observable.create(new Observable.OnSubscribe<Comic>() {
                @Override
                public void call(Subscriber<? super Comic> subscriber) {
                    try {
                        Document doc = Jsoup.connect(Url.TencentDetail+mComicId).get();
                        Comic comic = TencentComicAnalysis.TransToComicDetail(doc);
                        subscriber.onNext(comic);
                    } catch (Exception e) {
                        subscriber.onError(e);
                        e.printStackTrace();
                    }finally {
                        subscriber.onCompleted();
                    }
                }
            });
            observable
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
}
