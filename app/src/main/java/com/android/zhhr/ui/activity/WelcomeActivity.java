package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.presenter.WelcomePresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.view.IWelcomeView;
import com.android.zhhr.utils.ADUtils;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.PermissionUtils;
import com.zonst.libzadsdk.ZAdComponent;
import com.zonst.libzadsdk.ZAdSdk;
import com.zonst.libzadsdk.ZAdType;
import com.zonst.libzadsdk.listener.ZAdDisplayListener;
import com.zonst.libzadsdk.listener.ZAdLoadListener;

import butterknife.Bind;

/**
 * Created by 张皓然 on 2018/1/29.
 */

public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements IWelcomeView,ActivityCompat.OnRequestPermissionsResultCallback{
    private ZAdComponent ad;
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
        if(Constants.isAD){
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
        }
        //add end

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestory();
        super.onDestroy();
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
