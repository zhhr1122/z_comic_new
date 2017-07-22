package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/19.
 */

public class MainAdapter extends BaseRecyclerAdapter<Comic> {

    public MainAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public MainAdapter(Context context, List<Comic> list, int itemLayoutId) {
        super(context, list, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setImageByUrl(R.id.iv_image,item.getCover());
    }
}
