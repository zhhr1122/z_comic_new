package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.view.IMainView;
import com.android.zhhr.utils.TencentComicAnalysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 皓然 on 2017/6/15.
 */

public class MainPresenter extends BasePresenter<IMainView>{
    private int num =20;

    public MainPresenter(Activity context, IMainView view) {
        super(context, view);
    }
    public void refreshData(){
        Subscriber subscriber = new Subscriber<List<Comic>>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(List<Comic> comics) {
                //mView.ShowToast(movieEntities.toString());
                mView.fillData(comics);
            }


        };
        Observable observable = Observable.create(new Observable.OnSubscribe<List<Comic>>() {
            @Override
            public void call(Subscriber<? super List<Comic>> subscriber) {
                try {
                    Document doc = Jsoup.connect(Url.TencentTopUrl+"1").get();
                    Document doc2 = Jsoup.connect(Url.TencentTopUrl+"2").get();
                    List<Comic>  mdats = TencentComicAnalysis.TransToComic(doc);
                    mdats.addAll(TencentComicAnalysis.TransToComic(doc2));
                    subscriber.onNext(mdats);
                } catch (IOException e) {
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

    public void loadData(int page){
        /*Subscriber subscriber = new Subscriber<List<MovieEntity>>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(List<MovieEntity> result) {
                mView.fillData(result);
            }
        };
        comicService.getTopMovie(num*page,num )
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Chapters, List<MovieEntity>>() {

                    @Override
                    public List<MovieEntity> call(Chapters subject) {
                        return (List<MovieEntity>) subject.getSubjects();
                    }
                })
                .subscribe(subscriber);*/
    }


    public void loadMoreData(final int page){
        Subscriber subscriber = new Subscriber<List<Comic>>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(List<Comic> movieEntities) {
                mView.appendMoreDataToView(movieEntities);
            }


        };
        Observable observable = Observable.create(new Observable.OnSubscribe<List<Comic>>() {
            @Override
            public void call(Subscriber<? super List<Comic>> subscriber) {
                try {
                    Document doc = Jsoup.connect(Url.TencentTopUrl+page).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToComic(doc);
                    subscriber.onNext(mdats);
                } catch (IOException e) {
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
