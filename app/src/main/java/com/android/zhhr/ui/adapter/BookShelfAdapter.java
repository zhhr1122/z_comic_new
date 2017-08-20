package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

import java.util.List;

/**
 * Created by 皓然 on 2017/8/14.
 */

public class BookShelfAdapter extends BaseRecyclerAdapter<Comic>{
    public BookShelfAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }
    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setImageByUrl(R.id.iv_image,item.getCover());
        holder.setText(R.id.tv_index_reader,item.getChapters().size()+"话");
    }
}
