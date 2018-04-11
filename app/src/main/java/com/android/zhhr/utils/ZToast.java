package com.android.zhhr.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.zhhr.R;
import com.android.zhhr.ui.custom.ToastLayout;

/**
 * Created by zhhr on 2018/3/27.
 */

public class ZToast {

    private Activity mActivity;
    private RelativeLayout mToastLayout;
    private ToastLayout mToast;
    private ViewGroup mView;
    private String text;
    private long times;
    private static ZToast mToastInstance;

    public ZToast(Activity mActivity,String text,long times){
        this.mActivity = mActivity;
        this.text = text;
        this.times = times;
    }

    public ZToast(ViewGroup mView,String text,long times){
        this.mView = mView;
        this.text = text;
        this.times = times;
    }

    public static ZToast makeText(Activity mActivity,String text,long times){
        mToastInstance = new ZToast(mActivity,text,times);
        return mToastInstance;
    }

    public static ZToast makeText(ViewGroup mView,String text,long times){
        mToastInstance = new ZToast(mView,text,times);
        return mToastInstance;
    }


    public void show(){
        if(mActivity!=null){
            mToastLayout = (RelativeLayout) mActivity.findViewById(R.id.rl_toast);
            if(mToastLayout==null){//判断是否已经添加进母VIEW里，没有则添加进去
                mToast = new ToastLayout(mActivity);
                mActivity.addContentView(mToast,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mActivity,60)));
            }else{//如果有，则直接取出
                mToast = (ToastLayout) mToastLayout.getParent();
            }
            mToast.setContent(text);
            mToast.showToast(times);
            return;
        }else if(mView!=null){
            mToastLayout = (RelativeLayout) mView.findViewById(R.id.rl_toast);
            if(mToastLayout==null){
                mToast = new ToastLayout(mView.getContext());
                mView.addView(mToast,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mView.getContext(),60)));
            }else{
                mToast = (ToastLayout) mToastLayout.getParent();
            }
            mToast.setContent(text);
            mToast.showToast(times);
        }
    }

    private boolean isShowToast(){
        if(mToast == null){
            return false;
        }
        return  mToast.isShow();
    }

    public static boolean isShow(){
        if(mToastInstance == null){
            return false;
        }else{
            boolean isShow = mToastInstance.isShowToast();
            mToastInstance = null;
            return isShow;
        }
    }
}
