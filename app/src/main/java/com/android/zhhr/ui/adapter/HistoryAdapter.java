package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.HomeTitle;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

import java.util.List;

/**
 * Created by 皓然 on 2017/8/14.
 */

public class HistoryAdapter extends BaseRecyclerAdapter<Comic>{
    public static final int ITEM_TITLE = 0;
    public static final int ITEM_FULL = 1;

    private int itemTitleLayoutId;

    private HistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public HistoryAdapter(Context context,int itemLayoutId,int itemTitleLayoutId){
        this(context,itemLayoutId);
        this.itemTitleLayoutId = itemTitleLayoutId;
    }

    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case ITEM_TITLE:
                view = inflater.inflate(itemTitleLayoutId, parent, false);
                break;
            default:
                view = inflater.inflate(itemLayoutId, parent, false);
                break;
        }

        return BaseRecyclerHolder.getRecyclerHolder(context, view);
    }


    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        if(item!=null){
            switch (getItemViewType(position)){
                case ITEM_TITLE:
                    holder.setText(R.id.tv_history_title,((HomeTitle)item).getItemTitle());
                    break;
                case ITEM_FULL:
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
                    }else{
                        holder.setVisibility(R.id.v_bottom_line, View.VISIBLE);
                    }
                    break;
            }
        }
    }

    public int getItemViewType(int position) {
        Comic comic = list.get(position);
        if(comic instanceof HomeTitle){
            return ITEM_TITLE;
        }else{
            return ITEM_FULL;
        }
    }
}
