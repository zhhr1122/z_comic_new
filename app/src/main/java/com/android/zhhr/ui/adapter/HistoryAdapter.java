package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.HomeTitle;
import com.android.zhhr.data.entity.LoadingItem;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.ui.adapter.base.BookShelfAdapter;
import com.android.zhhr.utils.LogUtil;

import java.util.List;

/**
 * Created by 皓然 on 2017/8/14.
 */

public class HistoryAdapter extends BookShelfAdapter<Comic> {
    public static final int ITEM_TITLE = 0;
    public static final int ITEM_FULL = 1;
    public static final int ITEM_LOADING = 2;

    private int itemTitleLayoutId;
    private int itemLoadingLayoutId;

    private HistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public HistoryAdapter(Context context,int itemLayoutId,int itemTitleLayoutId,int itemLoadingLayoutId){
        this(context,itemLayoutId);
        this.itemTitleLayoutId = itemTitleLayoutId;
        this.itemLoadingLayoutId = itemLoadingLayoutId;
    }

    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case ITEM_TITLE:
                view = inflater.inflate(itemTitleLayoutId, parent, false);
                break;
            case ITEM_LOADING:
                view = inflater.inflate(itemLoadingLayoutId, parent, false);
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
                    if(!isEditing){
                        holder.setImageResource(R.id.iv_select, R.mipmap.download_icon_finish);
                        holder.setVisibility(R.id.tv_download_info,View.VISIBLE);
                    }else{
                        holder.setVisibility(R.id.tv_download_info,View.GONE);
                        if(mMap.size()!=0&&mMap.get(position) == Constants.CHAPTER_SELECTED){
                            holder.setImageResource(R.id.iv_select,R.mipmap.item_selected);
                        }else{
                            holder.setImageResource(R.id.iv_select,R.mipmap.item_select_history);
                        }
                    }
                    holder.setText(R.id.tv_title,item.getTitle());
                    holder.setImageByUrl(R.id.iv_cover,item.getCover());
                    int currentpage = item.getCurrent_page();
                    if(currentpage == 0){
                        currentpage = 1;
                    }
                    holder.setText(R.id.tv_history_page,currentpage+"页/"+item.getCurrent_page_all()+"页");
                    if(item.getChapters()!=null&&item.getChapters().size()!=0){
                        int current = item.getCurrentChapter();
                        holder.setText(R.id.tv_chapters_current,"上次看到"+(current+1)+"-"+item.getChapters().get(current));
                    }
                    holder.setText(R.id.tv_chapters,"更新到"+item.getChapters().size()+"话");
                    //最后一个item隐藏下划线
                    if(list.size()>2&&position == list.size()-2){
                        holder.setVisibility(R.id.v_bottom_line, View.GONE);
                    }else{
                        holder.setVisibility(R.id.v_bottom_line, View.VISIBLE);
                    }
                    break;
                case ITEM_LOADING:
                    LoadingItem loading = (LoadingItem) item;
                    if(loading.isLoading()){
                        holder.startAnimation(R.id.iv_loading);
                        holder.setText(R.id.tv_loading,"正在加载");
                        holder.setVisibility(R.id.v_padding,View.VISIBLE);
                    }else{
                        holder.setImageResource(R.id.iv_loading,R.mipmap.loading_finish);
                        holder.setText(R.id.tv_loading,"已全部加载完毕");
                        holder.setVisibility(R.id.v_padding,View.GONE);
                    }
                    break;
            }
        }
    }

    public int getItemViewType(int position) {
        Comic comic = list.get(position);
        if(comic instanceof HomeTitle){
            return ITEM_TITLE;
        }else if((comic instanceof LoadingItem)){
            return ITEM_LOADING;
        } else{
            return ITEM_FULL;
        }
    }
}
