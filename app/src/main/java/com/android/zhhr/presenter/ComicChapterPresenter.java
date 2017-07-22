package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Subject;
import com.android.zhhr.ui.adapter.ChapterViewpagerAdapter;
import com.android.zhhr.ui.view.IChapterView;

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

    public void loadData(String id,String chapter){
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
                mView.fillData(result);
            }
        };
        comicService.getChapters(id,chapter)
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
}
