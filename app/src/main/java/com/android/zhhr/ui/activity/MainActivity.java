package com.android.zhhr.ui.activity;

import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.android.zhhr.R;
import com.android.zhhr.ui.activity.base.BaseFragmentActivity;
import com.android.zhhr.ui.custom.CustomDialog;
import com.android.zhhr.ui.custom.FloatEditLayout;
import com.android.zhhr.ui.custom.SwitchNightRelativeLayout;
import com.android.zhhr.ui.fragment.BookShelfFragment;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.ZToast;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

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
    @Bind(R.id.rl_switch_night)
    SwitchNightRelativeLayout mSwitchNight;

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

       CheckVersion();

    }

    /**
     * 检查新版本
     */
    private void CheckVersion() {
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                LogUtil.d("没有发现新版本");
            }

            @Override
            public void onUpdateAvailable(String result) {
                final AppBean appBean = getAppBeanFromString(result);
                LogUtil.d(appBean.toString());
                final CustomDialog dialog = new CustomDialog(MainActivity.this,"自动更新","发现新版本:v"+appBean.getVersionName()+",是否更新?");
                dialog.setListener(new CustomDialog.onClickListener() {
                    @Override
                    public void OnClickConfirm() {
                        startDownloadTask(
                                MainActivity.this,
                                appBean.getDownloadURL());
                        dialog.dismiss();
                    }

                    @Override
                    public void OnClickCancel() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void quitEdit(){
        setEditBottomVisible(View.GONE);
        bookShelfFragment.quitEdit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
    }


    @OnClick(R.id.btn_home)
    public void toHomeFragment(View view){
        selectTab(0);
        resetBottomBtn();
        mHome.setBackgroundResource(R.drawable.homepage_press);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initStatusBar(true);
        }
    }

    @OnClick(R.id.btn_bookshelf)
    public void toBookShelfFragment(View view){
        selectTab(1);
        resetBottomBtn();
        mBookShelf.setBackgroundResource(R.drawable.bookshelf_press);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initStatusBar(false);
        }
    }

    @OnClick(R.id.btn_mine)
    public void toMineFragment(View view){
        selectTab(2);
        resetBottomBtn();
        mMine.setBackgroundResource(R.drawable.mine_press);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initStatusBar(false);
        }
    }

    public void resetBottomBtn(){
        mHome.setBackgroundResource(R.drawable.homepage);
        mMine.setBackgroundResource(R.drawable.mine);
        mBookShelf.setBackgroundResource(R.drawable.bookshelf);
    }

    public void setEditBottomVisible(int Visible){
        mEditBottom.setVisibility(Visible);
    }

    public void setSwitchNightVisible(int Visible,boolean isNight){
        mSwitchNight.setVisibility(Visible,isNight);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK)){
            if(ZToast.isShow()){
                return super.onKeyDown(keyCode, event);
            }else{
                ZToast.makeText(MainActivity.this,"再按一次返回键退出",1000).show();
                return false;
            }
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
}
