package com.android.zhhr.ui.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.android.zhhr.R;
import com.android.zhhr.ui.activity.base.BaseFragmentActivity;
import com.android.zhhr.ui.custom.FloatEditLayout;
import com.android.zhhr.ui.fragment.BookShelfFragment;

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
    @Bind(R.id.rl_edit_bottom)
    FloatEditLayout mEditBottom;
    BookShelfFragment bookShelfFragment;


    public FloatEditLayout getmEditBottom() {
        return mEditBottom;
    }

    @Override
    protected void initView() {
        mHome.setBackgroundResource(R.drawable.homepage_press);
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        bookShelfFragment= (BookShelfFragment) fragmentManager.findFragmentById(R.id.fm_bookshelf);
        fragments.add (fragmentManager.findFragmentById(R.id.fm_home));
        fragments.add (bookShelfFragment);
        fragments.add (fragmentManager.findFragmentById(R.id.fm_mine));
        mEditBottom.setListener(new FloatEditLayout.onClickListener() {
            @Override
            public void OnClickSelect() {
                bookShelfFragment.OnClickSelect();
            }

            @Override
            public void OnDelete() {
                bookShelfFragment.OnClickDelete();
            }
        });
        selectTab(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_home)
    public void toHomeFragment(View view){
        selectTab(0);
        resetBottomBtn();
        mHome.setBackgroundResource(R.drawable.homepage_press);
        initStatusBar(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_bookshelf)
    public void toBookShelfFragment(View view){
        selectTab(1);
        resetBottomBtn();
        mBookShelf.setBackgroundResource(R.drawable.bookshelf_press);
        initStatusBar(false);
    }

    public void resetBottomBtn(){
        mHome.setBackgroundResource(R.drawable.homepage);
        mMine.setBackgroundResource(R.drawable.mine);
        mBookShelf.setBackgroundResource(R.drawable.bookshelf);
    }

    public void setEditBottomVisible(int Visible){
        mEditBottom.setVisibility(Visible);
    }
}
