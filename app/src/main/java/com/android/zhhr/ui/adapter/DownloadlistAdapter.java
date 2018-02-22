package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.net.download.HttpDownOnNextListener;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.TextUtils;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class DownloadlistAdapter extends BaseRecyclerAdapter<DownInfo> {

    public DownloadlistAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(final BaseRecyclerHolder holder, final DownInfo item, int position) {
        holder.setText(R.id.tv_title,item.getSavePath());
        holder.setProgress(R.id.pg_download,item.getCountLength(),item.getReadLength());
        item.setListener(new HttpDownOnNextListener<DownInfo>() {
            @Override
            public void onNext(DownInfo downInfo) {

            }

            @Override
            public void onStart() {
                LogUtil.v("开始下载");
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void updateProgress(long readLength, long countLength) {
                holder.setProgress(R.id.pg_download,countLength,readLength);
                LogUtil.d("url="+item.getUrl()+",readLength="+readLength+",countLength="+countLength);
            }
        });
    }
}
