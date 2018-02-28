package com.android.zhhr.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 张皓然 on 2018/2/28.
 */

public class BookShelfFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fraglist;
    public BookShelfFragmentAdapter(FragmentManager fm, List<Fragment> fraglist) {
        super(fm);
        this.fraglist=fraglist;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fraglist.get(arg0);
    }

    @Override
    public int getCount() {
        return fraglist.size();
    }

}
