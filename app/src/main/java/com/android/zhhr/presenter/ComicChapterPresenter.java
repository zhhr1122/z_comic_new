package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.data.entity.db.DBChapters;
import com.android.zhhr.db.helper.DaoHelper;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IChapterView;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.ShowErrorTextUtil;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import skin.support.SkinCompatManager;

/**
 * Created by 皓然 on 2017/7/20.
 */

public class ComicChapterPresenter extends BasePresenter<IChapterView>{
    public static boolean isLoading;
    private ComicModule mModel;
    //当前三话漫画
    private PreloadChapters mPreloadChapters;
    //漫画阅读方向
    private int mDirect;
    /**
     * 初始化传入的数据 章节数，章节标题，漫画ID
     */
    private int comic_chapters;
    private List<String> comic_chapter_title;
    private long comic_id;
    //当前页面的长度
    private int comic_size;
    //判断此时是否在加载数据，防止重复加载
    boolean isLoadingdata = false;
    //加载过程中进行的翻页
    private int loadingPosition = 0;

    private int loadingDy = 0;

    private DaoHelper mHelper;

    private Comic mComic;

    public Comic getmComic() {
        return mComic;
    }

    public int getCurrentPage(){
        return mComic.getCurrent_page()-1;
    }

    /* private ZAdComponent mReWardAd;
    private ZAdComponent mPreAd;
    private ZAdComponent mVideoAd;*/

    public ComicChapterPresenter(Activity context, IChapterView view) {
        super(context, view);
        mModel = new ComicModule(context);
        mDirect = Constants.LEFT_TO_RIGHT;
        mHelper = new DaoHelper(context);
    }

    /***
     * 初始化present
     */
    public void init(Comic comic,int Chapters){
        this.mComic= comic;
        isLoading = true;
        Comic DBComic = (Comic) mHelper.findComic(comic.getId());
        //判断如果是点进上次点击的那一话
        if(DBComic!=null&&DBComic.getCurrentChapter() != Chapters){
            mComic.setCurrent_page(1);
        }
        this.comic_chapter_title = comic.getChapters();
        this.comic_id = comic.getId();
        this.comic_chapters = Chapters;
        this.mDirect = comic.getReadType();
    }

    public int getLoadingDy() {
        return loadingDy;
    }

    public void setLoadingDy(int loadingDy) {
        this.loadingDy = loadingDy;
    }

    public List<String> getComic_chapter_title() {
        return comic_chapter_title;
    }

    public void setComic_chapter_title(List<String> comic_chapter_title) {
        this.comic_chapter_title = comic_chapter_title;
    }


    public void setReaderModuel(int mDirect){
        mComic.setReadType(mDirect);
        mHelper.update(mComic);
        mView.SwitchModel(mDirect);
    }


    public long getComic_id() {
        return comic_id;
    }

    public void setComic_id(long comic_id) {
        this.comic_id = comic_id;
    }


    public int getComic_chapters() {
        return comic_chapters;
    }

    public void setComic_chapters(int comic_chapters) {
        this.comic_chapters = comic_chapters;
    }

    public PreloadChapters getmPreloadChapters() {
        return mPreloadChapters;
    }

    public void setmPreloadChapters(PreloadChapters mPreloadChapters) {
        this.mPreloadChapters = mPreloadChapters;
    }

    public int getmDirect() {
        return mDirect;
    }

    public void setmDirect(int mDirect) {
        this.mDirect = mDirect;
    }


