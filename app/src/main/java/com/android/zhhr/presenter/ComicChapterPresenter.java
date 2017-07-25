package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.data.entity.Subject;
import com.android.zhhr.ui.adapter.ChapterViewpagerAdapter;
import com.android.zhhr.ui.view.IChapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 皓然 on 2017/7/20.
 */

public class ComicChapterPresenter extends BasePresenter<IChapterView>{
    public ComicChapterPresenter(Activity context, IChapterView view) {
        super(context, view);
    }

    public void loadData(String id,int chapter){
        Subscriber subscriber = new Subscriber<Chapters>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(Chapters result) {
                mView.fillData(result);
            }
        };
        comicService.getPreNowChapterList(id,chapter+"")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void clickScreen(float x,float y){
        if (x<0.336){
            mView.prePage();
        }else if(x<0.666){
            mView.showMenu();
        }else {
            mView.nextPage();
        }
    }

    public void setTitle(String comic_chapter_title, int comic_size, int position,int Direct){
        String title = null;
        if(Direct == Constants.LEFT_TO_RIGHT){
            title = comic_chapter_title+"-"+(position+1)+"/"+comic_size;
        }else{
            title = comic_chapter_title+"-"+(comic_size-position)+"/"+comic_size;
        }
        mView.setTitle(title);
    }

    public void loadNextData(String id, int chapter, int direction){

        Subscriber subscriber = new Subscriber<Subject>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(Subject result) {
                Chapters chapters = new Chapters();
                chapters.setNextlist(result.getComiclist());
                mView.nextChapter(chapters);

            }
        };
        comicService.getChapters(id,(chapter+2)+"")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }

    public void loadPreData(String id, int chapter, int direction){

        Subscriber subscriber = new Subscriber<Subject>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(Subject result) {
                Chapters chapters = new Chapters();
                chapters.setPrelist(result.getComiclist());
                mView.preChapter(chapters);

            }
        };
        comicService.getChapters(id,(chapter-2)+"")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }
}
