package com.android.zhhr.data.entity;

import java.io.Serializable;

/**
 * Created by 皓然 on 2017/6/15.
 */

public class BaseBean implements Serializable{
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
