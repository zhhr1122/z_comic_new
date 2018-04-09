package com.android.zhhr.utils;

import android.content.Context;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhhr on 2018/3/20.
 */

public class KukuComicAnalysis {

    public static List<Comic> TransToSearchResultComic_Kuku(Document doc){
        List<Comic> mdats = new ArrayList<>();
        List<Element> detail = doc.select("dd");
        for(int i=0;i<detail.size();i++){
            Comic comic = new Comic();
            comic.setFrom(Constants.FROM_KUKU);
            String title = detail.get(i).select("a").get(1).text();
            comic.setTitle(title.substring(0,title.length()-4));
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setId(Long.parseLong(getID(detail.get(i).select("a").get(0).attr("href")))*1000000);
            /*List<Element> info = detail.get(i).select("small");
            comic.setUpdates(info.get(0).text());
            String descriptions = info.get(1).text();
            String mdescriptions[] = descriptions.split("：");
            List<String> tags = new ArrayList<>();
            for(int j=0;j<mdescriptions.length;j++){
                tags.add(mdescriptions[j]);
            }
            comic.setTags(tags);
            comic.setAuthor(info.get(2).text());*/
            List<String> tags = new ArrayList<>();
            tags.add("酷酷漫画");
            comic.setTags(tags);
            comic.setAuthor(title.substring(0,title.length()-4));
            mdats.add(comic);
        }
        return mdats;
    }


    /**
     * 漫画详情
     * @param doc
     * @return
     */
    public static Comic TransToComicDetail(Document doc){
        //设置标题
        final Comic comic = new Comic();
        try{

            Element detail = doc.select("table").get(8);
            String title = doc.getElementsByAttributeValue("name","description").get(0).attr("content");
            comic.setTitle(title.substring(0,title.length()-7));

            //设置标签
            List<String> tags = new ArrayList<>();
            tags.add("热血");
            comic.setTags(tags);

            //设置图片
            comic.setCover(detail.select("img").attr("src"));
            //设置作者
            String infos[]= detail.getElementsByAttributeValue("align","center").get(6).text().split(" | ");
            comic.setAuthor(infos[0].split("：")[1]);
            //设置收藏数
            comic.setCollections("未知");
            //设置章节数
            List<Element> ElementChapters = detail.getElementsByAttributeValue("id","comiclistn").get(0).select("dd");
            List<String> chapters = new ArrayList<>();
            List<String> chapters_url = new ArrayList<>();
            for(int i=0;i<ElementChapters.size();i++){
                chapters.add(ElementChapters.get(i).select("a").get(0).text());
                String url = ElementChapters.get(i).select("a").get(0).attr("href");
                chapters_url.add(url.substring(0,url.length()-5));
            }
            comic.setChapters(chapters);
            comic.setChapters_url(chapters_url);

            Element ElementDescribe = detail.getElementsByAttributeValue("id","ComicInfo").get(0);
            comic.setDescribe(ElementDescribe.text());


            comic.setPopularity("未知");
            //设置状态
            String status = infos[2].split("：")[1];
            //设置更新日期
            if(status.equals("完结")){
                comic.setStatus("已完结");
                comic.setUpdates("全"+chapters.size()+"话");
            }else {
                comic.setUpdates(infos[4].split("：")[1]);
                comic.setStatus("更新最新话");
            }

            comic.setPoint("暂无评分");
            //设置阅读方式
            comic.setReadType(Constants.RIGHT_TO_LEFT);
            comic.setState(DownState.START);
        }catch (Exception e){

        }finally {
            return comic;
        }
    }


    /**
     * 获取漫画ID
     * @param splitID
     * @return
     */
    public static String getID(String splitID){
        String[] ids = splitID.split("/");
        return ids[ids.length-1];
    }

}
