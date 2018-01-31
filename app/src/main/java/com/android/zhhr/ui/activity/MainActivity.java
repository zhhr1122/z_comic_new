package com.android.zhhr.ui.activity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.presenter.WelcomePresenter;
import com.android.zhhr.ui.activity.base.BaseFragmentActivity;
import com.android.zhhr.utils.ADUtils;
import com.android.zhhr.utils.IntentUtil;
import com.zonst.libzadsdk.ZAdComponent;
import com.zonst.libzadsdk.ZAdSdk;
import com.zonst.libzadsdk.ZAdType;
import com.zonst.libzadsdk.listener.ZAdDisplayListener;
import com.zonst.libzadsdk.listener.ZAdLoadListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 皓然 on 2017/8/6.
 */

public class MainActivity extends BaseFragmentActivity {
    @Bind(R.id.btn_home)
    Button mHome;
    @Bind(R.id.btn_bookshelf)
    Button mBookShelf;
    @Bind(R.id.btn_mine)
    Button mMine;

    ZAdComponent ad;
    @Bind(R.id.rl_splash)
    RelativeLayout mSplash;

    private static Handler mhandler;
    private closeRunnable runnable;

    @Override
    protected void initView() {
        mhandler = new Handler(this.getMainLooper());
        runnable = new closeRunnable();
        mhandler.postDelayed(runnable,1000);

        mHome.setBackgroundResource(R.drawable.homepage_press);
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        fragments.add (fragmentManager.findFragmentById(R.id.fm_home));
        fragments.add (fragmentManager.findFragmentById(R.id.fm_bookshelf));
        fragments.add (fragmentManager.findFragmentById(R.id.fm_mine));
        selectTab(0);
        if(Constants.isAD){
            if(ad==null){
                ad = ZAdSdk.getInstance().createAd(this, ZAdType.FULL_SCREEN, "1002");
            }
           /* ad.setDisplayListener(new ZAdDisplayListener() {
                @Override
                public void onDisplay(boolean b) {

                }
            });
            ad.setLoadListener(new ZAdLoadListener() {
                @Override
                public void onResult(int i) {

                }
            });*/
            ZAdSdk.getInstance().getLoader().loadAd(ad);
            //预加载视频
            ZAdSdk.getInstance().getLoader().preloadAd(ZAdType.VIDEO,"1004");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(runnable);
    }

    class closeRunnable implements Runnable{

        @Override
        public void run() {
            mSplash.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_home)
    public void toHomeFragment(View view){
        selectTab(0);
        resetBottomBtn();
        mHome.setBackgroundResource(R.drawable.homepage_press);
    }

    @OnClick(R.id.btn_bookshelf)
    public void toBookShelfFragment(View view){
        selectTab(1);
        resetBottomBtn();
        mBookShelf.setBackgroundResource(R.drawable.bookshelf_press);
    }

    public void resetBottomBtn(){
        mHome.setBackgroundResource(R.drawable.homepage);
        mMine.setBackgroundResource(R.drawable.mine);
        mBookShelf.setBackgroundResource(R.drawable.bookshelf);
    }
}
