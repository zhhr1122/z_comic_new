package com.android.zhhr.ui.activity.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.presenter.BasePresenter;
import com.android.zhhr.utils.ToastUtils;
import com.orhanobut.hawk.Hawk;
import com.pgyersdk.crash.PgyCrashManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by 皓然 on 2017/6/15.
 */

public abstract class BaseActivity<P extends BasePresenter> extends RxAppCompatActivity {

    protected View NightModel;
    protected boolean isNight;

    private ConnectivityManager manager;
    //未指定类型的Presenter
    protected P mPresenter;
    //初始化Presenter

    protected boolean isTrans;
    protected abstract void initPresenter(Intent intent);
    //设置布局
    protected abstract int getLayout();
    //初始化布局
    protected abstract void initView();

    protected ToastUtils toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initStatusBar(true);
        }
        //初始化ButterKnife
        ButterKnife.bind(this);
        initPresenter(getIntent());
        checkPresenterIsNull();
        toast = new ToastUtils(this);
        initView();
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

    protected void switchModel() {
        NightModel  = findViewById(R.id.v_night);
        try{
            isNight = Hawk.get(Constants.MODEL);
            if(isNight){
                NightModel.setVisibility(View.VISIBLE);
            }else{
                NightModel.setVisibility(View.GONE);
            }
        }catch (Exception e){
            NightModel.setVisibility(View.GONE);
        }
    }


    private void checkPresenterIsNull() {
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter in initPresenter() method ");
        }
    }

    //设置打印方法
    public void showToast(String text){
        toast.showToast(text);
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /***
     * 获取屏幕宽度
     * @return 屏幕宽度（px）
     */
    public int getMobileWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return  width;
    }

    /**
     * 获取屏幕高度
     * @return 屏幕高度(px)
     */
    public int getMobileHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return  height;
    }

    /**
     * 获取状态栏高度
     * @return 高度（px）
     */
    public int getStatusBarHeight(){
        int result = 0;
        int resultId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resultId>0){
            result =getResources().getDimensionPixelSize(resultId);
        }
        return result;
    }

    /**
     * 检查网络
     * @return 是否有网络
     */
    public boolean checkNetworkState(){
        boolean flag = false;
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getActiveNetworkInfo() != null){
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

}
