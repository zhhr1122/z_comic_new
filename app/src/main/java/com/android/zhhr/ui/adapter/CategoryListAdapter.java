package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.LoadingItem;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.ui.custom.NoScrollGridLayoutManager;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class CategoryListAdapter extends BaseRecyclerAdapter<Comic> {
    public static final int ITEM_FULL = 1;
    public static final int ITEM_LOADING = 2;
    private int itemLoadingLayoutId;
    public CategoryListAdapter(Context context, int itemLayoutId,int itemLoadingLayoutId) {
        super(context, itemLayoutId);
        this.itemLoadingLayoutId = itemLoadingLayoutId;
    }

    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
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
        switch (getItemViewType(position)){
            case ITEM_FULL:
                holder.setText(R.id.tv_title,item.getTitle());
                holder.setText(R.id.tv_describe,item.getDescribe());
                holder.setImageByUrl(R.id.iv_image,item.getCover());
                break;
            case ITEM_LOADING:
                LoadingItem loading = (LoadingItem) item;
                if(loading.isLoading()){
                    holder.startAnimation(R.id.iv_loading);
                    holder.setText(R.id.tv_loading,"正在加载");
                }else{
                    holder.setImageResource(R.id.iv_loading,R.mipmap.loading_finish);
                    holder.setText(R.id.tv_loading,"已全部加载完毕");
                }
                break;
        }
    }

    public int getItemViewType(int position) {
        Comic comic = list.get(position);
        if((comic instanceof LoadingItem)){
            return ITEM_LOADING;
        } else{
            return ITEM_FULL;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof NoScrollGridLayoutManager) {
            final NoScrollGridLayoutManager gridManager = ((NoScrollGridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int size ;
                    switch (getItemViewType(position)){
                        case  ITEM_FULL:
                            size = 1;
                            break;
                        case ITEM_LOADING:
                            size = 3;
                            break;
                        default:
                            size = 3;
                            break;
                    }
                    return size;
                }
            });
        }
    }
}

