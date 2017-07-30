package com.android.zhhr.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.custom.IndexItemView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 皓然 on 2017/7/9.
 */

public class TencentComicAnalysis {
    //处理漫画列表
    public static List<Comic> TransToComic(Document doc){
        List<Comic> mdats = new ArrayList<Comic>();
        List<Element> detail = doc.getElementsByAttributeValue("class","ret-works-cover");
        List<Element> infos = doc.getElementsByAttributeValue("class","ret-works-info");
        for(int i=0;i<detail.size();i++){
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            comic.setId(getID(infos.get(i).select("a").attr("href")));
            mdats.add(comic);
        }
        return mdats;
    }

    public static Comic TransToComicDetail(Document doc, final Context context){
        //设置标题
        final Comic comic = new Comic();
        comic.setTitle(doc.title().split("-")[0]);
        //设置标签
        Element ElementDescription = doc.getElementsByAttributeValue("name","Description").get(0);
        String descriptions = ElementDescription.select("meta").attr("content");
        String mdescriptions[] = descriptions.split("：");
        List<String> tags = new ArrayList<>();
        String mtags[] = mdescriptions[mdescriptions.length-1].split(",");
        for(int i=0;i<mtags.length;i++){
            tags.add(mtags[i]);
        }
        comic.setTags(tags);
        Element detail = doc.getElementsByAttributeValue("class","works-cover ui-left").get(0);
        comic.setCover(detail.select("img").attr("src"));
        //设置作者
        Element author = doc.getElementsByAttributeValue("class"," works-author-name").get(0);
        comic.setAuthor(author.select("a").attr("title"));
        //设置收藏数
        Element collect = doc.getElementsByAttributeValue("id","coll_count").get(0);
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String collection=decimalFormat.format(Float.parseFloat(collect.text())/10000);//format 返回的是字符串
        comic.setCollections("("+collection+")万");
        //设置章节数
        Element DivChapter = doc.getElementsByAttributeValue("class","chapter-page-all works-chapter-list").get(0);
        List<Element> ElementChapters = DivChapter.getElementsByAttributeValue("target","_blank");
        final List<String> chapters = new ArrayList<>();
        for(int i=0;i<ElementChapters.size();i++){
            chapters.add(ElementChapters.get(i).select("a").text());
        }
        comic.setChapters(chapters);

        Element ElementDescribe = doc.getElementsByAttributeValue("class","works-intro-short ui-text-gray9").get(0);
        comic.setDescribe(ElementDescribe.select("p").text());


        Element ElementPopularity = doc.getElementsByAttributeValue("class"," works-intro-digi").get(0);
        comic.setPopularity(ElementPopularity.select("em").get(1).text());
        //设置状态
        String status = detail.select("label").get(0).text();
        //设置更新日期
        if(status.equals("已完结")){
            comic.setStatus("已完结");
            comic.setUpdates("全"+ElementChapters.size()+"话");
        }else {
            Element ElementUpdate = doc.getElementsByAttributeValue("class"," ui-pl10 ui-text-gray6").get(0);
            String updates = ElementUpdate.select("span").get(0).text();
            comic.setUpdates(updates);
            comic.setStatus("更新最新话");
        }

        Element ElementPoint = doc.getElementsByAttributeValue("class","ui-text-orange").get(0);
        comic.setPoint(ElementPoint.select("strong").get(0).text());

        return comic;
    }

    public static String getID(String splitID){
        String[] ids = splitID.split("/");
        return ids[ids.length-1];
    }
}
