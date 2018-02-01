package com.android.zhhr.data.entity;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchBean extends BaseBean{
    public long id;
    public String title;
    public int type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}
