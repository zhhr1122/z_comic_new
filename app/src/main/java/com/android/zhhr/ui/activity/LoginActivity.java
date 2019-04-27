package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.zhhr.R;
import com.android.zhhr.presenter.LoginPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.view.ILoginView;
import com.android.zhhr.utils.IntentUtil;
import com.android.zhhr.utils.ZToast;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {
    @Bind(R.id.et_username)
    EditText username;
    @Bind(R.id.et_password)
    EditText password;
    @Bind(R.id.pb_progress)
    ProgressBar mProgressBar;
    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new LoginPresenter(this);
    }

    @OnClick(R.id.tv_to_register)
    public void toRegister(){
        IntentUtil.toRegisterActivity(this);
    }

    @OnClick(R.id.tv_login)
    public void login(){
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.login(username,password);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onLoginSuccess() {
        finish();
    }

    @Override
    public void onLoginFailed() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void ShowToast(String t) {
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.GONE);
        }
        ZToast.makeText(LoginActivity.this,t,1000).show();
    }

    @OnClick(R.id.iv_back_color)
    public void toFinish(){
        finish();
    }

    @OnClick(R.id.iv_login)
    public void loginByOther(){
        ZToast.makeText(LoginActivity.this,"暂未开通",1000).show();
    }
}
