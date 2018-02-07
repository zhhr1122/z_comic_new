package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.utils.TextUtils;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchDynamicAdapter extends BaseRecyclerAdapter<Comic>{
    private String key;
    public void setKey(String key){
        this.key = key;
    }
    public SearchDynamicAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setHtmlText(R.id.tv_dynamic_search, TextUtils.getSearchText(key,item.getTitle()));
    }
}
