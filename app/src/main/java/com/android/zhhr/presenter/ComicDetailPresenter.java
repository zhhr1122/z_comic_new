package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.model.ComicModel;
import com.android.zhhr.ui.custom.IndexItemView;
import com.android.zhhr.ui.view.IDetailView;
import com.android.zhhr.ui.view.IMainView;
import com.android.zhhr.utils.ShowErrorTextUtil;
import com.android.zhhr.utils.TencentComicAnalysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 皓然 on 2017/7/12.
 */

public class ComicDetailPresenter extends  BasePresenter<IDetailView>{
    private Context context;
    private String mComicId;
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
    private ComicModel mModel;
    public ComicDetailPresenter(Activity context, IDetailView view) {
        super(context, view);
        this.context = context;
        this.mComic = new Comic();
        this.mModel = new ComicModel(context);
    }

    /**
     * 加载详情
     * @param comic_id
     */
    public void getDetail(String comic_id){
        if(comic_id==null){
            mView.ShowToast("获取ID失败");
        }else{
            mComicId = comic_id;
            mModel.getCmoicDetail(mComicId,new Subscriber<Comic>() {
                @Override
                public void onCompleted() {
                    mView.getDataFinish();
                }

                @Override
                public void onError(Throwable throwable) {
                    mView.showErrorView(ShowErrorTextUtil.ShowErrorText(throwable));
                }

                @Override
                public void onNext(Comic comic) {
                    comic.setId(mComicId);
                    mComic = comic;
                    mView.fillData(comic);
                }
            });
        }
    }


    public void orderIndex(LinearLayout mlayout) {
        for(int position=0;position<mComic.getChapters().size();position++){
            IndexItemView itemView = (IndexItemView) mlayout.getChildAt(position);
            TextView textView = (TextView) itemView.getChildAt(0);
            if(!isOrder()){
                textView.setText((position+1)+" - "+mComic.getChapters().get(position));
            }else{
                textView.setText((mComic.getChapters().size()-position)+" - "+mComic.getChapters().get(mComic.getChapters().size()-1-position));
            }
        }
        if(!isOrder()){
            mView.OrderData(R.mipmap.zhengxu);
        }else{
            mView.OrderData(R.mipmap.daoxu);
        }
    }
}
