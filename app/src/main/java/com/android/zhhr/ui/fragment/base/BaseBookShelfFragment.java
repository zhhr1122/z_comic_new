package com.android.zhhr.ui.fragment.base;

import com.android.zhhr.presenter.BasePresenter;

/**
 * Created by 张皓然 on 2018/3/6.
 */

public abstract class BaseBookShelfFragment<P extends BasePresenter> extends BaseFragment<P>{
    public abstract void OnEditList(boolean isEditing);
}
