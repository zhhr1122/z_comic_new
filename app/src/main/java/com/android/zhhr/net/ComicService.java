package com.android.zhhr.net;

import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.data.entity.SearchBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by 皓然 on 2017/7/4.
 */

public interface ComicService{

    @GET("getChapterList/{id}/{chapter}")
    Observable<Chapters> getChapters(@Path("id") String id, @Path("chapter") String chapter);

    @GET("getPreNowChapterList/{id}/{chapter}")
    Observable<PreloadChapters> getPreNowChapterList(@Path("id") String id, @Path("chapter") String chapter);
    @GET
    Observable<HttpResult<List<SearchBean>>> getDynamicSearchResult(@Url String url);
}
