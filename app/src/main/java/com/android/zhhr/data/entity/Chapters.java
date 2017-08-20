package com.android.zhhr.data.entity;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/20.
 */

public class Chapters extends BaseBean{

    protected List<String> comiclist;
    protected String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getComiclist() {
        return comiclist;
    }

    public void setComiclist(List<String> comiclist) {
        this.comiclist = comiclist;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "comiclist=" + comiclist +
                '}';
    }
}
