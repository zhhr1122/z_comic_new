package com.android.zhhr.net;

import com.android.zhhr.data.entity.Subject;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 皓然 on 2017/7/4.
 */

public interface ComicService {
    @GET("top250")
    Observable<Subject> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("getChapterList/{id}/{chapter}")
    Observable<Subject> getChapters(@Path("id") String id,@Path("chapter") String chapter);
}
