package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.zhhr.ui.view.IWelcomeView;
import com.android.zhhr.utils.IntentUtil;

/**
 * Created by 张皓然 on 2018/1/29.
 */

public class WelcomePresenter extends BasePresenter<IWelcomeView>{
    private Handler mhandler;
    private Activity mContext;
    private closeRunnable runnable;
    private boolean isShowAd;
    public WelcomePresenter(Activity context, IWelcomeView view) {
        super(context, view);
        mhandler = new Handler(context.getMainLooper());
        mContext = context;
        runnable = new closeRunnable();
        isShowAd = false;
    }

    public void init(){
        mhandler.postDelayed(runnable,2000);
    }


    class closeRunnable implements Runnable{

        @Override
        public void run() {
            mContext.finish();
            IntentUtil.ToMainActivity(mContext);
        }
    }

    public void onDestory(){
        if(!isShowAd){
            mhandler.removeCallbacks(runnable);
        }
    }

    public void getAdSuccess(int i){
        if(i == 10000){
            IntentUtil.ToMainActivity(mContext);
        }
    }

    public void ShowAdSuccess(boolean b){
        mhandler.removeCallbacks(runnable);
        isShowAd = true;
        if(b){
            mContext.finish();
        }
    }
}
