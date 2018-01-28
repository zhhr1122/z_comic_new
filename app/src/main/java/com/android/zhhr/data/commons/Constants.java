package com.android.zhhr.data.commons;

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
    public final static String COMIC = "comic";
    public final static int RIGHT_TO_LEFT =0;
    public final static int LEFT_TO_RIGHT =1;
    public final static String DB_NAME = "comic";
    /**
     * 表示当前章节的状态
     * 0 未选取 1 已选择 2 已下载
     */
    public final static int CHAPTER_FREE = 0;
    public final static int CHAPTER_SELECTED= 1;
    public final static int CHAPTER_DOWNLOAD = 2;
    /**
     * 表示主页标题的种类
     *  0  热门推荐 1 排行榜
     */
    public final static int TYPE_RECOMMEND = 0;
    public final static int TYPE_RANK_LIST = 1;
    public final static int TYPE_HOT_SERIAL = 2;
    public final static int TYPE_HOT_JAPAN = 3;
    /**
     * 控制广告的开关
     */
    public final static boolean isAD = false;


}
