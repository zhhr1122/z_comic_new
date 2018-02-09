package com.android.zhhr.ui.activity.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.zhhr.R;
import com.android.zhhr.ui.fragment.BaseFragment;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 皓然 on 2017/8/6.
 */

public abstract class BaseFragmentActivity extends RxAppCompatActivity {
    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;
    protected List<Fragment> fragments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initStatusBar();

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
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
