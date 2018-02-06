package com.android.zhhr.presenter;

import android.app.Activity;
import android.util.Log;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.SearchResult;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.ISearchView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.observers.Subscribers;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchPresenter extends BasePresenter<ISearchView>{
    private ComicModule mModel;
    private boolean isDynamicLoading;
    private SearchResult mDynamicResult;
    public SearchPresenter(Activity context, ISearchView view) {
        super(context, view);
        mModel = new ComicModule(context);
        isDynamicLoading = false;
    }

    public SearchResult getmDynamicResult() {
        return mDynamicResult;
    }

    public void getDynamicResult(String title) {
        if(!isDynamicLoading){
            mModel.getDynamicResult(title,new Subscriber<SearchResult>(){

                @Override
                public void onCompleted() {
                    mView.getDataFinish();
                    isDynamicLoading = false;
                }

                @Override
                public void onError(Throwable throwable) {
                    isDynamicLoading = false;
                }

                @Override
                public void onNext(SearchResult searchResult) {
                    isDynamicLoading = false;
                    //Log.d("zhhr1122",searchResult.toString());
                    if(searchResult.status == 2){
                        mDynamicResult = searchResult;
                        mView.fillDynamicResult(searchResult);
                    }
                }
            });
            isDynamicLoading = true;
        }
    }

    public void getSearchResult() {
       String title =  mView.getSearchText();
        if(title!=null){
            mModel.getSearchResult(title, new Subscriber<List<Comic>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    //mView.ShowToast(throwable.toString());
                    Log.d("zhhr1122","throwable="+throwable.toString());

                }

                @Override
                public void onNext(List<Comic> comics) {
                    if(comics!=null&&comics.size()!=0){
                        mView.fillResult(comics);
                    }
                }
            });
            mModel.updateSearchResultToDB(title, new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                }

                @Override
                public void onNext(Boolean aBoolean) {
                }
            });
        }
    }

    public void getSearchResult(String title) {
        mView.setSearchText(title);
        if(title!=null){
            mModel.getSearchResult(title, new Subscriber<List<Comic>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onNext(List<Comic> comics) {
                    if(comics!=null&&comics.size()!=0){
                        mView.fillResult(comics);
                    }
                }
            });
            mModel.updateSearchResultToDB(title, new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                }

                @Override
                public void onNext(Boolean aBoolean) {
                }
            });
        }
    }

    public void getHistorySearch(){
        mModel.getHistorySearch(new Subscriber<List<Comic>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(List<Comic> comics) {
                mView.fillData(comics);
            }
        });
    }

    public void getSearch() {
        mModel.getTopResult(new Subscriber<List<Comic>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                //mView.ShowToast(throwable.toString());
                Log.d("zhhr1122","throwable="+throwable.toString());

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
        mModel.clearSearchHistory(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

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
