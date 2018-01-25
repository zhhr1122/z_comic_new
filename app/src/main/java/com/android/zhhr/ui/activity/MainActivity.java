package com.android.zhhr.ui.activity;

import android.view.View;
import android.widget.Button;

import com.android.zhhr.R;
import com.android.zhhr.ui.activity.base.BaseFragmentActivity;
import com.android.zhhr.utils.ADUtils;

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

    @Override
    protected void initView() {
        mHome.setBackgroundResource(R.drawable.homepage_press);
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        fragments.add (fragmentManager.findFragmentById(R.id.fm_home));
        fragments.add (fragmentManager.findFragmentById(R.id.fm_bookshelf));
        fragments.add (fragmentManager.findFragmentById(R.id.fm_mine));
        selectTab(0);
        //add start for adsdk
        ADUtils.getAdFullInterstitial(this);
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
