package com.android.zhhr.data.entity;

/**
 * Created by 张皓然 on 2018/1/25.
 */

public class LoadingItem extends Comic{
    boolean isLoading;
    public LoadingItem(boolean isLoading){
        this.isLoading = isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }
}
