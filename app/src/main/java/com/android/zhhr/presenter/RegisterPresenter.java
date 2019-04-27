package com.android.zhhr.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IRegisterView;

import io.reactivex.observers.DisposableObserver;

public class RegisterPresenter extends BasePresenter<IRegisterView>{
    private ComicModule mModel;

    public RegisterPresenter(Activity context){
        super(context, (IRegisterView) context);
        mModel = new ComicModule(context);
    }

    public void register(EditText username, EditText password, EditText passwordRe, EditText describe) {
        String usernameText = username.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String describeText = describe.getText().toString().trim();
        if(TextUtils.isEmpty(usernameText)){
            mView.onRegisterFailed();
            mView.ShowToast("用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(passwordText)){
            mView.onRegisterFailed();
            mView.ShowToast("请输入密码");
            return;
        }
        if(!passwordText.equals(passwordRe.getText().toString().trim())){
            mView.onRegisterFailed();
            mView.ShowToast("两次输入密码不一致");
            return;
        }
        mModel.register(usernameText, passwordText, describeText, new DisposableObserver<Boolean>() {

            @Override
            public void onNext(Boolean aBoolean) {
                mView.ShowToast("注册成功");
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("此用户名已被注册");
                mView.onRegisterFailed();
            }

            @Override
            public void onComplete() {
                mView.onRegisterSuccess();
            }
        });
    }
}
