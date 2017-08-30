package com.android.zhhr.data.entity;

import android.widget.LinearLayout;

import com.android.zhhr.db.utils.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 皓然 on 2017/7/10.
 */
@Entity
public class Comic extends BaseBean {
    @Id
    protected long id;
    //标题
    protected String title;
    //封面图片
    protected String cover;
    //作者
    protected String author;
    //章节标题
    @Convert(columnType = String.class, converter = StringConverter.class)
    protected List<String> chapters;
    //标签
    @Convert(columnType = String.class, converter = StringConverter.class)
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

    protected int CurrentChapter;

    public int getCurrentChapter() {
        return this.CurrentChapter;
    }

    public void setCurrentChapter(int CurrentChapter) {
        this.CurrentChapter = CurrentChapter;
    }

    public int getReadType() {
        return this.readType;
    }

    public void setReadType(int readType) {
        this.readType = readType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdates() {
        return this.updates;
    }

    public void setUpdates(String updates) {
        this.updates = updates;
    }

    public String getTopics() {
        return this.topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getPopularity() {
        return this.popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPoint() {
        return this.point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCollections() {
        return this.collections;
    }

    public void setCollections(String collections) {
        this.collections = collections;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getChapters() {
        return this.chapters;
    }

    public void setChapters(List<String> chapters) {
        this.chapters = chapters;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Generated(hash = 839294503)
    public Comic(long id, String title, String cover, String author,
            List<String> chapters, List<String> tags, String collections,
            String describe, String point, String popularity, String topics,
            String updates, String status, int readType, int CurrentChapter) {
        this.id = id;
        this.title = title;
        this.cover = cover;
        this.author = author;
        this.chapters = chapters;
        this.tags = tags;
        this.collections = collections;
        this.describe = describe;
        this.point = point;
        this.popularity = popularity;
        this.topics = topics;
        this.updates = updates;
        this.status = status;
        this.readType = readType;
        this.CurrentChapter = CurrentChapter;
    }

    @Generated(hash = 1347984162)
    public Comic() {
    }


}