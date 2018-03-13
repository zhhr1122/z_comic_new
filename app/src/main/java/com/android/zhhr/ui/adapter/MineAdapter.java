package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.MineTitle;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

/**
 * Created by zhhr on 2018/3/13.
 */

public class MineAdapter extends BaseRecyclerAdapter<MineTitle> {
    public MineAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, MineTitle item, int position) {
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setImageResource(R.id.iv_mine_icon,item.getResID());
        holder.setText(R.id.tv_size,item.getSize());
        if(position == 0){
            holder.setImageResource(R.id.iv_select,R.mipmap.item_select);
        }else{
            holder.setImageResource(R.id.iv_select,R.mipmap.add_more);
        }
    }
}
