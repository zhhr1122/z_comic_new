package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.ui.adapter.base.BookShelfAdapter;
import com.android.zhhr.utils.LogUtil;

import java.util.List;

/**
 * Created by 皓然 on 2017/8/14.
 */

public class DownloadAdapter extends BookShelfAdapter<Comic> {
    public DownloadAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }
    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        if(item!=null){
            holder.setText(R.id.tv_title,item.getTitle());
            holder.setImageByUrl(R.id.iv_cover,item.getCover());
            if(!isEditing){
                holder.setVisibility(R.id.tv_download_info,View.VISIBLE);
                switch (item.getState()){
                    case FINISH:
                        holder.setText(R.id.tv_download_status,"下载完成("+item.getDownload_num_finish()+")");
                        holder.setImageResource(R.id.iv_download_status,R.mipmap.download_icon_finish);
                        holder.setText(R.id.tv_download_info,"续看");
                        holder.setVisibility(R.id.fl_cover_wraper,View.GONE);
                        break;
                    case START:
                        holder.setVisibility(R.id.fl_cover_wraper,View.VISIBLE);
                        holder.setHtmlText(R.id.tv_download_status,"<font color='#eb6056'>"+"已暂停("+item.getDownload_num_finish()+"/"+item.getDownload_num()+")"+"</font>");
                        holder.setImageResource(R.id.iv_download_status,R.mipmap.download_icon);
                        holder.setText(R.id.tv_download_info,"下载管理");
                        break;
                    case DOWN:
                        holder.setVisibility(R.id.fl_cover_wraper,View.VISIBLE);
                        holder.setHtmlText(R.id.tv_download_status,"<font color='#eb6056'>"+"已暂停("+item.getDownload_num_finish()+"/"+item.getDownload_num()+")"+"</font>");
                        holder.setImageResource(R.id.iv_download_status,R.mipmap.download_icon);
                        holder.setText(R.id.tv_download_info,"下载管理");
                        break;
                }
            }else{
                holder.setVisibility(R.id.tv_download_info,View.GONE);
                if(mMap.size()>position&&mMap.get(position) == Constants.CHAPTER_SELECTED){
                    holder.setImageResource(R.id.iv_download_status,R.mipmap.item_selected);
                }else{
                    holder.setImageResource(R.id.iv_download_status,R.mipmap.item_select_history);
                }
            }
            //最后一个item隐藏下划线
            if(position == list.size()-1){
                holder.setVisibility(R.id.v_bottom_line, View.GONE);
            }else{
                holder.setVisibility(R.id.v_bottom_line, View.VISIBLE);
            }
        }
    }
}
