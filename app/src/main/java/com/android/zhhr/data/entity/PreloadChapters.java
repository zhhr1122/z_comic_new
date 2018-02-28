package com.android.zhhr.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 皓然 on 2017/7/24.
 */

public class PreloadChapters extends BaseBean{
    protected List<String> prelist;
    protected List<String> nowlist;
    protected List<String> nextlist;
    protected String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPrelist() {
        return prelist;
    }

    public void setPrelist(List<String> prelist) {
        this.prelist = prelist;
    }

    public List<String> getNowlist() {
        return nowlist;
    }

    public void setNowlist(List<String> nowlist) {
        this.nowlist = nowlist;
    }

    public List<String> getNextlist() {
        return nextlist;
    }

    public void setNextlist(List<String> nextlist) {
        this.nextlist = nextlist;
    }

    public PreloadChapters(){

    }

    public int getDataSize(){
        return prelist.size()+nowlist.size()+nextlist.size();
    }

    public int getNowSize(){
        return nowlist.size();
    }

    public int getPreSize(){
        return prelist.size();
    }

    public int getNextSize(){
        return nextlist.size();
    }

    public int getSize(){
        return getPreSize()+getNowSize()+getNextSize();
    }

    @Override
    public String toString() {
        return "PreloadChapters{" +
                "prelist=" + prelist +
                ", nowlist=" + nowlist +
                ", nextlist=" + nextlist +
                '}';
    }

    public boolean isNotNull(){
        return prelist!=null&&nowlist!=null&&nextlist!=null;
    }
}
