package com.android.zhhr.data.entity;

/**
 * Created by 张皓然 on 2018/1/25.
 */

public class HomeTitle extends Comic{
    //标题名字
    public String itemTitle;
    //标题种类
    public int TitleType;

    public HomeTitle(String itemTitle){
        this.itemTitle = itemTitle;
    }

    public HomeTitle(){
    }


    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getTitleType() {
        return TitleType;
    }

    public void setTitleType(int titleType) {
        TitleType = titleType;
    }
}
