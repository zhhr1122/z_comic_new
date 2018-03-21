package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.db.DBChapters;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.ui.adapter.base.BookShelfAdapter;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class DownloadChapterlistAdapter extends BookShelfAdapter<DBChapters> {

    public DownloadChapterlistAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(final BaseRecyclerHolder holder, final DBChapters item, final int position) {
        holder.setText(R.id.tv_title,item.getChapters_title());
        holder.setProgress(R.id.pg_download,item.getNum(),item.getCurrent_num());
        if(isEditing()){
            holder.setVisibility(R.id.iv_download_select, View.VISIBLE);
            if(mMap.size()!=0&&mMap.get(position) == Constants.CHAPTER_SELECTED){
                holder.setImageResource(R.id.iv_download_select,R.mipmap.item_selected);
            }else{
                holder.setImageResource(R.id.iv_download_select,R.mipmap.item_select);
            }
        }else{
            holder.setVisibility(R.id.iv_download_select, View.GONE);
        }
        switch (item.getState()){
            case NONE:
                /*起始状态*/
                if(item.getNum() == 0 ){
                    holder.setText(R.id.tv_progress,"等待下载");
                }else{
                    holder.setText(R.id.tv_progress,"等待下载:"+item.getCurrent_num()+"/"+item.getNum());
                }
                holder.setImageResource(R.id.iv_download_status,R.mipmap.item_downloading);
                break;
            case START:
                /*起始状态*/
                holder.setText(R.id.tv_progress,"解析下载地址");
                holder.setImageResource(R.id.iv_download_status,R.mipmap.item_downloading);
                break;
            case PAUSE:
                //holder.setText(R.id.tv_progress,"等待下载:"+item.getCurrent_num()+"/"+item.getNum());
                break;
            case DOWN:
                holder.setText(R.id.tv_progress,"正在下载:"+item.getCurrent_num()+"/"+item.getNum());
                holder.setImageResource(R.id.iv_download_status,R.mipmap.item_downloading);
                break;
            case STOP:
                if(item.getNum() == 0 ){
                    holder.setText(R.id.tv_progress,"下载停止");
                }else{
                    holder.setText(R.id.tv_progress,"下载停止:"+item.getCurrent_num()+"/"+item.getNum());
                }
                holder.setImageResource(R.id.iv_download_status,R.mipmap.item_pasue);
                break;
            case ERROR:
                if(item.getNum() == 0 ){
                    holder.setText(R.id.tv_progress,"下载错误");
                }else{
                    holder.setText(R.id.tv_progress,"下载错误:"+item.getCurrent_num()+"/"+item.getNum());
                }
                holder.setImageResource(R.id.iv_download_status,R.mipmap.item_pasue);
                break;
            case FINISH:
                holder.setText(R.id.tv_progress,"下载完成:"+item.getCurrent_num()+"/"+item.getNum());
                holder.setImageResource(R.id.iv_download_status,R.mipmap.item_finish);
                break;
        }
    }
}
