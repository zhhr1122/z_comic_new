package com.android.zhhr.utils;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.zonst.libzadsdk.ZAdComponent;
import com.zonst.libzadsdk.ZAdSdk;
import com.zonst.libzadsdk.ZAdType;
import com.zonst.libzadsdk.listener.ZAdDisplayListener;

/**
 * Created by 张皓然 on 2018/1/25.
 */

public class ADUtils {

    public static void getAdBanner(Context context, ViewGroup mAdBanner) {
        ZAdComponent banner = ZAdSdk.getInstance().getAd(ZAdType.BANNER, "1001");
        banner.removeFromParent();
        mAdBanner.addView(banner.getContentView()); // 添加到父视图里
        ZAdSdk.getInstance().getLoader().loadAd(context, banner, false);
    }

    public static void getAdFullInterstitial(Context context){
        ZAdComponent ad = ZAdSdk.getInstance().getAd(ZAdType.FULL_SCREEN, "1003");
        ZAdSdk.getInstance().getLoader().loadAd(context, ad, false);

    }
}
