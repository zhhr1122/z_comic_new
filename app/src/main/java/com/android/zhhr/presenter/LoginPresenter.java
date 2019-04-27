package com.android.zhhr.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.ILoginView;

import io.reactivex.observers.DisposableObserver;

public class LoginPresenter extends BasePresenter<ILoginView>{
    private ComicModule mModel;

    public LoginPresenter(Activity context){
        super(context, (ILoginView) context);
        mModel = new ComicModule(context);
    }


    public void login(EditText username, EditText password) {
        String usernameText = username.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        if(TextUtils.isEmpty(usernameText)){
            mView.onLoginFailed();
            mView.ShowToast("用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(passwordText)){
            mView.onLoginFailed();
            mView.ShowToast("请输入密码");
            return;
        }
        mModel.login(usernameText, passwordText, new DisposableObserver<Boolean>() {

            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                    mView.ShowToast("登录成功");
                }else {
                    mView.ShowToast("登录失败");
                }

            }

            @Override
            public void onError(Throwable e) {
                mView.onLoginFailed();
            }

            @Override
            public void onComplete() {
                mView.onLoginSuccess();
            }
        });
    }
}
