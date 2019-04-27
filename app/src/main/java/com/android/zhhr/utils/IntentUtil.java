package com.android.zhhr.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.activity.CategoryActivity;
import com.android.zhhr.ui.activity.ComicChapterActivity;
import com.android.zhhr.ui.activity.ComicDetaiActivity;
import com.android.zhhr.ui.activity.DownloadChapterlistActivity;
import com.android.zhhr.ui.activity.IndexActivity;
import com.android.zhhr.ui.activity.LoginActivity;
import com.android.zhhr.ui.activity.MainActivity;
import com.android.zhhr.ui.activity.NewListActivity;
import com.android.zhhr.ui.activity.RankActivity;
import com.android.zhhr.ui.activity.RegisterActivity;
import com.android.zhhr.ui.activity.SearchActivity;
import com.android.zhhr.ui.activity.SelectDownloadActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 皓然 on 2017/7/12.
 * intent跳转类
 */

public class IntentUtil {
    public static void ToMainActivity(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    public static void ToComicDetail(Activity context, String id,String title){
        Intent intent = new Intent(context, ComicDetaiActivity.class);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_TITLE,title);
        context.startActivity(intent);
    }

    public static void ToComicDetail(Activity context, String id,String title,int type){
        Intent intent = new Intent(context, ComicDetaiActivity.class);
        intent.putExtra(Constants.COMIC_FROM,type);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_TITLE,title);
        context.startActivity(intent);
    }


    public static void ToComicChapter(Activity context, int chapters,Comic mComic){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC,mComic);
        context.startActivity(intent);
    }

    public static void ToComicChapter(Context context, int chapters,Comic mComic){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC,mComic);
        context.startActivity(intent);
    }

    public static void ToComicChapterForResult(Activity context, int chapters, Comic mComic){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC,mComic);
        context.startActivityForResult(intent,1);
    }


    public static void ToComicChapter(Activity context, int chapters,long id,String title,List<String> chapter_titles,int type){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_TITLE,title);
        intent.putExtra(Constants.COMIC_READ_TYPE,type);
        intent.putStringArrayListExtra(Constants.COMIC_CHAPER_TITLE,  new ArrayList<>(chapter_titles));
        context.startActivity(intent);
    }

    public static void ToIndex(Activity context, Comic mComic){
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra(Constants.COMIC,mComic);
        context.startActivity(intent);
    }

    public static void ToSelectDownload(Activity context, Comic mComic){
        Intent intent = new Intent(context, SelectDownloadActivity.class);
        intent.putExtra(Constants.COMIC,mComic);
        context.startActivity(intent);
    }

    public static void ToSearch(Activity context){
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public static void ToDownloadListActivity(Activity context,HashMap<Integer,Integer> map,Comic comic) {
        Intent intent = new Intent(context, DownloadChapterlistActivity.class);
        intent.putExtra(Constants.COMIC_SELECT_DOWNLOAD,map);
        intent.putExtra(Constants.COMIC,comic);
        context.startActivity(intent);
    }

    public static void toRankActivity(Activity context) {
        Intent intent = new Intent(context, RankActivity.class);
        context.startActivity(intent);
    }


    public static void toUrl(Activity context,String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void toQQchat(Activity context,String number) {
        String url = "mqqwpa://im/chat?chat_type=wpa&uin="+number;//uin是发送过去的qq号码
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void toCategoryActivity(Activity context) {
        Intent intent = new Intent(context, CategoryActivity.class);
        context.startActivity(intent);
    }

    public static void toCategoryActivity(Activity context,String type,int value) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(type,value);
        context.startActivity(intent);
    }

    public static void toNewActivity(Activity context) {
        Intent intent = new Intent(context, NewListActivity.class);
        context.startActivity(intent);
    }

    public static void toLoginActivity(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void toRegisterActivity(Activity context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
