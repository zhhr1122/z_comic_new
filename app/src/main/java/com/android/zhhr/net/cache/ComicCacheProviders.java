package com.android.zhhr.net.cache;

import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.data.entity.SearchBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

/**
 * Created by 张皓然 on 2018/2/9.
 */

public interface ComicCacheProviders {

    @LifeCache(duration = 1,timeUnit = TimeUnit.DAYS)
    Observable<PreloadChapters> getPreNowChapterList(Observable<PreloadChapters> comics, DynamicKey key, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 1,timeUnit = TimeUnit.DAYS)
    Observable<Chapters> getChapters(Observable<Chapters> comics, DynamicKey key, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 1,timeUnit = TimeUnit.DAYS)
    Observable<HttpResult<List<SearchBean>>> getDynamicSearchResult(Observable<HttpResult<List<SearchBean>>> Result, DynamicKey key, EvictDynamicKey evictDynamicKey);
}
