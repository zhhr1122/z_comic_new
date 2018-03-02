package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.android.zhhr.ui.view.IWelcomeView;
import com.android.zhhr.utils.FileUtil;
import com.android.zhhr.utils.IntentUtil;
import com.android.zhhr.utils.PermissionUtils;

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

    public void CheckPermission(){
        //检查读写权限
        if(PermissionUtils.CheckPermission(PermissionUtils.READ_EXTERNAL_STORAGE,mContext)&&PermissionUtils.CheckPermission(PermissionUtils.WRITE_EXTERNAL_STORAGE,mContext)){
            init();
        }else{
            PermissionUtils.verifyStoragePermissions(mContext);
        }
    }

    /**
     * 初始化
     */
    public void init(){
        mhandler.postDelayed(runnable,2000);
        FileUtil.init();
    }

    /**
     * 根据请求权限的结果
     * @param grantResult
     */
    public void requestPermission(int grantResult) {
        switch (grantResult){
            case PackageManager.PERMISSION_GRANTED:
                init();
                break;
            case PackageManager.PERMISSION_DENIED:
                mView.ShowToast("请开启SD卡权限");
                mContext.finish();
                break;
            default:
                break;
        }
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

   /* public void getAdSuccess(int i){
        if(i == ZAdError.OK){
            IntentUtil.ToMainActivity(mContext);
        }
    }

    public void ShowAdSuccess(boolean b){
        mhandler.removeCallbacks(runnable);
        isShowAd = true;
        if(!b){
            mContext.finish();
        }
    }*/

}
