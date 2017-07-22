package com.android.zhhr.utils;

import android.content.Context;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.ui.activity.ComicChapterActivity;
import com.android.zhhr.ui.activity.ComicDetaiActivity;

/**
 * Created by 皓然 on 2017/7/12.
 * intent跳转类
 */

public class IntentUtil {
    public static void ToComicDetail(Context context, String id){
        Intent intent = new Intent(context, ComicDetaiActivity.class);
        intent.putExtra(Constants.COMIC_ID,id);
        context.startActivity(intent);
    }

    public static void ToComicChapter(Context context, String id,String chapters,String ChapterTitle){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC_CHAPER_TITLE,ChapterTitle);
        context.startActivity(intent);
    }
}
