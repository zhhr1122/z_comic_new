package com.android.zhhr.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.FullHomeItem;
import com.android.zhhr.data.entity.LargeHomeItem;
import com.android.zhhr.data.entity.SmallHomeItem;
import com.android.zhhr.ui.custom.IndexItemView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 皓然 on 2017/7/9.
 */

public class TencentComicAnalysis {
    /**
     * banner的获取
     * @param doc
     * @return
     */
    public static List<Comic> TransToBanner(Document doc){
        List<Comic> mdats = new ArrayList<>();
        Element detail = doc.getElementsByAttributeValue("class","banner-list").get(0);
        List<Element> infos = detail.getElementsByTag("a");
        for(int i=0;i<infos.size();i++){
            Comic comic = new Comic();
            comic.setTitle(infos.get(i).select("a").attr("title"));
            comic.setCover(infos.get(i).select("img").attr("src"));
            try{
                comic.setId(Long.parseLong(getID(infos.get(i).select("a").attr("href"))));
                mdats.add(comic);
            }catch (Exception e){
            }
        }
        return mdats;
    }


    /**
     * 日漫首页
     * @param doc
     * @return
     */
    public static List<Comic> TransToBannerJapan(Document doc){
        List<Comic> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class","comic-text");
        Random random =new Random();
        int result = random.nextInt(3);
        for(int i=(result*4);i<(result+1)*4;i++){
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setId(Long.parseLong(getID(detail.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }
    //处理漫画列表
    public static List<Comic> TransToComic(Document doc){
        List<Comic> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class","ret-works-cover");
        List<Element> infos = doc.getElementsByAttributeValue("class","ret-works-info");
        for(int i=0;i<detail.size();i++){
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            comic.setId(Long.parseLong(getID(infos.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    //处理漫画列表
    public static List<Comic> TransToNewListComic(Document doc){
        List<Comic> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class","ret-works-cover");
        List<Element> infos = doc.getElementsByAttributeValue("class","ret-works-info");
        for(int i=0;i<detail.size();i++){
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            Element info = infos.get(i).getElementsByAttributeValue("class","ret-works-tags").get(0);
            comic.setAuthor(infos.get(i).getElementsByAttributeValue("class","ret-works-author").get(0).attr("title"));
            comic.setUpdates(info.select("em").text());
            comic.setDescribe(info.select("span").text());
            comic.setId(Long.parseLong(getID(infos.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    public static List<Comic> TransToSearchResultComic(Document doc){
        List<Comic> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class","comic-item");
        for(int i=0;i<detail.size();i++){
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("strong").text());
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setId(Long.parseLong(getID(detail.get(i).select("a").attr("href"))));
            comic.setFrom(Constants.FROM_TENCENT);
            List<Element> info = detail.get(i).select("small");
            comic.setUpdates(info.get(0).text());
            String descriptions = info.get(1).text();
            String mdescriptions[] = descriptions.split("：");
            List<String> tags = new ArrayList<>();
            for(int j=0;j<mdescriptions.length;j++){
                tags.add(mdescriptions[j]);
            }
            comic.setTags(tags);
            comic.setAuthor(info.get(2).text());
            mdats.add(comic);
        }
        return mdats;
    }

    public static List<Comic> TransToSearchTopComic(Document doc){
        List<Comic> mdats = new ArrayList<>();
        Element detail = doc.getElementsByAttributeValue("class","search-hot-list").get(0);
        List<Element> details = detail.select("a");
        for(int i=0;i<details.size();i++){
            Comic comic = new Comic();
            comic.setTitle(details.get(i).text());
            comic.setId(Long.parseLong(getID(details.get(i).attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    //处理漫画列表
    public static List<FullHomeItem> TransToJapanComic(Document doc){
        List<FullHomeItem> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class","ret-works-cover");
        List<Element> infos = doc.getElementsByAttributeValue("class","ret-works-info");
        for(int i=0;i<3;i++){
            FullHomeItem comic = new FullHomeItem();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            comic.setAuthor(infos.get(i).select("p").attr("title"));
            Element ElementDescribe = infos.get(i).getElementsByAttributeValue("class","ret-works-decs").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(infos.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    /**
     * 强推漫画
     * @param doc
     * @return
     */
    public static List<LargeHomeItem> TransToRecommendComic(Document doc){
        List<LargeHomeItem> mdats = new ArrayList<LargeHomeItem>();
        List<Element> detail = doc.getElementsByAttributeValue("class","in-anishe-text");
        Random random =new Random();
        int result = random.nextInt(5);
        for(int i=(result*6);i<(result+1)*6;i++){
            LargeHomeItem comic = new LargeHomeItem();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            Element ElementDescribe = detail.get(i).getElementsByAttributeValue("class","mod-cover-list-intro").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(detail.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    /**
     * 男生榜
     * @param doc
     * @return
     */
    public static List<SmallHomeItem> TransToBoysComic(Document doc){
        List<SmallHomeItem> mdats = new ArrayList<SmallHomeItem>();
        Random random =new Random();
        Element detail = doc.getElementsByAttributeValue("class","in-teen-list mod-cover-list clearfix").get(0);
        List<Element> boys = detail.getElementsByTag("li");
        for(int i=0;i<boys.size();i++){
            SmallHomeItem comic = new SmallHomeItem();
            comic.setTitle(boys.get(i).select("img").attr("alt"));
            comic.setCover(boys.get(i).select("img").attr("data-original"));
            Element ElementDescribe = boys.get(i).getElementsByAttributeValue("class","mod-cover-list-intro").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(boys.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }


    /**
     * 女生榜
     * @param doc
     * @return
     */
    public static List<SmallHomeItem> TransToGirlsComic(Document doc){
        List<SmallHomeItem> mdats = new ArrayList<SmallHomeItem>();
        Element detail = doc.getElementsByAttributeValue("class","in-girl-list mod-cover-list clearfix").get(0);
        List<Element> girls = detail.getElementsByTag("li");
        for(int i=0;i<girls.size();i++){
            SmallHomeItem comic = new SmallHomeItem();
            comic.setTitle(girls.get(i).select("img").attr("alt"));
            comic.setCover(girls.get(i).select("img").attr("data-original"));
            Element ElementDescribe = girls.get(i).getElementsByAttributeValue("class","mod-cover-list-intro").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(girls.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    /**
     * 热门连载
     * @param doc
     * @return
     */
    public static List<LargeHomeItem> TransToNewComic(Document doc){
        List<LargeHomeItem> mdats = new ArrayList<LargeHomeItem>();
        Random random =new Random();
        int result = random.nextInt(7);
        Element detail = doc.getElementsByAttributeValue("class","in-anishe-list clearfix in-anishe-ul").get(result);
        List<Element> hots = detail.getElementsByTag("li");
        for(int i=0;i<4;i++){
            LargeHomeItem comic = new LargeHomeItem();
            comic.setTitle(hots.get(i).select("img").attr("alt"));
            comic.setCover(hots.get(i).select("img").attr("data-original"));
            Element ElementDescribe = hots.get(i).getElementsByAttributeValue("class","mod-cover-list-intro").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(hots.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    public static List<Comic> TransToRank(Document doc) {
        List<Comic> mComic = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class","comic-link");
        for(int i=0;i<detail.size();i++){
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("strong").text());
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setId(Long.parseLong(getID(detail.get(i).attr("href"))));
            comic.setUpdates(detail.get(i).getElementsByAttributeValue("class","comic-update").get(0).text());
            List<String> taglist = new ArrayList<>();
            try{
                Element ElementDescribe = detail.get(i).getElementsByAttributeValue("class","comic-desc").get(0);
                comic.setDescribe(ElementDescribe.text());
                String stringtags = detail.get(i).getElementsByAttributeValue("class","comic-tag").get(0).text();
                String tags[] = stringtags.split(" ");
                for(int j=0;j<tags.length;j++){
                    taglist.add(tags[j]);
                }
                comic.setTags(taglist);
            }catch (Exception e){

            }
            mComic.add(comic);
        }
        return mComic;
    }

    public static List<Comic> TransToCategory(Document doc) {
        List<Comic> mComic = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class","comic-link");
        for(int i=0;i<detail.size();i++){
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("strong").text());
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setId(Long.parseLong(getID(detail.get(i).attr("href"))));
            comic.setUpdates(detail.get(i).getElementsByAttributeValue("class","comic-update").get(0).text());
            List<String> taglist = new ArrayList<>();
            try{
                Element ElementDescribe = detail.get(i).getElementsByAttributeValue("class","comic-desc").get(0);
                comic.setDescribe(ElementDescribe.text());
                String stringtags = detail.get(i).getElementsByAttributeValue("class","comic-tag").get(0).text();
                String tags[] = stringtags.split(" ");
                for(int j=0;j<tags.length;j++){
                    taglist.add(tags[j]);
                }
                comic.setTags(taglist);
            }catch (Exception e){

            }
            mComic.add(comic);
        }
        return mComic;
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
            //设置阅读方式
            List<Element> Element_isJ = doc.getElementsByAttributeValue("src","http://q2.qlogo.cn/g?b=qq&k=hMPm8WLLDbcdk0Vs4epHxA&s=100&t=561");
            if(Element_isJ!=null&&Element_isJ.size()!=0){
                comic.setReadType(Constants.UP_TO_DOWN);
            }else{
                comic.setReadType(Constants.UP_TO_DOWN);
            }
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
