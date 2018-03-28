package com.android.zhhr.data.commons;

import retrofit2.http.PUT;

/**
 * 一些基本的常量定义
 * Created by 皓然 on 2017/7/13.
 */

public class Constants {
    public final static String COMIC_ID ="comic_id";
    public final static String COMIC_CHAPERS ="comic_chapter";
    public final static String COMIC_TITLE = "comic_title";
    public final static String COMIC_CHAPER_TITLE = "comic_chapter_title";
    public final static String COMIC_READ_TYPE = "comic_read_type";
    public final static String COMIC_SELECT_DOWNLOAD = "comic_select_download";
    public final static String COMIC = "comic";
    public final static String COMIC_FROM = "comic_from";
    public final static int RIGHT_TO_LEFT =0;
    public final static int LEFT_TO_RIGHT =1;
    public final static int UP_TO_DOWN = 2;
    public final static String DB_NAME = "comic.db";
    /**
     * 表示当前章节的状态
     * 0 未选取 1 已选择 2 已下载
     */
    public final static int CHAPTER_FREE = 0;
    public final static int CHAPTER_SELECTED= 1;
    public final static int CHAPTER_DOWNLOAD = 2;
    public final static int CHAPTER_DOWNLOADING = 3;
    /**
     * 表示主页标题的种类
     *  0  热门推荐 1 排行榜
     */
    public final static int TYPE_RECOMMEND = 0;
    public final static int TYPE_RANK_LIST = 1;
    public final static int TYPE_HOT_SERIAL = 2;
    public final static int TYPE_HOT_JAPAN = 3;
    public final static int TYPE_BOY_RANK = 4;
    public final static int TYPE_GIRL_RANK = 5;
    /**
     * 控制广告的开关
     */
    public final static boolean isAD = false;


    public static final int OK = 1;

    public static final String MODEL = "model";

    public static final boolean DEFAULT_MODEL = false;

    public static final boolean NIGHT_MODEL = true;

    public final static String CATEGORY_TITLE_THEME = "theme";
    public final static String CATEGORY_TITLE_FINISH = "finish";
    public final static String CATEGORY_TITLE_AUDIENCE = "audience";
    public final static String CATEGORY_TITLE_NATION = "nation";

    /**
     * 漫画来源 腾讯 0 酷酷 1
     */

    public final static int FROM_TENCENT = 0;

    public final static int FROM_KUKU = 1;


    public static boolean isNeedKuku = true;

    public final static String LAST_START_TIME = "last_start_time";
    public final static int CACHE_DAYS = 1;
}
