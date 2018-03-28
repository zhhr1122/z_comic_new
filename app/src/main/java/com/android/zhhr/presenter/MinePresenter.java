package com.android.zhhr.presenter;

import android.app.Activity;
import android.view.View;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.MineTitle;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.activity.MainActivity;
import com.android.zhhr.ui.custom.CustomDialog;
import com.android.zhhr.ui.view.IMineView;
import com.android.zhhr.utils.GlideCacheUtil;
import com.android.zhhr.utils.IntentUtil;
import com.orhanobut.hawk.Hawk;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import skin.support.SkinCompatManager;

/**
 * Created by zhhr on 2018/3/13.
 */

public class MinePresenter extends BasePresenter<IMineView>{
    private List<MineTitle> mLists;
    private ComicModule mModel;
    private String size;
    private boolean isNight;
    public MinePresenter(Activity context, IMineView view) {
        super(context, view);
        mLists = new ArrayList<>();
        mModel = new ComicModule(context);
    }

    public void loadData() {
        size = GlideCacheUtil.getInstance().getCacheSize(mContext);
        mLists.clear();
        MineTitle mTitle = new MineTitle();
        mTitle.setTitle("夜间模式");
        mTitle.setResID(R.mipmap.icon_night);
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.mipmap.icon_cache);
        mTitle.setTitle("清除缓存");
        mTitle.setSize(size);
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.mipmap.icon_feedback);
        mTitle.setTitle("问题反馈");
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.mipmap.icon_author);
        mTitle.setTitle("关于作者");
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.mipmap.icon_author);
        mTitle.setTitle("检查自动更新");
        mLists.add(mTitle);
        mView.fillData(mLists);
        mView.getDataFinish();
        try{
            isNight = Hawk.get(Constants.MODEL);
        }catch (Exception e){
            isNight = false;
        }
        mView.SwitchSkin(isNight);
    }

    public void onItemClick(int position) {
        switch (position){
            case 0:
                switchSkin();
                break;
            case 1:
                clearCache();
                break;
            case 2:
                try {
                    IntentUtil.toQQchat(mContext,"530414168");
                    mView.ShowToast( "已为您跳转到作者QQ");
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.ShowToast( "请检查是否安装QQ");
                }
                break;
            case 3:
                mView.ShowToast( "已为您跳转到作者博客");
                IntentUtil.toUrl(mContext,"http://blog.csdn.net/zhhr1122");
                break;
            case 4:
                CheckVersion();
                break;
        }
    }

    /**
     * 检查新版本
     */
    private void CheckVersion() {
        PgyUpdateManager.register(mContext, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                mView.ShowToast("没有发现新版本");
            }

            @Override
            public void onUpdateAvailable(String result) {
                final AppBean appBean = getAppBeanFromString(result);
                final CustomDialog dialog = new CustomDialog(mContext,"自动更新","发现新版本:v"+appBean.getVersionName()+",是否更新?");
                dialog.setListener(new CustomDialog.onClickListener() {
                    @Override
                    public void OnClickConfirm() {
                        startDownloadTask(
                                mContext,
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

    /**
     * 更换皮肤
     */
    private void switchSkin() {
        if(isNight){
            ((MainActivity)mContext).setSwitchNightVisible(View.GONE,isNight);
            SkinCompatManager.getInstance().restoreDefaultTheme();
            Hawk.put(Constants.MODEL,Constants.DEFAULT_MODEL);
            mView.SwitchSkin(!isNight);
        }else{
            SkinCompatManager.getInstance().loadSkin("night", new SkinCompatManager.SkinLoaderListener() {
                @Override
                public void onStart() {
                    ((MainActivity)mContext).setSwitchNightVisible(View.GONE,isNight);
                }

                @Override
                public void onSuccess() {
                    Hawk.put(Constants.MODEL,Constants.NIGHT_MODEL);
                    mView.SwitchSkin(isNight);
                }

                @Override
                public void onFailed(String errMsg) {
                    mView.ShowToast("更换失败");
                }
            },SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // load by suffix
        }
        isNight = !isNight;
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        final CustomDialog customDialog = new CustomDialog(mContext,"提示","确认清除漫画所有缓存？(默认"+Constants.CACHE_DAYS+"天清除一次)");
        customDialog.setListener(new CustomDialog.onClickListener() {
            @Override
            public void OnClickConfirm() {
                mModel.clearCache(new DisposableObserver<String>() {

                    @Override
                    public void onNext(@NonNull String s) {
                        GlideCacheUtil.getInstance().clearImageMemoryCache(mContext);
                        size = s;
                        loadData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.ShowToast("清除失败"+e.toString());
                        if (customDialog.isShowing()){
                            customDialog.dismiss();
                        }
                    }

                    @Override
                    public void onComplete() {
                        mView.ShowToast("清除成功");
                        if (customDialog.isShowing()){
                            customDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void OnClickCancel() {
                if (customDialog.isShowing()){
                    customDialog.dismiss();
                }
            }
        });
        customDialog.show();
    }
}
