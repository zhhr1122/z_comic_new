package com.android.zhhr.net;

/**
 * Created by 皓然 on 2017/7/4.
 */

public class MainFactory {

    public static ComicService comicService;
    protected static final Object monitor = new Object();

    public static ComicService getComicServiceInstance(){
        synchronized (monitor){
            if(comicService==null){
                comicService = new MainRetrofit().getService();
            }
            return comicService;
        }
    }
}
