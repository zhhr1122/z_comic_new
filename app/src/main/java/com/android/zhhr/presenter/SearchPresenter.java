package com.android.zhhr.presenter;

import android.app.Activity;
import android.util.Log;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.ISearchView;
import com.android.zhhr.utils.DBEntityUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchPresenter extends BasePresenter<ISearchView>{
    private ComicModule mModel;
    private boolean isDynamicLoading;
    private List<Comic> mHistroys;
    private List<Comic> mResult;
    public SearchPresenter(Activity context, ISearchView view) {
        super(context, view);
        mModel = new ComicModule(context);
        isDynamicLoading = false;
        mHistroys = new ArrayList<>();
        mResult = new ArrayList<>();
    }


    public void getDynamicResult(String title) {
        if(!isDynamicLoading){
            mModel.getDynamicResult(title,new DisposableObserver<List<SearchBean>>(){
                @Override
                public void onError(Throwable throwable) {
                    isDynamicLoading = false;
                }

                @Override
                public void onComplete() {
                    mView.getDataFinish();
                    isDynamicLoading = false;
                }

                @Override
                public void onNext(List<SearchBean> searchBeen) {
                    if(searchBeen!=null&&searchBeen.size()!=0){
                        mView.fillDynamicResult(DBEntityUtils.transDynamicSearchToComic(searchBeen));
                    }
                }
            });
            isDynamicLoading = true;
        }
    }

    public void getSearchResult() {
       final String title =  mView.getSearchText();
        if(title!=null){
            mModel.getSearchResult(title, new DisposableObserver<List<Comic>>() {

                @Override
                protected void onStart() {
                    super.onStart();
                    mResult.clear();
                }

                @Override
                public void onError(Throwable throwable) {
                    mView.showErrorView(title);
                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onNext(List<Comic> comics) {
                    mResult.addAll(comics);
                    if(comics!=null&&comics.size()!=0){
                        mView.fillResult(mResult);
                    }
                }
            });
            mModel.updateSearchResultToDB(title, new DisposableObserver<Boolean>() {
                @Override
                public void onError(Throwable throwable) {
                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    Comic comic = new Comic();
                    comic.setTitle(title);
                    mHistroys.add(0,comic);
                    mView.fillData(mHistroys);
                }
            });
        }
    }

    public void getSearchResult(final String title) {
        mView.setSearchText(title);
        if(title!=null){
            mModel.getSearchResult(title, new DisposableObserver<List<Comic>>() {

                @Override
                protected void onStart() {
                    mResult.clear();
                    super.onStart();
                }

                @Override
                public void onError(Throwable throwable) {
                    mView.showErrorView(title);
                }

                @Override
                public void onComplete() {

                }


                @Override
                public void onNext(List<Comic> comics) {
                    mResult.addAll(comics);
                    if(comics!=null&&comics.size()!=0){
                        mView.fillResult(mResult);
                    }
                }
            });
            mModel.updateSearchResultToDB(title, new DisposableObserver<Boolean>() {
                @Override
                public void onError(Throwable throwable) {
                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    Comic comic = new Comic();
                    comic.setTitle(title);
                    mHistroys.add(0,comic);
                    mView.fillData(mHistroys);
                }
            });
        }
    }

    public void getHistorySearch(){
        mModel.getHistorySearch(new Observer<List<Comic>>() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(List<Comic> comics) {
                mHistroys = comics;
                mView.fillData(comics);
            }
        });
    }

    public void getSearch() {
        mModel.getTopResult(new Observer<List<Comic>>() {
            @Override
            public void onError(Throwable throwable) {
                //mView.ShowToast(throwable.toString());
                Log.d("zhhr1122","throwable="+throwable.toString());

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(List<Comic> comics) {
                if(comics!=null&&comics.size()!=0){
                    mView.fillTopSearch(comics);
                }
            }
        });
        getHistorySearch();
    }

    public void clearHistory() {
        mModel.clearSearchHistory(new Observer<Boolean>() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                    mView.fillData(null);
                }
            }

        });

    }
}
