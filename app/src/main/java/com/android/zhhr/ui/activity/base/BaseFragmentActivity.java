package com.android.zhhr.ui.activity.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.orhanobut.hawk.Hawk;
import com.pgyersdk.crash.PgyCrashManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 皓然 on 2017/8/6.
 */

public abstract class BaseFragmentActivity extends RxAppCompatActivity {
    View NightModel;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;
    protected List<Fragment> fragments;

    protected boolean isTrans;

    public boolean isTrans() {
        return isTrans;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initStatusBar(true);
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        ButterKnife.bind(this);
        initView();

        PgyCrashManager.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchModel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyCrashManager.unregister();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        PgyCrashManager.unregister();
    }

    public void switchModel() {
        NightModel  = findViewById(R.id.v_night);
        try{
            if(Hawk.get(Constants.MODEL)){
                NightModel.setVisibility(View.VISIBLE);
            }else{
                NightModel.setVisibility(View.GONE);
            }
        }catch (Exception e){
            NightModel.setVisibility(View.GONE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initStatusBar(boolean isTransparent) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if(isTransparent){
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }else{
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        isTrans = isTransparent;
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

    }

    public void selectTab(int num){
        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            for(int i = 0;i < fragments.size(); i++){
                fragmentTransaction.hide(fragments.get(i));
            }
            fragmentTransaction.show(fragments.get(num));
        } catch (ArrayIndexOutOfBoundsException ae) {
            ae.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    protected abstract void initView();

    //获取Intent
    protected void handleIntent(Intent intent) {

    }


    protected int getContentViewId() {
        return R.layout.activity_base;
    }


}
