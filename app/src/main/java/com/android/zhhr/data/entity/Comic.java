package com.android.zhhr.data.entity;

import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/10.
 */

public class Comic extends BaseBean{
    //标题
    protected String title;
    //封面图片
    protected String cover;
    //作者
    protected String author;
    //章节标题
    protected List<String> chapters;
    //标签
    protected List<String> tags;
    //收藏数
    protected String collections;
    //详情
    protected String describe;
    //评分
    protected String point;
    //人气值
    protected String popularity;
    //话题量
    protected String topics;
    //更新时间
    protected String updates;
    //状态
    protected String status;
    //默认阅读方式
    protected int readType;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReadType() {
        return readType;
    }

    public void setReadType(int readType) {
        this.readType = readType;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCollections() {
        return collections;
    }

    public void setCollections(String collections) {
        this.collections = collections;
    }

    public String getDescribe() {
        return "作品简介:"+describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getUpdates() {
        return updates;
    }

    public void setUpdates(String updates) {
        this.updates = updates;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public void setChapters(List<String> chapters) {
        this.chapters = chapters;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", author='" + author + '\'' +
                ", chapters=" + chapters +
                ", tags=" + tags +
                ", collections='" + collections + '\'' +
                ", describe='" + describe + '\'' +
                ", point='" + point + '\'' +
                ", popularity='" + popularity + '\'' +
                ", topics='" + topics + '\'' +
                ", updates='" + updates + '\'' +
                ", status='" + status + '\'' +
                ", readType=" + readType +
                '}';
    }
}
