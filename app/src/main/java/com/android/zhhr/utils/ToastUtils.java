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

public class ToastUtils {

    private Activity mActivity;
    private RelativeLayout mToastLayout;
    private ToastLayout mToast;
    private ViewGroup mView;

    public ToastUtils(Activity mActivity){
        this.mActivity = mActivity;
    }

    public ToastUtils(ViewGroup mView){
        this.mView = mView;
    }

    /**
     * 初始化
     */
    public void showToast(String str){
        showToast(str,1000);
    }

    public void showToast(String str,long times){
        if(mActivity!=null){
            mToastLayout = (RelativeLayout) mActivity.findViewById(R.id.rl_toast);
            if(mToastLayout==null){
                mToast = new ToastLayout(mActivity);
                mActivity.addContentView(mToast,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mActivity,60)));
            }else{
                mToast = (ToastLayout) mToastLayout.getParent();
            }
            mToast.setContent(str);
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
            mToast.setContent(str);
            mToast.showToast(times);
        }
    }

    public boolean isShow(){
        if(mToast == null){
            return false;
        }
        return  mToast.isShow();
    }
}
