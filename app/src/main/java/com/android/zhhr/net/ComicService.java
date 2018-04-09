package com.android.zhhr.net;

import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.data.entity.db.DBChapters;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 皓然 on 2017/7/4.
 */

public interface ComicService{

    @GET("getChapterList/{id}/{chapter}")
    Observable<DBChapters> getChapters(@Path("id") String id, @Path("chapter") String chapter);

    @GET("getPreNowChapterList/{id}/{chapter}")
    Observable<PreloadChapters> getPreNowChapterList(@Path("id") String id, @Path("chapter") String chapter);
    @GET
    Observable<HttpResult<List<SearchBean>>> getDynamicSearchResult(@Url String url);

    @GET("getKuKuChapterList/{id}/{chapter}")
    Observable<DBChapters> getKuKuChapterList(@Path("id") String id, @Path("chapter") String chapter);


    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);
}
