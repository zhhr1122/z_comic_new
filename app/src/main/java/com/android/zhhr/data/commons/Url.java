package com.android.zhhr.data.commons;

/**
 * Created by 皓然 on 2016/12/4.
 */
public class Url {
    //抓取腾讯漫画TOP
    public static String TencentTopUrl = "http://ac.qq.com/Comic/all/state/pink/search/hot/page/";

    public static String TencentHomePage = "http://ac.qq.com/";

    public static String TencentJapanHot = "http://ac.qq.com/Comic/all/state/pink/nation/4/search/hot/page/1";

    public static String TencentBannerJanpan = "http://ac.qq.com/Jump";

    public static String TencentBanner = "http://m.ac.qq.com";

    //抓取腾讯漫画更新时间TOP
    public static String TencentUpdateTimeUrl = "http://ac.qq.com/Comic/index/state/pink/";

    //抓取腾讯漫画详情页面
    public static String TencentDetail = "http://ac.qq.com/Comic/comicInfo/id/";
    //抓取详细的漫画阅读界面 2018/1/30更换为滴滴云
    public static String TencentComicChapters ="http://116.85.46.229:5000";
    //更换为阿里云
    public static String TencentComicChapters1 = "http://chengmboy.xin:5001";
    //腾讯漫画搜索接口
    public static String TencentSearchBase = "http://m.ac.qq.com/search/";

    public static String TencentSearchUrl  = TencentSearchBase + "smart?word=";

    public static String TencentSearchResultUrl = TencentSearchBase+"result?word=";

    public static String TencentSearchRecommend = TencentSearchBase+"index";

}

