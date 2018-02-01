package com.android.zhhr.net;

import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.data.entity.SearchResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by 皓然 on 2017/7/4.
 */

public interface ComicService{

    @GET("getChapterList/{id}/{chapter}")
    Observable<Chapters> getChapters(@Path("id") String id, @Path("chapter") String chapter);

    @GET("getPreNowChapterList/{id}/{chapter}")
    Observable<PreloadChapters> getPreNowChapterList(@Path("id") String id, @Path("chapter") String chapter);
    @GET
    Observable<SearchResult> getDynamicSearchResult(@Url String url);
}
