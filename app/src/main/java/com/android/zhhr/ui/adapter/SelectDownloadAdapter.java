package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.graphics.Color;

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
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        if(!isOrder){
            position = list.size()-position-1;
        }
        holder.setText(R.id.tv_position,(position+1)+"-"+list.get(position));
        if(map!=null){
            switch (map.get(position)){
                case Constants.CHAPTER_SELECTED:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_selected_download);
                    holder.setTextViewColor(R.id.tv_position, Color.WHITE);
                    break;
                case Constants.CHAPTER_DOWNLOAD:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_downloaded_download);
                    holder.setTextViewColor(R.id.tv_position, Color.parseColor("#666666"));
                    break;
                case Constants.CHAPTER_FREE:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_select_download);
                    holder.setTextViewColor(R.id.tv_position, Color.parseColor("#666666"));
                    break;
                default:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper,R.drawable.btn_select_download);
                    holder.setTextViewColor(R.id.tv_position, Color.parseColor("#666666"));
                    break;
            }
        }
    }
}
