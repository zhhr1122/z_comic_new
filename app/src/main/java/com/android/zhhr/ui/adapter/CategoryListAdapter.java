package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.Type;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class CategoryListAdapter extends BaseRecyclerAdapter<Comic> {
    private Map<String,Integer> mSelectMap;

    public void setSelectMap(Map<String, Integer> mSelectMap) {
        this.mSelectMap = mSelectMap;
        notifyDataSetChanged();
    }

    public CategoryListAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mSelectMap = new HashMap<>();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setText(R.id.tv_describe,item.getDescribe());
        holder.setImageByUrl(R.id.iv_image,item.getCover());
    }
}
