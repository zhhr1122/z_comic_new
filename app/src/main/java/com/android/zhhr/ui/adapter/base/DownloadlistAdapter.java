package com.android.zhhr.ui.adapter.base;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.utils.TextUtils;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class DownloadlistAdapter extends BaseRecyclerAdapter<DBDownloadItems>{
    private String key;
    public void setKey(String key){
        this.key = key;
    }
    public DownloadlistAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, DBDownloadItems item, int position) {
        holder.setText(R.id.tv_title,item.getChapters_title());
    }
}
