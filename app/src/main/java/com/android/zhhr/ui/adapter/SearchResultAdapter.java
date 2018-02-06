package com.android.zhhr.ui.adapter;

import android.content.Context;
import android.text.Html;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;
import com.android.zhhr.utils.TextUtils;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchResultAdapter extends BaseRecyclerAdapter<Comic> {
    private String key;
    public SearchResultAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }
    //设置关键字
    public void setKey(String key){
        this.key = key;
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setHtmlText(R.id.tv_title, TextUtils.getSearchText(key,item.getTitle()));
        holder.setHtmlText(R.id.tv_author,TextUtils.getSearchText(key,item.getAuthor()));
        holder.setText(R.id.tv_update,item.getUpdates());
        holder.setText(R.id.tv_tag,item.getTags().toString());
        holder.setImageByUrl(R.id.iv_cover,item.getCover());
    }
}
