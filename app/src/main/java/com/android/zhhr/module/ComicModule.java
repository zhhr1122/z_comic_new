package com.android.zhhr.module;

import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.HomeTitle;
import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.data.entity.db.DBChapters;
import com.android.zhhr.data.entity.db.DBSearchResult;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.db.helper.DaoHelper;
import com.android.zhhr.net.ComicService;
import com.android.zhhr.net.Function.HttpResultFunc;
import com.android.zhhr.net.Function.RetryFunction;
import com.android.zhhr.net.MainFactory;
import com.android.zhhr.net.cache.CacheProviders;
import com.android.zhhr.presenter.ComicChapterPresenter;
import com.android.zhhr.presenter.DownloadChapterlistPresenter;
import com.android.zhhr.utils.DBEntityUtils;
import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.GlideCacheUtil;
import com.android.zhhr.utils.KukuComicAnalysis;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.NetworkUtils;
import com.android.zhhr.utils.TencentComicAnalysis;
import com.orhanobut.hawk.Hawk;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    //addComic(recommend,mdats,Constants.TYPE_RECOMMEND);
                    addComic(recommend,mdats,Constants.TYPE_HOT_SERIAL);
                    addComic(recommend,mdats,Constants.TYPE_BOY_RANK);
                    addComic(recommend,mdats,Constants.TYPE_GIRL_RANK);
                    addComic(japan,mdats,Constants.TYPE_HOT_JAPAN);
                    addComic(doc,mdats,Constants.TYPE_RANK_LIST);

                    HomeTitle homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("");
                    mdats.add(homeTitle);
                    observableEmitter.onNext(mdats);
                } catch (Exception e) {
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
                    homeTitle.setTitleType(Constants.TYPE_BOY_RANK);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToBoysComic(doc));
                    break;
                case Constants.TYPE_GIRL_RANK:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("少女漫画");
                    homeTitle.setTitleType(Constants.TYPE_GIRL_RANK);
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
    public void getComicDetail(final String mComicId, final int from, Observer observer){
       Observable.create(new ObservableOnSubscribe<Comic>() {
           @Override
           public void subscribe(@NonNull ObservableEmitter<Comic> observableEmitter) throws Exception {
               try {
                   Comic comicFromDB = (Comic) mHelper.findComic(Long.parseLong(mComicId));
                   if(NetworkUtils.isAvailable(rxAppCompatActivity)){
                       Comic mComic;
                       if(from == Constants.FROM_TENCENT){
                           Document doc = Jsoup.connect(Url.TencentDetail+mComicId).get();
                           mComic = TencentComicAnalysis.TransToComicDetail(doc);
                           mComic.setFrom(from);
                       }else{
                           String url = Url.KukuComicBase+"/comiclist/"+(Long.parseLong(mComicId)/1000000);
                           Document doc = Jsoup.connect(url).get();
                           mComic = KukuComicAnalysis.TransToComicDetail(doc);
                           mComic.setFrom(from);
                       }

                       if(comicFromDB!=null) {
                           mComic.setCurrentChapter(comicFromDB.getCurrentChapter());
                           mComic.setStateInte(comicFromDB.getStateInte());
                           mComic.setDownloadTime(comicFromDB.getDownloadTime());
                           mComic.setCollectTime(comicFromDB.getCollectTime());
                           mComic.setClickTime(comicFromDB.getClickTime());
                           mComic.setDownload_num(comicFromDB.getDownload_num());
                           mComic.setDownload_num_finish(comicFromDB.getDownload_num_finish());
                           mComic.setCurrent_page(comicFromDB.getCurrent_page());
                           mComic.setCurrent_page_all(comicFromDB.getCurrent_page_all());
                           mComic.setIsCollected(comicFromDB.getIsCollected());
                           mComic.setReadType(comicFromDB.getReadType());
                           mComic.setFrom(from);
                       }else{
                           mComic.setCurrentChapter(0);
                       }
                       observableEmitter.onNext(mComic);
                   }else{
                       if(comicFromDB !=null){
                           observableEmitter.onNext(comicFromDB);
                       }else{
                           observableEmitter.onError(new ConnectException());
                       }
                   }
               } catch (SocketTimeoutException e){
                   observableEmitter.onError(e);
               } catch (Exception e) {
                   LogUtil.e(e.toString());
                   if(e instanceof InterruptedIOException){//如果线程错误不做任何处理

                   }else{
                       observableEmitter.onError(e);
                   }
               }finally {
                   observableEmitter.onComplete();
               }
           }
        }).subscribeOn(Schedulers.io())
               .retryWhen(new RetryFunction())
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

    public void getChaptersList(final Comic mComic, final ComicChapterPresenter.OnProgressListener listener, final int comic_chapters, Observer observer){
        //拉取漫画用了多级缓存
        //首先从数据库看有没有下载完，下载完成则直接从数据库读取本地图片
        DBChapters items;
        final String comic_id = mComic.getId()+"";
        try{
            //防止-1的情况出现
            items = mHelper.findDBDownloadItems(Long.parseLong(comic_id+comic_chapters));
            if(items.getState()==DownState.DELETE){
                items = null;
            }
        }catch (Exception e){
            items = null;
        }
        //判断是否在下载列表中，item不为null表示添加到了数据库，但是只取出其中的下载完成和有缓存的数据
        if(items!=null&&items.getComiclist()!=null){
            LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"之前加载过，从数据库中取出");
            if(items.getState() == DownState.FINISH){
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"已经下载完成，加载SD卡中的漫画");
                items.setComiclist(items.getChapters_path());
            }
            observer.onNext(items);
            observer.onComplete();
        }else{
            LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"联网获取");
            //如果是腾讯漫画
            if(mComic.getFrom() == Constants.FROM_TENCENT){
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"调用腾讯漫画接口");
                //否则就联网拉取数据，先读取接口的缓存
                Observable<DBChapters> observable = comicService.getChapters(comic_id,comic_chapters+"");
                //真正调用联网接口
                CacheProviders.getComicCacheInstance()
                        .getChapters(observable,new DynamicKey(comic_id+comic_chapters),new EvictDynamicKey(false))
                        .retryWhen(new RetryFunction())
                        .map(new Function<DBChapters, Object>() {
                            @Override
                            public Object apply(@NonNull DBChapters chapters) throws Exception {
                                chapters.setComic_id(mComic.getId());
                                chapters.setChapters(comic_chapters);
                                return chapters;
                            }
                        }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//生命周期管理
                        .subscribe(observer);
                //腾讯漫画采用了RxCache作为缓存，所有不需要存入到数据库也可以进行缓存
            }else{ //如果是酷酷漫画
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"调用酷酷漫画接口");
                //否则就联网拉取数据，先读取接口的缓存
                /*String chapters[] = mComic.getChapters_url().get(comic_chapters).split("/");
                Observable<DBChapters> observable = comicService.getKuKuChapterList(chapters[2],chapters[3]);
                //真正调用联网接口
                CacheProviders.getComicCacheInstance()
                        .getKuKuChapterList(observable,new DynamicKey(comic_id+comic_chapters),new EvictDynamicKey(false))
                        .retryWhen(new RetryFunction())
                        .map(new Function<DBChapters, Object>() {
                            @Override
                            public Object apply(@NonNull DBChapters chapters) throws Exception {
                                chapters.setComic_id(mComic.getId());
                                chapters.setChapters(comic_chapters);
                                return chapters;
                            }
                        }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//生命周期管理
                        .subscribe(observer);*/
                //腾讯漫画采用了RxCache作为缓存，所有不需要存入到数据库也可以进行缓存
                final List<String> imageUrl = new ArrayList<>();
                Observable.create(new ObservableOnSubscribe<DBChapters>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<DBChapters> observableEmitter) throws Exception {
                        try{
                            int page = 1;
                            int size = 0;
                            int offset = 2;
                            String url = Url.KukuComicBase+mComic.getChapters_url().get(comic_chapters);
                           while (ComicChapterPresenter.isLoading|| DownloadChapterlistPresenter.isLoading){
                                //解析漫画
                               Document doc = Jsoup.connect(url+page+".htm").get();
                               if(size==0){
                                   String size_pre = doc.getElementsByAttributeValue("valign","top").get(0).text().split("共")[1];
                                   size = Integer.parseInt(size_pre.split("页")[0]);
                               }
                               String image = doc.select("script").get(3).toString();
                               String image_url[] = image.split("src=");
                               //酷酷漫画每一个page都加载了两张漫画
                               String image_url1 = "";
                               String image_url2 = "";
                               //解析漫画，目前发现三种形式，先试试，以后再加
                               try{//适用于大多数
                                   image_url1= Url.KukuComicImageBae+image_url[0].split("\"")[5].split("'")[0];
                                   image_url2 = Url.KukuComicImageBae+image_url[1].split("\"")[2].split("'")[0];
                               }catch (Exception e){//适用于整本的漫画（火影）
                                   image_url1 = Url.KukuComicImageBae+image_url[1].split("\"")[2].split("'")[0];
                                   if(image_url.length>2){
                                       image_url2 = Url.KukuComicImageBae+image_url[2].split("\"")[2].split("'")[0];
                                   }else{
                                       offset = 1;//寄生兽专属
                                   }
                               }
                               if(imageUrl.size()<size){
                                   imageUrl.add(image_url1);
                                   LogUtil.d(mComic.getTitle()+(comic_chapters+1)+image_url1);
                               }
                               if(imageUrl.size()<size&&offset==2){//寄生兽专属
                                   imageUrl.add(image_url2);
                                   LogUtil.d(mComic.getTitle()+(comic_chapters+1)+image_url2);
                               }
                               if(listener!=null){//及时更新进度
                                   listener.OnProgress(page,size);
                               }
                               page = page +offset;
                            }
                        } catch (HttpStatusException e){
                            DBChapters chapters = new DBChapters();
                            chapters.setId(Long.parseLong(comic_id+comic_chapters));
                            chapters.setChapters_title(mComic.getChapters().get(comic_chapters));
                            chapters.setComic_id(mComic.getId());
                            chapters.setChapters(comic_chapters);
                            chapters.setComiclist(imageUrl);
                            chapters.setState(DownState.CACHE);
                            //传回给VIEW
                            observableEmitter.onNext(chapters);
                            try{
                                //酷酷漫画没有采用了RxCache作为缓存，所有需要存入到数据库也进行缓存
                                //把刚刚拼接成的list存入数据库，并设置状态为cache，和下载列表区分
                                mHelper.insert(chapters);
                                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"存入数据库,id="+comic_id+comic_chapters);
                            }catch (SQLiteConstraintException exception){
                                //如果已经在数据库中存在，则不插入数据库
                                LogUtil.e(mComic.getTitle()+(comic_chapters+1)+"插入数据库失败");
                            }
                        }catch (SocketTimeoutException e){
                            observableEmitter.onError(e);
                        } catch (Exception e){
                            LogUtil.e(e.toString());
                            if(e instanceof InterruptedIOException){//如果线程错误不做任何处理

                            }else{
                                observableEmitter.onError(e);
                            }
                        }finally {
                            observableEmitter.onComplete();
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//暂时先关闭生命周期处理，防止崩溃
                        .subscribe(observer);
            }
        }
    }


    public void getDownloadChaptersList(final Comic comic, final int comic_chapters, Observer observer){
        getChaptersList(comic,null,comic_chapters,observer);
      /*  Observable<DBChapters> observable = comicService.getChapters(comic_id,comic_chapters+"");
        CacheProviders.getComicCacheInstance()
                .getChapters(observable,new DynamicKey(comic_id+comic_chapters),new EvictDynamicKey(false))
                .retryWhen(new RetryFunction())
                .map(new Function<DBChapters, Object>() {
                    @Override
                    public Object apply(@NonNull DBChapters chapters) throws Exception {
                        return chapters.getComiclist();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//生命周期管理
                .subscribe(observer);*/
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
                List<Comic> mdats = new ArrayList<>();
                List<Comic> mkukudatas = new ArrayList<>();
                try {
                    //腾讯的漫画
                    Document doc = Jsoup.connect(Url.TencentSearchResultUrl+title).get();
                    mdats = TencentComicAnalysis.TransToSearchResultComic(doc);
                    observableEmitter.onNext(mdats);
                }catch (SocketTimeoutException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    //observableEmitter.onError(e);
                    e.printStackTrace();
                }
                if(Constants.isNeedKuku){
                    try{
                        //酷酷的漫画
                        String kukuUrl = Url.KukuSearchUrlHead+ URLEncoder.encode(title, "GBK")+Url.KukuSearchUrlFoot;
                        Document doc_kuku = Jsoup.connect(kukuUrl).get();
                        mkukudatas = KukuComicAnalysis.TransToSearchResultComic_Kuku(doc_kuku);
                        observableEmitter.onNext(mkukudatas);
                    }catch (SocketTimeoutException e){
                        e.printStackTrace();
                    } catch (Exception e) {
                        //observableEmitter.onError(e);
                        e.printStackTrace();
                    }
                }
                if(mdats.size()==0&&mkukudatas.size()==0){
                    observableEmitter.onError(new NullPointerException());
                }
                observableEmitter.onComplete();
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
                    if(mComic!=null){
                        mComic.setCurrentChapter(current_chapters+1);
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
     *得到按照时间排列的list
     * @param page 页面
     * @param observer
     */
    public void getHistoryComicList(final int page, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic> comics = mHelper.queryHistory(page);
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

    public void getDownloadComicList(Observer observer){
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic> comics = mHelper.queryDownloadComic();
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
        Observable.create(new ObservableOnSubscribe<List<DBChapters>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DBChapters>> observableEmitter) throws Exception {
                try{
                    List<DBChapters> results = mHelper.queryDownloadItems(comic_id);
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

    public void getDownloadItemsFromDB(final Comic mComic, final HashMap<Integer,Integer> mMap, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<DBChapters>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DBChapters>> observableEmitter) throws Exception {
                try{
                    if(mMap.size()>0){
                        DBChapters item;
                        //把hashmap進行排序操作
                        List<Map.Entry<Integer,Integer>> list = new ArrayList<>(mMap.entrySet());
                        Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
                            public int compare(Map.Entry<Integer, Integer> o1,
                                               Map.Entry<Integer, Integer> o2) {
                                return o1.getKey().compareTo(o2.getKey());
                            }
                        });
                        //遍历map
                        for(Map.Entry<Integer,Integer> mapping:list){
                            if(mapping.getValue() == Constants.CHAPTER_SELECTED){
                                try{
                                    //有可能阅读的时候加载过一次，存入了数据库，所以要在数据库中查询一下
                                    item = mHelper.findDBDownloadItems(Long.parseLong(mComic.getId()+""+mapping.getKey()));
                                    if(item.getState()==DownState.DELETE){
                                        item = new DBChapters();//没查询到或者已经被删除过都置为new
                                    }
                                }catch (Exception e){
                                    item = new DBChapters();//没查询到或者已经被删除过都置为new
                                }
                                item.setId(Long.parseLong(mComic.getId()+""+mapping.getKey()));
                                item.setChapters_title(mComic.getChapters().get(mapping.getKey()));
                                item.setComic_id(mComic.getId());
                                item.setChapters(mapping.getKey());
                                item.setState(DownState.NONE);
                                try{
                                    //把数据先存入数据库
                                    mHelper.insert(item);
                                }catch (SQLiteConstraintException exception){
                                    LogUtil.e("插入下载列表失败，更新数据库");
                                    //已经存在，所以更新一下状态state
                                    mHelper.update(item);
                                }
                            }
                        }
                    }
                    List<DBChapters> results = mHelper.queryDownloadItems(mComic.getId());
                    //修改漫画下载总数
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

    public void download(final DBChapters info, final int page, Observer observer) {
        if(page>=info.getComiclist().size())//防止数组越界
            return;
        comicService.download("bytes=0" + "-", info.getComiclist().get(page))
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Object>() {
                    @Override
                    public Object apply(@NonNull ResponseBody responseBody) throws Exception {
                        //把图片保存到SD卡
                        FileUtil.saveImgToSdCard(responseBody.byteStream(), FileUtil.SDPATH + FileUtil.COMIC + info.getComic_id() + "/" + info.getChapters()+"/",page+".png");
                        ArrayList<String> paths =  new ArrayList<>();
                        if(info.getChapters_path()!=null){
                            paths =new ArrayList<>(info.getChapters_path());
                        }
                        paths.add(FileUtil.SDPATH + FileUtil.COMIC + info.getComic_id() + "/"+ info.getChapters()+"/"+page+".png");
                        //保存存储位置
                        info.setChapters_path(paths);
                        return info;
                    }
                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(observer);
    }



    public void updateDownloadItemsList(final List<DBChapters> mLists, Observer observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    boolean result = true;
                    for(int i=0;i<mLists.size();i++){
                        if (mLists.get(i).getState() != DownState.FINISH){
                            mLists.get(i).setState(DownState.NONE);
                        }
                        result = mHelper.insertList(mLists);
                    }
                    observableEmitter.onNext(result);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void deleteHistoryComic(final List<Comic> mLists, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try{
                    for(int i=0;i<mLists.size();i++){
                        Comic items = mLists.get(i);
                        items.setClickTime(0);
                        items.setCurrent_page(0);
                        items.setCurrentChapter(0);
                        mHelper.update(items);
                    }
                    List<Comic> mComics = mHelper.queryHistory(0);
                    observableEmitter.onNext(mComics);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void deleteHistoryComic(Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try{
                    List<Comic> mLists = mHelper.queryHistory();
                    for(int i=0;i<mLists.size();i++){
                        mLists.get(i).setClickTime(0);
                        mLists.get(i).setCurrent_page(0);
                        mLists.get(i).setCurrentChapter(0);
                    }
                    mHelper.insertList(mLists);
                    List<Comic> mComics = mHelper.queryHistory(0);
                    observableEmitter.onNext(mComics);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void deleteCollectComic(final List<Comic> mLists, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    for (int i = 0; i < mLists.size(); i++) {
                        Comic items = mLists.get(i);
                        items.setIsCollected(false);
                        mHelper.update(items);
                    }
                    List<Comic> mComics = mHelper.queryCollect();
                    observableEmitter.onNext(mComics);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                } finally {
                    observableEmitter.onComplete();
                }
            }

        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void deleteDownloadComic(final List<Comic> mLists, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    for (int i = 0; i < mLists.size(); i++) {
                        mLists.get(i).setStateInte(-1);
                        FileUtil.deleteDir(FileUtil.SDPATH + FileUtil.COMIC + mLists.get(i).getId());
                    }
                    mHelper.insertList(mLists);
                    List<Comic> mComics = mHelper.queryDownloadComic();
                    observableEmitter.onNext(mComics);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                } finally {
                    observableEmitter.onComplete();
                }
            }

        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void deleteDownloadItem(final List<DBChapters> mLists, final Comic comic , Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<DBChapters>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DBChapters>> observableEmitter) throws Exception {
                try {
                    for (int i = 0; i < mLists.size(); i++) {
                        mLists.get(i).setStateInte(-1);
                        mLists.get(i).setChapters_path(new ArrayList<String>());
                        FileUtil.deleteDir(FileUtil.SDPATH + FileUtil.COMIC + comic.getId() + "/" + mLists.get(i).getChapters());
                    }
                    mHelper.insertList(mLists);
                    List<DBChapters> Items = mHelper.queryDownloadItems(comic.getId());
                    observableEmitter.onNext(Items);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                } finally {
                    observableEmitter.onComplete();
                }
            }

        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getRankList(final long currenttime, final String type, final int page, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentRankUrl+"t="+currenttime+"&type="+type+"&page="+page+"&pageSize=10&style=items").get();
                    List<Comic> mdats = TencentComicAnalysis.TransToRank(doc);
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

    public void clearCache(Observer observer) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> observableEmitter) throws Exception {
                try {
                    //FileUtil.deleteDir(FileUtil.SDPATH + FileUtil.CACHE);//首先清除API缓存
                    GlideCacheUtil.getInstance().clearImageAllCache(rxAppCompatActivity);
                    observableEmitter.onNext(GlideCacheUtil.getInstance().getCacheSize(rxAppCompatActivity));
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

    public void getCategroyList(final Map<String, Integer> mSelectMap, final int page, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    String theme= "";
                    String audience = "";
                    String finish = "";
                    String nation = "";
                    if(mSelectMap.get("theme")!=0){
                        theme = "/theme/"+mSelectMap.get("theme");
                    }
                    if(mSelectMap.get("audience")!=0){
                        audience = "/audience/"+mSelectMap.get("audience");
                    }
                    if(mSelectMap.get("finish")!=0){
                        finish = "/finish/"+mSelectMap.get("finish");
                    }
                    if(mSelectMap.get("nation")!=0){
                        nation = "/nation/"+mSelectMap.get("nation");
                    }
                    String url = Url.TencentCategoryUrlHead+audience+theme+finish+Url.TencentCategoryUrlMiddle+nation+Url.TencentCategoryUrlFoot+page;
                    Document doc = Jsoup.connect(url).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToComic(doc);
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

    public void getNewComicList( final int page, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentNewUrl+page).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToNewListComic(doc);
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

    public void register(final String usernameText, final String passwordText, final String describeText, Observer observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try {
                    Thread.sleep(2000);
                    if(Hawk.get(usernameText,null) != null){
                        observableEmitter.onError(new IllegalArgumentException());
                        return;
                    }
                    Hawk.put(usernameText,passwordText);
                    Hawk.put(usernameText + "des",describeText);
                    observableEmitter.onNext(true);
                    Thread.sleep(1000);
                    observableEmitter.onComplete();
                }catch (Exception e){
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {

                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void login(final String usernameText, final String passwordText, Observer observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try {
                    Thread.sleep(2000);
                    String password = Hawk.get(usernameText);
                    if(password.equals(passwordText)){
                        Hawk.put("isLogin",usernameText);
                        observableEmitter.onNext(true);
                    }else{
                        observableEmitter.onNext(false);
                    }
                    Thread.sleep(1000);
                    observableEmitter.onComplete();
                }catch (Exception e){
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {

                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }
}
