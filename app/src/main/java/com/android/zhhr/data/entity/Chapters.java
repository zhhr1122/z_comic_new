package com.android.zhhr.data.entity;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/20.
 */

public class Chapters extends BaseBean{

    protected List<String> comiclist;
    protected String comic_id;
    protected int chapters;


    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
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
