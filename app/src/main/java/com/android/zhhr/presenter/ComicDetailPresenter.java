package com.android.zhhr.presenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.custom.IndexItemView;
import com.android.zhhr.ui.view.IDetailView;
import com.android.zhhr.utils.DisplayUtil;
import com.android.zhhr.utils.ShowErrorTextUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


/**
 * Created by 皓然 on 2017/7/12.
 */

public class ComicDetailPresenter extends  BasePresenter<IDetailView>{
    private Context context;
    private long mComicId;
    private boolean isOrder;
    private String comic_id;
    private int from;

    public Comic getmComic() {
        return mComic;
    }

    public void setmComic(Comic mComic) {
        this.mComic = mComic;
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

    private Comic mComic;
    private ComicModule mModel;
    public ComicDetailPresenter(Activity context, IDetailView view, Intent intent) {
        super(context, view);
        this.context = context;
        this.mComic = new Comic();
        this.mModel = new ComicModule(context);
        this.comic_id = intent.getStringExtra(Constants.COMIC_ID);
        //默认漫画都来自腾讯
        this.from = intent.getIntExtra(Constants.COMIC_FROM,0);
    }

    /**
     * 加载详情
     */
    public void getDetail(){
        if(comic_id==null){
            mView.ShowToast("获取ID失败");
        }else{
            mComicId =  Long.parseLong(comic_id);
            mModel.getComicDetail(comic_id,from,new DisposableObserver<Comic>() {

                @Override
                public void onError(Throwable throwable) {
                    mView.showErrorView(ShowErrorTextUtil.ShowErrorText(throwable));
                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onNext(Comic comic) {
                    comic.setId(mComicId);
                    mComic = comic;
                    SaveComicToDB(mComic);
                    mView.fillData(comic);
                    //mView.ShowToast("之前看到"+(mComic.getCurrentChapter())+"话");
                }
            });
        }
    }

    public void getCurrentChapters(){
        mModel.getComicFromDB(mComicId, new DisposableObserver<Comic>() {

            @Override
            public void onError(Throwable e) {
                //mView.ShowToast("获取当前章节数目失败"+e.toString());
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(Comic comic) {
                if(comic!=null){
                    mComic=comic;
                    mView.setCurrent(comic.getCurrentChapter()+1);
                }
            }
        });
    }

    public void collectComic(boolean isCollected){
        mComic.setCollectTime(getCurrentTime());
        mComic.setIsCollected(isCollected);
        mModel.updateComicToDB(mComic, new DisposableObserver<Boolean>() {

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("已经收藏");
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(Boolean result) {
                if(result){
                    mView.setCollect(mComic.getIsCollected());
                }
            }
        });
    }

    public void SaveComicToDB(Comic mComic){
        mModel.saveComicToDB(mComic, new DisposableObserver<Boolean>() {

            @Override
            public void onError(Throwable e) {
                //mView.ShowToast("保存到数据库失败"+e.toString());
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(Boolean CanSelect) {
               /* if(CanSelect){
                    mView.ShowToast("保存到数据库成功");
                }else{
                    mView.ShowToast("收藏失败");
                }*/
            }

        });
    }


    public void orderIndex(LinearLayout mlayout) {
        Drawable img_location = context.getResources().getDrawable(R.mipmap.location);
        img_location.setBounds(0, 0, img_location.getMinimumWidth(), img_location.getMinimumHeight());
        for(int position=0;position<mComic.getChapters().size();position++){
            IndexItemView itemView = (IndexItemView) mlayout.getChildAt(position);
            TextView textView = itemView.getmTitle();
            if(!isOrder()){
                if(mComic.getCurrentChapter() == position){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setTextAppearance(R.style.colorTextPrimary);
                    }else{
                        textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    }
                    textView.setCompoundDrawables(null, null, img_location, null);
                    textView.setCompoundDrawablePadding(DisplayUtil.dip2px(context,10));
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setTextAppearance(R.style.colorTextBlack);
                    }else{
                        textView.setTextColor(ContextCompat.getColor(context,R.color.colorTextBlack));
                    }
                    textView.setCompoundDrawables(null, null, null, null);
                }
                textView.setText((position+1)+" - "+mComic.getChapters().get(position));
            }else{
                if(mComic.getChapters().size()-mComic.getCurrentChapter() == position+1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setTextAppearance(R.style.colorTextPrimary);
                    }else{
                        textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    }
                    textView.setCompoundDrawables(null, null, img_location, null);
                    textView.setCompoundDrawablePadding(DisplayUtil.dip2px(context,10));
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setTextAppearance(R.style.colorTextBlack);
                    }else {
                        textView.setTextColor(ContextCompat.getColor(context,R.color.colorTextBlack));
                    }
                    textView.setCompoundDrawables(null, null, null, null);
                }
                textView.setText((mComic.getChapters().size()-position)+" - "+mComic.getChapters().get(mComic.getChapters().size()-1-position));
            }
        }
        if(!isOrder()){
            mView.OrderData(R.mipmap.zhengxu);
        }else{
            mView.OrderData(R.mipmap.daoxu);
        }
    }

    /**
     * 添加banner广告
     * @param context
     * @param mAdBanner
     */
    public void getAdBanner(Context context,ViewGroup mAdBanner) {
       /* ZAdComponent banner = ZAdSdk.getInstance().createAd(context,ZAdType.BANNER, "1001");
        mAdBanner.addView(banner.getContentView()); // 添加到父视图里
        ZAdSdk.getInstance().getLoader().loadAd(banner);*/
    }
}
