package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.net.download.HttpDownOnNextListener;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.utils.LogUtil;

import java.util.List;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class DownloadChapterlistAdapter extends BaseRecyclerAdapter<DBDownloadItems> {

    public DownloadChapterlistAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public List<DBDownloadItems> getLists(){
        return list;
    }

    @Override
    public void convert(final BaseRecyclerHolder holder, final DBDownloadItems item, final int position) {
        holder.setText(R.id.tv_title,item.getChapters_title());
        holder.setProgress(R.id.pg_download,item.getNum(),item.getCurrent_num());
        switch (item.getState()){
            case NONE:
                /*起始状态*/
                if(item.getNum() == 0 ){
                    holder.setText(R.id.tv_progress,"等待下载");
                }else{
                    holder.setText(R.id.tv_progress,"等待下载:"+item.getCurrent_num()+"/"+item.getNum());
                }
                break;
            case START:
                /*起始状态*/
                holder.setText(R.id.tv_progress,"解析下载地址");
                break;
            case PAUSE:
                //holder.setText(R.id.tv_progress,"等待下载:"+item.getCurrent_num()+"/"+item.getNum());
                break;
            case DOWN:
                holder.setText(R.id.tv_progress,"正在下载:"+item.getCurrent_num()+"/"+item.getNum());
                break;
            case STOP:
                if(item.getNum() == 0 ){
                    holder.setText(R.id.tv_progress,"下载停止");
                }else{
                    holder.setText(R.id.tv_progress,"下载停止:"+item.getCurrent_num()+"/"+item.getNum());
                }
                break;
            case ERROR:
                if(item.getNum() == 0 ){
                    holder.setText(R.id.tv_progress,"下载错误");
                }else{
                    holder.setText(R.id.tv_progress,"下载错误:"+item.getCurrent_num()+"/"+item.getNum());
                }
                break;
            case FINISH:
                holder.setText(R.id.tv_progress,"下载完成:"+item.getCurrent_num()+"/"+item.getNum());
                break;
        }
    }
}
