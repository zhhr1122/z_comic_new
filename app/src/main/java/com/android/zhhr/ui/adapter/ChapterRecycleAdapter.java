package com.android.zhhr.ui.adapter;

import android.content.Context;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerHolder;

/**
 * Created by 张皓然 on 2018/1/30.
 */

public class ChapterRecycleAdapter extends BaseRecyclerAdapter<String>{

    public ChapterRecycleAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public void setDatas(PreloadChapters preloadChapters){
        this.list.clear();
        this.list.addAll(preloadChapters.getPrelist());
        this.list.addAll(preloadChapters.getNowlist());
        this.list.addAll(preloadChapters.getNextlist());
        notifyDataSetChanged();
    }

    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        holder.setPhotoViewImageByUrl(R.id.pv_comic,item);
    }
}