    public void loadData(){
        mPreloadChapters = new PreloadChapters();
        final int[] page = new int[3];
        final int[] all_page = new int[3];
        for(int i=0;i<3;i++){
            final int finalI = i;
            mModel.getChaptersList(mComic, new OnProgressListener() {
                @Override
                public void OnProgress(final int num,int all) {
                    //如果是酷酷动漫，则显示一下加载进度
                    page[finalI] = num;
                    all_page[finalI] = all;
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(all_page[0]!=0||all_page[1]!=0||all_page[2]!=0){
                                mView.showErrorView("已加载"+(page[0]+page[1]+page[2])+"页"+"/共"+(all_page[0]+all_page[1]+all_page[2])+"页");
                            }
                        }
                    });
                }
            },(comic_chapters-1+i),new DisposableObserver<DBChapters>() {
                @Override
                public void onNext(@NonNull DBChapters chapters) {
                    //分别设置三个章节
                    if(comic_chapters-1 == chapters.getChapters()){
                        if(comic_chapters-1<0){
                            mPreloadChapters.setPrelist(new ArrayList<String>());
                        }
                        mPreloadChapters.setPrelist(chapters.getComiclist());
                    }else if(comic_chapters == chapters.getChapters()){
                        mPreloadChapters.setNowlist(chapters.getComiclist());
                    }else{
                        if(comic_chapters+1>comic_chapter_title.size()){
                            mPreloadChapters.setNextlist(new ArrayList<String>());
                        }
                        mPreloadChapters.setNextlist(chapters.getComiclist());
                    }
                    //三个章节都不为NULL
                    if(mPreloadChapters.isNotNull()){
                        if(mPreloadChapters.getNowSize() == 1){
                            mView.showErrorView("该章节是腾讯付费章节，处于版权问题不以展示，请去腾讯观看");
                        }else{
                            mView.fillData(mPreloadChapters);
                            mView.setTitle(comic_chapter_title.get(comic_chapters)+"-"+mComic.getCurrent_page()+"/"+ mPreloadChapters.getNowlist().size());
                            mView.getDataFinish();
                            updateComic(mComic.getCurrent_page());
                        }
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    //mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
                    ArrayList<String> temp = new ArrayList<>();
                    if(mPreloadChapters.getPrelist()==null){
                        mPreloadChapters.setPrelist(temp);
                    }
                    if(mPreloadChapters.getNowlist()==null){
                        mPreloadChapters.setNowlist(temp);
                    }
                    if(mPreloadChapters.getNextlist()==null){
                        mPreloadChapters.setNextlist(temp);
                    }
                    if(mPreloadChapters.getSize()>0){
                        mView.fillData(mPreloadChapters);
                        mView.setTitle(comic_chapter_title.get(comic_chapters)+"-1/"+ mPreloadChapters.getNowlist().size());
                        mView.getDataFinish();
                    }else{
                        if(e instanceof  IndexOutOfBoundsException){ //防止最后一话数组越界报错
                            return;
                        }
                        mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        }

    }

    /**
     * 保存当前章节数目到数据库
     */
    public void updateComic(int current_page) {
        mComic.setCurrent_page(current_page);
        mComic.setCurrent_page_all(mPreloadChapters.getNowSize());
        mComic.setClickTime(getCurrentTime());
        mComic.setCurrentChapter(comic_chapters);
        mHelper.update(mComic);
        Intent intent = new Intent();
        intent.putExtra(Constants.COMIC,mComic);
        mContext.setResult(Constants.OK,intent);
    }

    public void loadMoreData(int position,int mDirect,int offset){
        String chapter_title = null;
        this.mDirect = mDirect;
        int now_postion =0;
        switch (mDirect){
            case Constants.UP_TO_DOWN:
                if(position< mPreloadChapters.getPrelist().size()){
                    loadingPosition = position- mPreloadChapters.getPrelist().size()+1;
                    chapter_title = comic_chapter_title.get(comic_chapters-1);//前一章
                    comic_size = mPreloadChapters.getPrelist().size();
                    now_postion =position;
                    if(!isLoadingdata){
                        isLoadingdata = true;
                        loadPreData(comic_chapters,loadingDy);
                    }
                }else if(position>= mPreloadChapters.getPrelist().size()+ mPreloadChapters.getNowlist().size()){//后一章
                    loadingPosition = position - mPreloadChapters.getPrelist().size()- mPreloadChapters.getNowlist().size();
                    comic_size = mPreloadChapters.getNextlist().size();
                    chapter_title = comic_chapter_title.get(comic_chapters+1);
                    now_postion = position - mPreloadChapters.getPrelist().size() - mPreloadChapters.getNowlist().size();
                    if(!isLoadingdata){
                        isLoadingdata = true;
                        loadNextData(comic_chapters,loadingDy);
                    }
                }else {//当前章节
                    isLoadingdata = false;
                    comic_size = mPreloadChapters.getNowlist().size();
                    chapter_title = comic_chapter_title.get(comic_chapters);
                    now_postion = position - mPreloadChapters.getPrelist().size();
                }
                break;
            case Constants.LEFT_TO_RIGHT:
                if(position< mPreloadChapters.getPrelist().size()){
                    loadingPosition = position- mPreloadChapters.getPrelist().size()+1;
                    chapter_title = comic_chapter_title.get(comic_chapters-1);//前一章
                    comic_size = mPreloadChapters.getPrelist().size();
                    now_postion =position;
                    if(!isLoadingdata){
                        isLoadingdata = true;
                        loadPreData(comic_chapters,0);
                    }
                }else if(position>= mPreloadChapters.getPrelist().size()+ mPreloadChapters.getNowlist().size()){//后一章
                    loadingPosition = position - mPreloadChapters.getPrelist().size()- mPreloadChapters.getNowlist().size();
                    comic_size = mPreloadChapters.getNextlist().size();
                    chapter_title = comic_chapter_title.get(comic_chapters+1);
                    now_postion = position - mPreloadChapters.getPrelist().size() - mPreloadChapters.getNowlist().size();
                    if(!isLoadingdata){
                        isLoadingdata = true;
                        loadNextData(comic_chapters,0);
                    }
                }else {//当前章节
                    isLoadingdata = false;
                    comic_size = mPreloadChapters.getNowlist().size();
                    chapter_title = comic_chapter_title.get(comic_chapters);
                    now_postion = position - mPreloadChapters.getPrelist().size();
                }
                break;
            case Constants.RIGHT_TO_LEFT:
                if(position< mPreloadChapters.getNextlist().size()){
                    loadingPosition = position- mPreloadChapters.getNextlist().size()+1;
                    chapter_title = comic_chapter_title.get(comic_chapters+1);//后一章
                    comic_size = mPreloadChapters.getNextlist().size();
                    now_postion = position;
                    if(!isLoadingdata){
                        isLoadingdata = true;
                        loadNextData(comic_chapters,0);
                    }
                }else if(position>= mPreloadChapters.getNextlist().size()+ mPreloadChapters.getNowlist().size()){//前一章
                    loadingPosition = position - mPreloadChapters.getNextlist().size()- mPreloadChapters.getNowlist().size();
                    comic_size = mPreloadChapters.getPrelist().size();
                    chapter_title = comic_chapter_title.get(comic_chapters-1);
                    now_postion = position - mPreloadChapters.getNextlist().size() - mPreloadChapters.getNowlist().size();
                    if(!isLoadingdata){
                        isLoadingdata = true;
                        loadPreData(comic_chapters,0);
                    }
                }else {//当前章节
                    isLoadingdata = false;
                    comic_size = mPreloadChapters.getNowlist().size();
                    chapter_title = comic_chapter_title.get(comic_chapters);
                    now_postion = position - mPreloadChapters.getNextlist().size();
                }
                break;
        }
        setTitle(chapter_title,comic_size,now_postion,mDirect);

    }

    public void clickScreen(float x,float y){
        if (x<0.336){
            mView.prePage();
        }else if(x<0.666){
            if(!isLoadingdata){
                mView.showMenu();
            }else{
                mView.ShowToast("正在载入，请稍后");
            }
        }else {
            mView.nextPage();
        }
    }

    public void setTitle(String comic_chapter_title, int comic_size, int position,int Direct){
        String title = null;
        if(Direct == Constants.LEFT_TO_RIGHT||Direct == Constants.UP_TO_DOWN){
            title = comic_chapter_title+"-"+(position+1)+"/"+comic_size;
            updateComic(position+1);
        }else{
            title = comic_chapter_title+"-"+(comic_size-position)+"/"+comic_size;
            updateComic(comic_size-position);
        }
        mView.setTitle(title);
    }

    /**
     * 加载后一章
     * @param chapter
     */
    public void loadNextData(int chapter, final int offset){

        mModel.getChaptersList(mComic, new OnProgressListener() {
            @Override
            public void OnProgress(int i,int all) {

            }
        }, (chapter + 2), new DisposableObserver<DBChapters>() {

            @Override
            public void onError(Throwable e) {
                DBChapters chapters = new DBChapters();
                chapters.setComiclist(new ArrayList<String>());
                onNext(chapters);
                isLoadingdata = false;
                //mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onComplete() {
                isLoadingdata = false;
                mView.getDataFinish();
            }


            @Override
            public void onNext(@NonNull DBChapters chapters) {
                if (isLoadingdata) {
                    mPreloadChapters.setPrelist(mPreloadChapters.getNowlist());
                    mPreloadChapters.setNowlist(mPreloadChapters.getNextlist());
                    if(chapters.getComiclist().size()==1){
                        mPreloadChapters.setNextlist(new ArrayList<String>());
                    }else{
                        mPreloadChapters.setNextlist(chapters.getComiclist());
                    }
                    comic_chapters++;
                    int mPosition;
                    if (mDirect == Constants.RIGHT_TO_LEFT) {
                        mPosition = mPreloadChapters.getNextlist().size() + mPreloadChapters.getNowlist().size() - 1 + loadingPosition;//关闭切换动画
                        mView.setTitle(comic_chapter_title.get(comic_chapters) + "-" + (1 - loadingPosition) + "/" + mPreloadChapters.getNowlist().size());
                    } else {
                        mPosition = mPreloadChapters.getPrelist().size() + loadingPosition;
                        mView.setTitle(comic_chapter_title.get(comic_chapters) + "-" + (1 + loadingPosition) + "/" + mPreloadChapters.getNowlist().size());
                    }
                    mView.nextChapter(mPreloadChapters, mPosition, offset);
                    updateComic(1);
                }
            }
        });
    }

    /***
     * 加载前一章
     * @param chapter
     */
    public void loadPreData(final int chapter, final int offset){
        mModel.getChaptersList(mComic, new OnProgressListener() {
            @Override
            public void OnProgress(int i, int all) {

            }

        }, chapter - 2, new DisposableObserver<DBChapters>() {

            @Override
            public void onError(Throwable e) {
                DBChapters chapters = new DBChapters();
                chapters.setComiclist(new ArrayList<String>());
                onNext(chapters);
                isLoadingdata = false;
                //mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onComplete() {
                isLoadingdata = false;
                mView.getDataFinish();
            }

            @Override
            public void onNext(@NonNull DBChapters chapters) {
                if (isLoadingdata) {
                    mPreloadChapters.setNextlist(mPreloadChapters.getNowlist());
                    mPreloadChapters.setNowlist(mPreloadChapters.getPrelist());
                    if(chapters.getComiclist().size()==1){
                        mPreloadChapters.setPrelist(new ArrayList<String>());
                    }else{
                        mPreloadChapters.setPrelist(chapters.getComiclist());
                    }
                    comic_chapters--;
                    int mPosition;
                    if (mDirect == Constants.RIGHT_TO_LEFT) {
                        mPosition = mPreloadChapters.getNextlist().size() + loadingPosition;//关闭切换动画
                        mView.setTitle(comic_chapter_title.get(comic_chapters) + "-" + (mPreloadChapters.getNowlist().size() - loadingPosition) + "/" + mPreloadChapters.getNowlist().size());
                    } else {
                        mPosition = mPreloadChapters.getPrelist().size() + mPreloadChapters.getNowlist().size() + loadingPosition - 1;
                        mView.setTitle(comic_chapter_title.get(comic_chapters) + "-" + (mPreloadChapters.getNowlist().size() + loadingPosition) + "/" + mPreloadChapters.getNowlist().size());
                    }
                    mView.preChapter(mPreloadChapters, mPosition, offset);
                    updateComic(1);
                }
            }
        });
    }

    public void switchNight(final boolean isNight) {
        if(isNight){
            mView.setSwitchNightVisible(View.GONE,isNight);
            SkinCompatManager.getInstance().restoreDefaultTheme();
            Hawk.put(Constants.MODEL,Constants.DEFAULT_MODEL);
            mView.SwitchSkin();
        }else{
            SkinCompatManager.getInstance().loadSkin("night", new SkinCompatManager.SkinLoaderListener() {
                @Override
                public void onStart() {
                    mView.setSwitchNightVisible(View.GONE,isNight);
                }

                @Override
                public void onSuccess() {
                    Hawk.put(Constants.MODEL,Constants.NIGHT_MODEL);
                    mView.SwitchSkin();
                }

                @Override
                public void onFailed(String errMsg) {
                    mView.ShowToast("更换失败");
                }
            },SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // load by suffix

        }
    }

    public void interruptThread() {
        LogUtil.d("执行了false");
        isLoading = false;
    }

    public interface OnProgressListener{
        void OnProgress(int i,int all);
    }
}
