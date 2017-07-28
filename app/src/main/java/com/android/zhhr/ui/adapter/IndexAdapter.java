package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.zhhr.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 皓然 on 2017/7/28.
 */

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.MyViewHolder>
{
    private Context mContext;
    private List<String> mDatas;
    public boolean isOrder = true;

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
        notifyDataSetChanged();
    }

    public IndexAdapter(Context context,List<String> lists){
        this.mContext = context;
        mDatas = lists;
    }

    public List<String> getmDatas() {
        return mDatas;
    }

    public void setDatas(List<String> mDatas){
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
    }

    public void updateWithClear(List<String> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }





    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chapter, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        if(!isOrder){
            holder.mTitle.setText(mDatas.get(mDatas.size()-position-1));
            holder.mPosition.setText(mDatas.size()-position+" - ");
        }else{
            holder.mTitle.setText(mDatas.get(position));
            holder.mPosition.setText((position+1)+" - ");
        }
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.tv_position)
        TextView mPosition;
        @Bind(R.id.tv_title)
        TextView mTitle;

        public MyViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }
}
