package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/19.
 */

public class DetailAdapter extends BaseRecyclerAdapter<String> {
    public boolean isOrder = true;

    public DetailAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public DetailAdapter(Context context, List<String> list, int itemLayoutId) {
        super(context, list, itemLayoutId);
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
        notifyDataSetChanged();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        if(!isOrder){
            holder.setText(R.id.tv_title,list.get(list.size()-position-1));
            holder.setText(R.id.tv_position,list.size()-position+" - ");
        }else{
            holder.setText(R.id.tv_title,item);
            holder.setText(R.id.tv_position,(position+1)+" - ");
        }

    }
}
