package com.android.zhhr.module;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.HomeTitle;
import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.data.entity.db.DBSearchResult;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.db.helper.DaoHelper;
import com.android.zhhr.net.ComicService;
import com.android.zhhr.net.HttpResultFunc;
import com.android.zhhr.net.MainFactory;
import com.android.zhhr.net.cache.CacheProviders;
import com.android.zhhr.utils.DBEntityUtils;
import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.TencentComicAnalysis;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.ResponseBody;


/**
 * Created by 皓然 on 2017/7/31.
 */

public class ComicModule {
    public static final ComicService comicService = MainFactory.getComicServiceInstance();
    public Context context;
    private DaoHelper mHelper;
    private RxAppCompatActivity rxAppCompatActivity;
    public ComicModule(Activity context){
        rxAppCompatActivity = (RxAppCompatActivity) context;
        mHelper = new DaoHelper(context);
    }
    //首页相关
    public void getData(Observer<List<Comic>> observer){
        Observable ComicListObservable = Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic>  mdats = new ArrayList<>();
                    Document recommend = Jsoup.connect(Url.TencentHomePage).get();
                    Document japan = Jsoup.connect(Url.TencentJapanHot).get();
                    Document doc = Jsoup.connect(Url.TencentTopUrl+"1").get();
                    addComic(recommend,mdats,Constants.TYPE_RECOMMEND);
                    addComic(recommend,mdats,Constants.TYPE_BOY_RANK);
                    addComic(recommend,mdats,Constants.TYPE_GIRL_RANK);
                    addComic(recommend,mdats,Constants.TYPE_HOT_SERIAL);
                    addComic(japan,mdats,Constants.TYPE_HOT_JAPAN);
                    addComic(doc,mdats,Constants.TYPE_RANK_LIST);

                    HomeTitle homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("");
                    mdats.add(homeTitle);
                    observableEmitter.onNext(mdats);
                } catch (IOException e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }

        });
        Observable ComicBannerObservable = Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentBanner).get();
                    List<Comic>  mdats = TencentComicAnalysis.TransToBanner(doc);
                    observableEmitter.onNext(mdats);
                } catch (IOException e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        });
        Observable.merge(ComicListObservable, ComicBannerObservable).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 添加漫画到List里
     * @param doc
     * @param mdats
     * @param type
     */
    private void addComic(Document doc, List<Comic> mdats, int type) {
        HomeTitle homeTitle;
        try {
            switch (type){
                case Constants.TYPE_RECOMMEND:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("强推作品");
                    homeTitle.setTitleType(Constants.TYPE_RECOMMEND);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToRecommendComic(doc));
                    break;
                case Constants.TYPE_BOY_RANK:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("少年漫画");
                    homeTitle.setTitleType(Constants.TYPE_HOT_SERIAL);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToBoysComic(doc));
                    break;
                case Constants.TYPE_GIRL_RANK:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("少女漫画");
                    homeTitle.setTitleType(Constants.TYPE_HOT_SERIAL);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToGirlsComic(doc));
                    break;
                case Constants.TYPE_HOT_SERIAL:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("热门连载");
                    homeTitle.setTitleType(Constants.TYPE_HOT_SERIAL);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToNewComic(doc));
                    break;
                case Constants.TYPE_HOT_JAPAN:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("日漫馆");
                    homeTitle.setTitleType(Constants.TYPE_HOT_JAPAN);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToJapanComic(doc));
                    break;
                case Constants.TYPE_RANK_LIST:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("排行榜");
                    homeTitle.setTitleType(Constants.TYPE_RANK_LIST);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToComic(doc));
                    break;
            }
        }catch (Exception e){
            Log.d("zhhr","type = "+type+" is Error");
        }

    }

    public void refreshData(Observer<List<Comic>> observer){
        Observable ComicListObservable = Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic>  mdats = new ArrayList<>();
                    HomeTitle homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("排行榜");
                    mdats.add(homeTitle);

                    Document doc = Jsoup.connect(Url.TencentTopUrl+"1").get();
                    Document doc2 = Jsoup.connect(Url.TencentTopUrl+"2").get();

                    mdats.addAll(TencentComicAnalysis.TransToComic(doc));
                    mdats.addAll(TencentComicAnalysis.TransToComic(doc2));

                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("");
                    mdats.add(homeTitle);
                    observableEmitter.onNext(mdats);
                } catch (IOException e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }

            }
        });
        ComicListObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getMoreComicList(final int page, Observer<List<Comic>> observer){
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentTopUrl+page).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToComic(doc);
                    observableEmitter.onNext(mdats);
                } catch (IOException e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe((Consumer<? super Object>) observer);

    }

    //详情页相关
    public void getComicDetail(final String mComicId,Observer observer){
       Observable.create(new ObservableOnSubscribe<Comic>() {
           @Override
           public void subscribe(@NonNull ObservableEmitter<Comic> observableEmitter) throws Exception {
               try {
                   Document doc = Jsoup.connect(Url.TencentDetail+mComicId).get();
                   Comic mComic = TencentComicAnalysis.TransToComicDetail(doc,context);
                   Comic comicFromDB = (Comic) mHelper.findComic(Long.parseLong(mComicId));
                   if(comicFromDB!=null) {
                       mComic.setCurrentChapter(comicFromDB.getCurrentChapter());
                   }else{
                       mComic.setCurrentChapter(0);
                   }
                   observableEmitter.onNext(mComic);
               } catch (Exception e) {
                   observableEmitter.onError(e);
                   e.printStackTrace();
               }finally {
                   observableEmitter.onComplete();
               }
           }
        }).subscribeOn(Schedulers.io())
               .unsubscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
               .subscribe(observer);
    }

    //阅读相关
    public void getPreNowChapterList(String comic_id,int comic_chapters,Observer observer){
        Observable<PreloadChapters> Observable = comicService.getPreNowChapterList(comic_id,comic_chapters+"");
        CacheProviders.getComicCacheInstance()
                .getPreNowChapterList(Observable,new DynamicKey(comic_id+comic_chapters+"all"),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getChaptersList(String comic_id,int comic_chapters,Observer observer){
        Observable<Chapters> observable = comicService.getChapters(comic_id,comic_chapters+"");
        CacheProviders.getComicCacheInstance()
                .getChapters(observable,new DynamicKey(comic_id+comic_chapters),new EvictDynamicKey(false))
                .map(new Function<Chapters, Object>() {
                    @Override
                    public Object apply(@NonNull Chapters chapters) throws Exception {
                        return chapters.getComiclist();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//生命周期管理
                .subscribe(observer);
    }


    public void getDownloadChaptersList(final String comic_id, final int comic_chapters, Observer observer){
        Observable<Chapters> observable = comicService.getChapters(comic_id,comic_chapters+"");
        CacheProviders.getComicCacheInstance()
                .getChapters(observable,new DynamicKey(comic_id+comic_chapters),new EvictDynamicKey(false))
               /* .map(new Function<Chapters, Object>() {
                    @Override
                    public Object apply(@NonNull Chapters chapters) throws Exception {
                        ArrayList<DownInfo> mLists = new ArrayList<>();
                        for(int i=0;i<chapters.getComiclist().size();i++){
                            DownInfo item = new DownInfo(chapters.getComiclist().get(i));
                            item.setId(Long.parseLong(comic_id+comic_chapters+i));
                            item.setState(DownState.START);
                            item.setComic_id(Long.parseLong(comic_id+comic_chapters));
                            item.setSavePath(FileUtil.SDPATH+FileUtil.COMIC+comic_id+"/"+comic_chapters+"/"+i+".png");
                            mHelper.insert(item);//保存到数据库
                            mLists.add(item);
                        }
                        return mLists;
                    }
                })*/
                .map(new Function<Chapters, Object>() {
                    @Override
                    public Object apply(@NonNull Chapters chapters) throws Exception {
                        return chapters.getComiclist();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//生命周期管理
                .subscribe(observer);
    }
    //搜索相关

    //搜索相关
    public void getDynamicResult(String title,Observer observer){
        Observable<HttpResult<List<SearchBean>>> Observable = comicService.getDynamicSearchResult(Url.TencentSearchUrl+title);
        CacheProviders.getComicCacheInstance()
                .getDynamicSearchResult(Observable,new DynamicKey(Url.TencentSearchUrl+title),new EvictDynamicKey(false))
                .map(new HttpResultFunc<List<SearchBean>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }



    public void getSearchResult(final String title, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentSearchResultUrl+title).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToSearchResultComic(doc);
                    observableEmitter.onNext(mdats);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getTopResult(Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentSearchRecommend).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToSearchTopComic(doc);
                    observableEmitter.onNext(mdats);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                } finally {
                    observableEmitter.onComplete();
                }
            }

        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    //操作数据库相关方法

    public void saveComicToDB(final Comic comic,Observer observer){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    if(mHelper.findComic(comic.getId())==null){
                        if(mHelper.insert(comic)){
                            observableEmitter.onNext(true);
                        }else{
                            observableEmitter.onNext(false);
                        }
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void updateComicCurrentChapter(final String comic_id, final int current_chapters, Observer observer){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    Comic mComic = (Comic) mHelper.findComic(Long.parseLong(comic_id));
                    final java.util.Date date = new java.util.Date();
                    long datetime = date.getTime();
                    if(mComic!=null){
                        mComic.setCurrentChapter(current_chapters+1);
                        mComic.setUpdateTime(datetime);
                        if(mHelper.update(mComic)){
                            observableEmitter.onNext(true);
                        }else{
                            observableEmitter.onNext(false);
                        }
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void updateComicToDB(final Comic mComic, Observer observer){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    Comic comic = (Comic) mHelper.findComic(mComic.getId());
                    if(comic!=null){
                        if(mHelper.update(mComic)){
                            observableEmitter.onNext(true);
                        }else{
                            observableEmitter.onNext(false);
                        }
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void isCollected(final long id,Observer observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>()  {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    Comic mComic = (Comic) mHelper.findComic(id);
                    if(mComic!=null) {
                        if(mComic.getIsCollected()){
                            observableEmitter.onNext(false);
                        }else{
                            observableEmitter.onNext(true);
                        }
                    }else{
                        observableEmitter.onNext(true);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getComicFromDB(final long id,Observer observer) {
        Observable.create(new ObservableOnSubscribe<Comic>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Comic> observableEmitter) throws Exception {
                try{
                    Comic mComic = (Comic) mHelper.findComic(id);
                    if(mComic!=null) {
                        observableEmitter.onNext(mComic);
                    }else{
                        observableEmitter.onNext(null);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getCollectedComicList(Observer observer){
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic> comics = mHelper.queryCollect();
                    observableEmitter.onNext(comics);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }

    /**
     * 插入搜索结果到数据库
     * @param title
     * @param observer
     */
    public void updateSearchResultToDB(final String title, Observer<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    final java.util.Date date = new java.util.Date();
                    long datetime = date.getTime();
                    DBSearchResult result = new DBSearchResult();
                    result.setTitle(title);
                    result.setSearch_time(datetime);
                    if(mHelper.insert(result)){
                        observableEmitter.onNext(true);
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }

    public void getHistorySearch(Observer<List<Comic>> observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try{
                    List<DBSearchResult> results = mHelper.querySearch();
                    List<Comic> comics = DBEntityUtils.transSearchToComic(results);
                    observableEmitter.onNext(comics);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.<List<Comic>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }

    public void clearSearchHistory(Observer observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    if(mHelper.deleteAllSearch()){
                        observableEmitter.onNext(true);
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getDownItemFromDB(final long comic_id, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<DownInfo>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DownInfo>> observableEmitter) throws Exception {
                try{
                    List<DownInfo> results = mHelper.queryDownInfo(comic_id);
                    observableEmitter.onNext(results);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getDownloadItemsFromDB(final long comic_id, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<DBDownloadItems>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DBDownloadItems>> observableEmitter) throws Exception {
                try{
                    LogUtil.d("操作数据库");
                    List<DBDownloadItems> results = mHelper.queryDownloaditmes(comic_id);
                    observableEmitter.onNext(results);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void download(String url, final String filePath, final String fileName, Observer observer) {
        comicService.download("bytes=0" + "-", url)
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {

                    @Override
                    public InputStream apply(@NonNull ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(@NonNull InputStream inputStream) throws Exception {
                        FileUtil.saveImgToSdCard(inputStream, filePath,fileName);
                    }
                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(observer);
    }
}
