package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.zhhr.R;
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.setTextViewAppearanceColor(R.id.tv_title,R.style.colorTextPrimary);
            }else{
                holder.setTextViewColor(R.id.tv_title, ContextCompat.getColor(context,R.color.colorPrimary));
            }
        }else{
            holder.setVisibility(R.id.iv_title_bg, View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.setTextViewAppearanceColor(R.id.tv_title,R.style.colorTextBlack);
            }else{
                holder.setTextViewColor(R.id.tv_title, ContextCompat.getColor(context,R.color.colorTextBlack));
            }
        }
        holder.setText(R.id.tv_title, item.getTitle());
    }
}
