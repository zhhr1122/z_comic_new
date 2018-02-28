package com.android.zhhr.ui.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.zhhr.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 张皓然 on 2018/2/28.
 */

public class CustomDialog extends AlertDialog {
    private onClickListener listener;

    @Bind(R.id.tv_content)
    TextView mContent;
    @Bind(R.id.tv_title)
    TextView mTitle;
    String title;
    String content;
    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    public CustomDialog(Context context,String title,String content) {
        this(context, R.style.MyDialog);
        this.title = title;
        this.content = content;
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 预先设置Dialog的一些属性
       setContentView(R.layout.dialog_default);
        ButterKnife.bind(this);
        mTitle.setText(title);
        mContent.setText(content);
    }

    @OnClick(R.id.tv_cancel)
    public void clickCancel(){
        if(listener!=null){
            listener.OnClickCancel();
        }
    }

    @OnClick(R.id.tv_confirm)
    public void clickConfirm(){
        if(listener!=null){
            listener.OnClickConfirm();
        }
    }

    public interface onClickListener{
        void OnClickConfirm();
        void OnClickCancel();
    }
}



