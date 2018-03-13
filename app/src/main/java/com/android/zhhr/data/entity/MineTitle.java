package com.android.zhhr.data.entity;

/**
 * Created by zhhr on 2018/3/13.
 */

public class MineTitle extends BaseBean{
    private int ResID;
    private String Title;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getResID() {
        return ResID;
    }

    public void setResID(int resID) {
        ResID = resID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
