package com.android.zhhr.ui.fragment.base;

import android.app.Activity;

import com.android.zhhr.presenter.BasePresenter;
import com.android.zhhr.ui.activity.MainActivity;
import com.android.zhhr.ui.activity.base.BaseFragmentActivity;

/**
 * Created by 张皓然 on 2018/3/6.
 */

public abstract class BaseBookShelfFragment<P extends BasePresenter> extends BaseFragment<P>{
    protected MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) getActivity();
    }

    public abstract void OnEditList(boolean isEditing);
    public abstract void OnDelete();
    public abstract void OnSelect();
}
