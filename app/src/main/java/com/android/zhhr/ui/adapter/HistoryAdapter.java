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
            if(item.getChapters()!=null&&item.getChapters().size()!=0){
                int current = item.getCurrentChapter();
                if(current == 0){
                    current =1;
                }
                holder.setText(R.id.tv_chapters_current,"上次看到"+current+"-"+item.getChapters().get(current-1));
            }
            holder.setText(R.id.tv_chapters,"更新到"+item.getChapters().size()+"话");
            //最后一个item隐藏下划线
            if(position == list.size()-1){
                holder.setVisibility(R.id.v_bottom_line, View.GONE);
            }
        }
    }
}
