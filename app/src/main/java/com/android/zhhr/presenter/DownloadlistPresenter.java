package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IDownloadlistView;
import com.android.zhhr.ui.view.IIndexView;
import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.android.zhhr.net.MainFactory.comicService;

/**
 * Created by DELL on 2018/2/12.
 */

public class DownloadlistPresenter extends BasePresenter<IDownloadlistView>{
    private Comic mComic;
    private HashMap<Integer,Integer> mMap;
    private ComicModule mModel;
    private ArrayList<DBDownloadItems> mLists;

    public DownloadlistPresenter(Activity context, IDownloadlistView view, Intent intent) {
        super(context, view);
        mComic= (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
        this.mModel = new ComicModule(context);
    }

    public void initData() {
        mLists = new ArrayList<>();
        DBDownloadItems item;
        for (Map.Entry<Integer, Integer> entry : mMap.entrySet()) {
            if(entry.getValue() != Constants.CHAPTER_FREE){
                item = new DBDownloadItems();
                item.setComic_id(mComic.getId());
                item.setTitle(mComic.getTitle());
                item.setChapters(entry.getKey());
                item.setChapters_title(mComic.getChapters().get(entry.getKey()));
                mLists.add(item);
            }
        }
        if(mLists!=null&&mLists.size()!=0){
            mView.fillData(mLists);
        }
    }

    public void getComic(){
        comicService.downloadFile(Url.test_url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<ResponseBody> responseBody) {
                        FileUtil.saveImgToSdCard(responseBody.body().byteStream(),FileUtil.SDPATH+FileUtil.COMIC,"a.png");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
