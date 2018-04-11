package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.LoadingItem;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class RankAdapter extends BaseRecyclerAdapter<Comic> {
    public static final int ITEM_FULL = 1;
    public static final int ITEM_LOADING = 2;
    private int itemLoadingLayoutId;
    public RankAdapter(Context context, int itemLayoutId,int itemLoadingLayoutId) {
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
        String tags = null;
        switch (getItemViewType(position)){
            case ITEM_FULL:
                holder.setText(R.id.tv_title,item.getTitle());
                if(item.getTags()!=null){
                    tags= item.getTags().toString();
                }
                holder.setText(R.id.tv_tag,tags);
                holder.setText(R.id.tv_update,item.getUpdates());
                holder.setText(R.id.tv_describe,item.getDescribe());
                holder.setImageByUrl(R.id.iv_image,item.getCover());
                switch (position){
                    case 0:
                        holder.setVisibility(R.id.iv_number, View.VISIBLE);
                        holder.setImageResource(R.id.iv_number,R.mipmap.icon_number1);
                        break;
                    case 1:
                        holder.setVisibility(R.id.iv_number, View.VISIBLE);
                        holder.setImageResource(R.id.iv_number,R.mipmap.icon_number2);
                        break;
                    case 2:
                        holder.setVisibility(R.id.iv_number, View.VISIBLE);
                        holder.setImageResource(R.id.iv_number,R.mipmap.icon_number3);
                        break;
                    default:
                        holder.setVisibility(R.id.iv_number, View.GONE);
                }
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
}
