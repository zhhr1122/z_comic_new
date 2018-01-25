package com.android.zhhr.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.activity.ComicChapterActivity;
import com.android.zhhr.ui.activity.ComicDetaiActivity;
import com.android.zhhr.ui.activity.IndexActivity;
import com.android.zhhr.ui.activity.SelectDownloadActivity;
import com.zonst.libzadsdk.ZAdComponent;
import com.zonst.libzadsdk.ZAdSdk;
import com.zonst.libzadsdk.ZAdType;
import com.zonst.libzadsdk.listener.ZAdDisplayListener;
import com.zonst.libzadsdk.listener.ZAdLoadListener;
import com.zonst.libzadsdk.listener.ZAdRewardListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 皓然 on 2017/7/12.
 * intent跳转类
 */

public class IntentUtil {
    public static void ToComicDetail(Context context, String id,String title){
        Intent intent = new Intent(context, ComicDetaiActivity.class);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_TITLE,title);
        context.startActivity(intent);
    }

    public static void ToComicChapter(Context context, int chapters,Comic mComic){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC_ID,mComic.getId());
        intent.putExtra(Constants.COMIC_TITLE,mComic.getTitle());
        intent.putExtra(Constants.COMIC_READ_TYPE,mComic.getReadType());
        intent.putStringArrayListExtra(Constants.COMIC_CHAPER_TITLE, (ArrayList<String>) mComic.getChapters());
        context.startActivity(intent);
    }

    public static void ToComicChapter(Context context, int chapters,long id,String title,List<String> chapter_titles,int type){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_TITLE,title);
        intent.putExtra(Constants.COMIC_READ_TYPE,type);
        intent.putStringArrayListExtra(Constants.COMIC_CHAPER_TITLE, (ArrayList<String>) chapter_titles);
        context.startActivity(intent);
    }

    public static void ToIndex(Context context, long id,List<String> ChapterTitles,String title,int type){
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_TITLE,title);
        intent.putExtra(Constants.COMIC_READ_TYPE,type);
        intent.putStringArrayListExtra(Constants.COMIC_CHAPER_TITLE, (ArrayList<String>) ChapterTitles);
        context.startActivity(intent);
    }

    public static void ToSelectDownload(Context context, Comic mComic){
        Intent intent = new Intent(context, SelectDownloadActivity.class);
        intent.putExtra(Constants.COMIC_ID,mComic.getId());
        intent.putStringArrayListExtra(Constants.COMIC_CHAPER_TITLE, (ArrayList<String>) mComic.getChapters());
        context.startActivity(intent);
    }

    public static void getRewardVideoAd(Context context, ZAdRewardListener listener){
        Log.d("zhhr1122","getVideoAd start");
        ZAdComponent ad = ZAdSdk.getInstance().getAd(ZAdType.VIDEO_REWARD, "1002");
        ad.setRewardListener(listener);
        ZAdSdk.getInstance().getLoader().loadAd(context, ad, false);
    }
}
