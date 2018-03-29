package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 皓然 on 2017/7/19.
 */

public class SelectDownloadAdapter extends BaseRecyclerAdapter<String> {
    private HashMap<Integer,Integer> map;
    public boolean isOrder = true;

    public HashMap<Integer, Integer> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Integer> map) {
        this.map = map;
    }

    public SelectDownloadAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }
    public void setOrder(boolean order) {
        isOrder = order;
        notifyDataSetChanged();
    }

    public boolean isOrder() {
        return isOrder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position);
        }else{
            convert(holder, list.get(position), position);
        }
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        if(!isOrder){
            position = list.size()-position-1;
        }
        holder.setText(R.id.tv_position,(position+1)+"-"+list.get(position));
        if(map!=null){
            switch (map.get(position)){
                case Constants.CHAPTER_SELECTED:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_selected_download);
                    //holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextColorBg);
                    holder.setTextViewColor(R.id.tv_position, ContextCompat.getColor(context,R.color.colorBg));
                    holder.setVisibility(R.id.iv_download_status, View.GONE);
                    break;
                case Constants.CHAPTER_DOWNLOAD:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_downloaded_download);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextBlack);
                    }else{
                        holder.setTextViewColor(R.id.tv_position, ContextCompat.getColor(context,R.color.colorTextBlack));
                    }
                    holder.setVisibility(R.id.iv_download_status, View.VISIBLE);
                    holder.setImageResource(R.id.iv_download_status,R.mipmap.icon_download_finished);
                    break;
                case Constants.CHAPTER_DOWNLOADING:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_downloaded_download);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextBlack);
                    }else{
                        holder.setTextViewColor(R.id.tv_position, ContextCompat.getColor(context,R.color.colorTextBlack));
                    }
                    holder.setVisibility(R.id.iv_download_status, View.VISIBLE);
                    holder.setImageResource(R.id.iv_download_status,R.mipmap.icon_download_downloading);
                    break;
                case Constants.CHAPTER_FREE:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_select_download);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextBlack);
                    }else{
                        holder.setTextViewColor(R.id.tv_position, ContextCompat.getColor(context,R.color.colorTextBlack));
                    }
                    holder.setVisibility(R.id.iv_download_status, View.GONE);
                    break;
                default:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_select_download);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextBlack);
                    }else{
                        holder.setTextViewColor(R.id.tv_position, ContextCompat.getColor(context,R.color.colorTextBlack));
                    }
                    holder.setVisibility(R.id.iv_download_status, View.GONE);
                    break;
            }
        }
    }
}
