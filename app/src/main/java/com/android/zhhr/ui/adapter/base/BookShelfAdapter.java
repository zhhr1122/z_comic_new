package com.android.zhhr.ui.adapter.base;

import android.content.Context;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 张皓然 on 2018/3/6.
 */

public abstract class BookShelfAdapter<T> extends BaseRecyclerAdapter<T> {
    protected boolean isEditing;
    protected HashMap<Integer,Integer> mMap;

    public HashMap<Integer, Integer> getmMap() {
        return mMap;
    }

    public void setmMap(HashMap<Integer, Integer> mMap) {
        this.mMap = mMap;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public BookShelfAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mMap = new HashMap<>();
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position);
        }else{
            convert(holder, list.get(position), position);
        }
    }
}
