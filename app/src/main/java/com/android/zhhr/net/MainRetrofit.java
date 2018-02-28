package com.android.zhhr.net;


import android.os.Looper;

import com.android.zhhr.data.commons.Url;
import com.android.zhhr.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 皓然 on 2017/7/5.
 */

class MainRetrofit {
    public ComicService comicService;

    public static final String BASE_URL = Url.TencentComicChapters1;

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    MainRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response orginalResponse = chain.proceed(chain.request());
                        return orginalResponse.newBuilder()
                                .body(new ProgressResponseBody(orginalResponse.body(), new ProgressListener() {
                                    @Override
                                    public void onProgress(long progress, long total, boolean done) {
                                        LogUtil.d( "onProgress: " + "total ---->" + total + "done ---->" + progress );
                                    }
                                }))
                                .build();
                    }
                })
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        /*OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);*/

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        comicService = retrofit.create(ComicService.class);
    }

    public ComicService getService(){
        return comicService;
    }
}
