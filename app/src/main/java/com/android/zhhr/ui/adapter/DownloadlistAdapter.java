package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.net.download.HttpDownOnNextListener;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.utils.LogUtil;

import java.util.List;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class DownloadlistAdapter extends BaseRecyclerAdapter<DownInfo> {

    public DownloadlistAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public List<DownInfo> getLists(){
        return list;
    }

    @Override
    public void convert(final BaseRecyclerHolder holder, final DownInfo item, final int position) {
        item.setListener(new HttpDownOnNextListener<DownInfo>() {
            @Override
            public void onNext(DownInfo downInfo) {

            }

            @Override
            public void onStart() {
                holder.setText(R.id.tv_progress,"开始下载");
                LogUtil.v(item.getSavePath()+"开始下载");
            }

            @Override
            public void onComplete() {
                holder.setText(R.id.tv_progress,"下载完成");
                holder.setProgress(R.id.pg_download,item.getCountLength(),item.getReadLength());
                LogUtil.v(item.getSavePath()+"下载完成");
            }

            @Override
            public void onPuase() {
                holder.setText(R.id.tv_progress,"下载暂停");
                super.onPuase();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onStop() {
                super.onStop();
            }

            @Override
            public void updateProgress(long readLength, long countLength) {
                holder.setText(R.id.tv_progress,"下载中");
                holder.setProgress(R.id.pg_download,countLength,readLength);
                LogUtil.d("url="+item.getSavePath()+",readLength="+readLength+",countLength="+countLength+",position="+position);
            }
        });

        switch (item.getState()){
            case START:
                /*起始状态*/
                holder.setText(R.id.tv_progress,"点击下载");
                break;
            case PAUSE:
                holder.setText(R.id.tv_progress,"下载暂停");
                break;
            case DOWN:
                //manager.startDown(apkApi);
                break;
            case STOP:
                holder.setText(R.id.tv_progress,"下载停止");
                break;
            case ERROR:
                holder.setText(R.id.tv_progress,"下载错误");
                break;
            case  FINISH:
                holder.setText(R.id.tv_progress,"下载完成");
                break;
        }
        holder.setText(R.id.tv_title,item.getSavePath());
        holder.setProgress(R.id.pg_download,item.getCountLength(),item.getReadLength());
    }
}
