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
    }
}
