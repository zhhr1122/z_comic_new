package com.android.zhhr.net;

import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.data.entity.Chapters;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 皓然 on 2017/7/4.
 */

public interface ComicService {
    @GET("top250")
    Observable<Chapters> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("getChapterList/{id}/{chapter}")
    Observable<Chapters> getChapters(@Path("id") String id, @Path("chapter") String chapter);

    @GET("getPreNowChapterList/{id}/{chapter}")
    Observable<PreloadChapters> getPreNowChapterList(@Path("id") String id, @Path("chapter") String chapter);
}
