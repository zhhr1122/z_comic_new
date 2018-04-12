package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.android.zhhr.R;
import com.android.zhhr.presenter.WelcomePresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.view.IWelcomeView;

/**
 * Created by 张皓然 on 2018/1/29.
 */

public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements IWelcomeView,ActivityCompat.OnRequestPermissionsResultCallback{
    //private ZAdComponent ad;
    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new WelcomePresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        //首先检查相应的权限
        mPresenter.CheckPermission();

        //add start for adsdk
        /*if(Constants.isAD){
            if(ad==null){
                ad = ZAdSdk.getInstance().createAd(this, ZAdType.FULL_SCREEN, "1002");
            }
            ad.setDisplayListener(new ZAdDisplayListener() {
                @Override
                public void onDisplay(boolean b) {
                    mPresenter.ShowAdSuccess(b);
                }
            });
            ad.setLoadListener(new ZAdLoadListener() {
                @Override
                public void onResult(int i) {
                    mPresenter.getAdSuccess(i);
                }
            });
            ZAdSdk.getInstance().getLoader().loadAd(ad);
            //预加载视频
            ZAdSdk.getInstance().getLoader().preloadAd(ZAdType.VIDEO,"1004");
        }*/
        //add end

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            return;
        }
        mPresenter.onDestory();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length!=0){
            mPresenter.requestPermission(grantResults[0]);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
