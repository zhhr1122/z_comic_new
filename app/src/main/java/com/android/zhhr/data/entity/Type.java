package com.android.zhhr.data.entity;

/**
 * Created by zhhr on 2018/3/16.
 */

public class Type extends BaseBean{
    //种类
    String type;
    //值
    int value;
    //标题
    String title;

    public Type(String type, String title, int value) {
        this.type = type;
        this.title = title;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
