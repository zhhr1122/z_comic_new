package com.android.zhhr.utils;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.zonst.libzadsdk.ZAdComponent;
import com.zonst.libzadsdk.ZAdSdk;
import com.zonst.libzadsdk.ZAdType;
import com.zonst.libzadsdk.listener.ZAdDisplayListener;
import com.zonst.libzadsdk.listener.ZAdLoadListener;
import com.zonst.libzadsdk.listener.ZAdRewardListener;

/**
 * Created by 张皓然 on 2018/1/25.
 */

public class ADUtils {
    private Context context;
    private static ADUtils instance;

    public ADUtils(Context context){
        this.context = context;
    }

    public static ADUtils getInstance(Context context){
        if(instance == null){
            synchronized (ADUtils.class){
                if(instance==null){
                    instance = new ADUtils(context);
                }
            }
        }
        return instance;
    }

    public void getAdBanner(ViewGroup mAdBanner) {
        ZAdComponent banner = ZAdSdk.getInstance().createAd(context,ZAdType.BANNER, "1001");
        mAdBanner.addView(banner.getContentView()); // 添加到父视图里
        ZAdSdk.getInstance().getLoader().loadAd(banner);
    }

    public void getAdFullInterstitial(ZAdDisplayListener displayListener,ZAdLoadListener loadListener){
        ZAdComponent ad = ZAdSdk.getInstance().createAd(context,ZAdType.FULL_SCREEN, "1002");
        ad.setDisplayListener(displayListener);
        ad.setLoadListener(loadListener);
        ZAdSdk.getInstance().getLoader().loadAd(ad);
    }

    public void getRewardVideoAd(ZAdRewardListener listener){
        ZAdComponent ad = ZAdSdk.getInstance().createAd(context,ZAdType.VIDEO_REWARD, "1003");
        ad.setRewardListener(listener);
        ZAdSdk.getInstance().getLoader().loadAd(ad);
    }

    public void getVideoAd(){
        ZAdComponent ad = ZAdSdk.getInstance().createAd(context,ZAdType.VIDEO, "1004");
        ZAdSdk.getInstance().getLoader().loadAd(ad);
    }

    public void getPreLoadVideoAd(String slotid){
        ZAdSdk.getInstance().getLoader().preloadAd(ZAdType.VIDEO,slotid);
    }

    public void showPreLoadVideoAd(String slotid) {
        ZAdComponent ad = ZAdSdk.getInstance().createAd(context,ZAdType.VIDEO, slotid);
        if(ZAdSdk.getInstance().getLoader().readyForPreloadAd(ad)){
            ZAdSdk.getInstance().getLoader().showPreloadAd(ad);
        }else{

        }
    }

}
