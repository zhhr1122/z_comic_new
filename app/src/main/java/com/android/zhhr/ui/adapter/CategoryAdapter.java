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

public class CategoryAdapter extends BaseRecyclerAdapter<Type> {
    private Map<String,Integer> mSelectMap;

    public void setSelectMap(Map<String, Integer> mSelectMap) {
        this.mSelectMap = mSelectMap;
        notifyDataSetChanged();
    }

    public CategoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mSelectMap = new HashMap<>();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Type item, int position) {
        if(item.getValue()==mSelectMap.get(item.getType())){
            holder.setVisibility(R.id.iv_title_bg, View.VISIBLE);
            holder.setTextViewAppearanceColor(R.id.tv_title,R.style.colorTextPrimary);
        }else{
            holder.setVisibility(R.id.iv_title_bg, View.GONE);
            holder.setTextViewAppearanceColor(R.id.tv_title,R.style.colorTextBlack);
        }
        holder.setText(R.id.tv_title, item.getTitle());
    }
}
