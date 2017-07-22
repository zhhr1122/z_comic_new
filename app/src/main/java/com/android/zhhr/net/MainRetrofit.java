package com.android.zhhr.net;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 皓然 on 2017/7/5.
 */

class MainRetrofit {
    public ComicService comicService;

    public static final String BASE_URL = "http://101.200.60.112:5000";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    MainRetrofit() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        comicService = retrofit.create(ComicService.class);
    }

    public ComicService getService(){
        return comicService;
    }
}
