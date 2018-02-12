package com.android.zhhr.data.entity.db;

import com.android.zhhr.data.entity.BaseBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 张皓然 on 2018/2/13.
 */
@Entity
public class DBDownloadItems extends BaseBean{
    @Id(autoincrement = true)
    protected Long id;

    protected Long comic_id;
    //漫画标题
    protected String title;
    //章节标题
    protected String chapters_title;
    //章节编号
    protected int chapters;
    //章节漫画页面
    protected int num;
    //当前下载的漫画页面
    protected int current_num;
    //开始下载时间
    protected Long create_time;
    //最后更新时间
    protected Long update_time;
    public Long getUpdate_time() {
        return this.update_time;
    }
    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }
    public Long getCreate_time() {
        return this.create_time;
    }
    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }
    public int getCurrent_num() {
        return this.current_num;
    }
    public void setCurrent_num(int current_num) {
        this.current_num = current_num;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getChapters() {
        return this.chapters;
    }
    public void setChapters(int chapters) {
        this.chapters = chapters;
    }
    public String getChapters_title() {
        return this.chapters_title;
    }
    public void setChapters_title(String chapters_title) {
        this.chapters_title = chapters_title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Long getComic_id() {
        return this.comic_id;
    }
    public void setComic_id(Long comic_id) {
        this.comic_id = comic_id;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 366649375)
    public DBDownloadItems(Long id, Long comic_id, String title,
            String chapters_title, int chapters, int num, int current_num,
            Long create_time, Long update_time) {
        this.id = id;
        this.comic_id = comic_id;
        this.title = title;
        this.chapters_title = chapters_title;
        this.chapters = chapters;
        this.num = num;
        this.current_num = current_num;
        this.create_time = create_time;
        this.update_time = update_time;
    }
    @Generated(hash = 1311776000)
    public DBDownloadItems() {
    }
}
