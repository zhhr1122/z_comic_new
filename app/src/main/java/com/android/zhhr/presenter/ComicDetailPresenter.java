package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zhhr.R;
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
    public ComicDetailPresenter(Activity context, IDetailView view) {
        super(context, view);
        this.context = context;
        this.mComic = new Comic();
        this.mModel = new ComicModule(context);
    }

    /**
     * 加载详情
     * @param comic_id
     */
    public void getDetail(String comic_id){
        if(comic_id==null){
            mView.ShowToast("获取ID失败");
        }else{
            mComicId =  Long.parseLong(comic_id);
            mModel.getComicDetail(comic_id,new DisposableObserver<Comic>() {

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
                    mComic.setClickTime(getCurrentTime());
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
        mModel.saveComicToDB(mComic, new Observer<Boolean>() {

            @Override
            public void onError(Throwable e) {
                //mView.ShowToast("保存到数据库失败"+e.toString());
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

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
            LinearLayout ll = (LinearLayout) itemView.getChildAt(0);
            ImageView imageView = (ImageView) ll.getChildAt(0);
            TextView textView = (TextView) ll.getChildAt(1);
            if(!isOrder()){
                if(mComic.getCurrentChapter() == (position+1)){
                    textView.setTextColor(Color.parseColor("#ff9a6a"));
                    textView.setCompoundDrawables(null, null, img_location, null);
                    textView.setCompoundDrawablePadding(DisplayUtil.dip2px(context,10));
                }else{
                    textView.setTextColor(Color.parseColor("#666666"));
                    textView.setCompoundDrawables(null, null, null, null);
                }
                textView.setText((position+1)+" - "+mComic.getChapters().get(position));
                //add start by zhr for sdk
                if(position>mComic.getChapters().size()-10){
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    imageView.setVisibility(View.GONE);
                }
                //add start by zhr for sdk

            }else{
                if(mComic.getChapters().size()-mComic.getCurrentChapter() == position){
                    textView.setTextColor(Color.parseColor("#ff9a6a"));
                    textView.setCompoundDrawables(null, null, img_location, null);
                    textView.setCompoundDrawablePadding(DisplayUtil.dip2px(context,10));
                }else{
                    textView.setTextColor(Color.parseColor("#666666"));
                    textView.setCompoundDrawables(null, null, null, null);
                }
                textView.setText((mComic.getChapters().size()-position)+" - "+mComic.getChapters().get(mComic.getChapters().size()-1-position));
                //add start by zhr for sdk
                if(position<10){
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    imageView.setVisibility(View.GONE);
                }
                //add end

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
