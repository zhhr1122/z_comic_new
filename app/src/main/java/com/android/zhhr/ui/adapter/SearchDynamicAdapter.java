package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchDynamicAdapter extends BaseRecyclerAdapter<SearchBean>{
    public SearchDynamicAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, SearchBean item, int position) {
        holder.setText(R.id.tv_dynamic_search,item.getTitle());
    }
}
