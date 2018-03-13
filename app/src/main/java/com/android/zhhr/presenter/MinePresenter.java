package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.MineTitle;
import com.android.zhhr.ui.view.IMineView;
import com.android.zhhr.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhhr on 2018/3/13.
 */

public class MinePresenter extends BasePresenter<IMineView>{
    private List<MineTitle> mLists;
    public MinePresenter(Activity context, IMineView view) {
        super(context, view);
        mLists = new ArrayList<>();
    }

    public void loadData() {
        MineTitle mTitle = new MineTitle();
        mTitle.setTitle("夜间模式");
        mTitle.setResID(R.mipmap.icon_night);
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.mipmap.icon_cache);
        mTitle.setTitle("清除缓存");
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.mipmap.icon_feedback);
        mTitle.setTitle("问题反馈");
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.mipmap.icon_author);
        mTitle.setTitle("关于作者");
        mLists.add(mTitle);
        mView.fillData(mLists);
        mView.getDataFinish();
    }

    public void onItemClick(int position) {
        switch (position){
            case 0:
                switchSkin();
                break;
            case 1:
                clearCache();
                break;
            case 2:
                try {
                    IntentUtil.toQQchat(mContext,"530414168");
                    mView.ShowToast( "已为您跳转到作者QQ");
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.ShowToast( "请检查是否安装QQ");
                }
                break;
            case 3:
                mView.ShowToast( "已为您跳转到作者博客");
                IntentUtil.toUrl(mContext.getApplicationContext(),"http://blog.csdn.net/zhhr1122");
                break;
        }
    }

    /**
     * 更换皮肤
     */
    private void switchSkin() {
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
    }
}
