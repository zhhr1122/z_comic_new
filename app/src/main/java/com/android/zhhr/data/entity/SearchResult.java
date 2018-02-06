package com.android.zhhr.data.entity;

import java.util.List;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchResult extends BaseBean{
    public int status;
    public List<SearchBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SearchBean> getData() {
        return data;
    }

    public void setData(List<SearchBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DBSearchResult{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}