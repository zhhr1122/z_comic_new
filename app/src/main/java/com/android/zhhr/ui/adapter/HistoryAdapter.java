package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

import java.util.List;

/**
 * Created by 皓然 on 2017/8/14.
 */

public class HistoryAdapter extends BaseRecyclerAdapter<Comic>{
    public HistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }
    public HistoryAdapter(Context context, List<Comic> data, int itemLayoutId) {
        super(context, data, itemLayoutId);
    }
    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        if(item!=null){
            holder.setText(R.id.tv_title,item.getTitle());
            holder.setImageByUrl(R.id.iv_cover,item.getCover());
            holder.setText(R.id.tv_history_page,item.getCurrent_page()+"页/"+item.getCurrent_page_all()+"页");
            if(item.getChapters()!=null&&item.getChapters().size()!=0){
                int current = item.getCurrentChapter();
                holder.setText(R.id.tv_chapters_current,"上次看到"+(current+1)+"-"+item.getChapters().get(current));
            }
            holder.setText(R.id.tv_chapters,"更新到"+item.getChapters().size()+"话");
            //最后一个item隐藏下划线
            if(position == list.size()-1){
                holder.setVisibility(R.id.v_bottom_line, View.GONE);
            }
        }
    }
}
