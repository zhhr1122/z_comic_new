package com.android.zhhr.net.cache;

import android.os.Environment;

import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.PermissionUtils;

import java.io.File;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;


/**
 * Created by 张皓然 on 2018/2/9.
 */

public class CacheProviders {
    private static ComicCacheProviders comicCacheProviders;

    public synchronized static ComicCacheProviders getComicCacheInstance() {
        if (comicCacheProviders == null) {
            File cacheDirectory = new File(FileUtil.SDPATH+FileUtil.CACHE);
            if(!cacheDirectory.exists()){
                FileUtil.createDir(cacheDirectory.getAbsolutePath());
            }
            comicCacheProviders = new RxCache.Builder()
                    .persistence(cacheDirectory, new GsonSpeaker())//缓存文件的配置、数据的解析配置
                    .using(ComicCacheProviders.class);//这些配置对应的缓存接口
        }
        return comicCacheProviders;
    }
}
