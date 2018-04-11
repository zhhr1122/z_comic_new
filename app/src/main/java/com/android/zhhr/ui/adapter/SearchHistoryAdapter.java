package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchHistoryAdapter extends BaseRecyclerAdapter<Comic> {
    public SearchHistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }
    public void onClear(){
        this.list.clear();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_history_search, item.getTitle());
    }
}
