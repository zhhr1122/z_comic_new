package com.android.zhhr.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.data.entity.PreloadChapters;
import com.android.zhhr.ui.view.IChapterView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 皓然 on 2017/7/20.
 */

public class ComicChapterPresenter extends BasePresenter<IChapterView>{
    //当前三话漫画
    private PreloadChapters mPreloadChapters;
    //漫画阅读方向
    private int mDirect;
    /**
     * 初始化传入的数据 章节数，章节标题，漫画ID
     */
    private int comic_chapters;
    private List<String> comic_chapter_title;
    private String comic_id;
    //当前页面的长度
    private int comic_size;
    //判断此时是否在加载数据，防止重复加载
    boolean isLoadingdata = false;
    //加载过程中进行的翻页
    private int loadingPosition = 0;

    public ComicChapterPresenter(Activity context, IChapterView view) {
        super(context, view);
        mPreloadChapters = new PreloadChapters();
        mDirect = Constants.LEFT_TO_RIGHT;

    }

    /***
     * 初始化present
     * @param comic_id
     * @param comic_chapters
     * @param comic_chapter_title
     */
    public void init(String comic_id,int comic_chapters,List<String> comic_chapter_title){
        this.comic_chapter_title = comic_chapter_title;
        this.comic_id = comic_id;
        this.comic_chapters = comic_chapters;
    }

    public List<String> getComic_chapter_title() {
        return comic_chapter_title;
    }

    public void setComic_chapter_title(List<String> comic_chapter_title) {
        this.comic_chapter_title = comic_chapter_title;
    }

    public int getmDirect() {
        return mDirect;
    }

    public void setmDirect(int mDirect) {
        this.mDirect = mDirect;
    }


    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
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


    public void loadData(){
        Subscriber subscriber = new Subscriber<PreloadChapters>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(PreloadChapters result) {
                mPreloadChapters = result;
                mView.fillData(result);
                mView.setTitle(comic_chapter_title.get(comic_chapters)+"-1/"+ mPreloadChapters.getNowlist().size());
            }
        };
        comicService.getPreNowChapterList(comic_id,comic_chapters+"")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loadMoreData(int position){
        String chapter_title = null;
        int now_postion =0;
        if(position< mPreloadChapters.getPrelist().size()){
            loadingPosition = position- mPreloadChapters.getPrelist().size()+1;
            chapter_title = comic_chapter_title.get(comic_chapters-1);//前一章
            comic_size = mPreloadChapters.getPrelist().size();
            now_postion =position;
            if(!isLoadingdata){
                isLoadingdata = true;
                loadPreData(comic_id,comic_chapters,Constants.LEFT_TO_RIGHT);
            }
        }else if(position>= mPreloadChapters.getPrelist().size()+ mPreloadChapters.getNowlist().size()){//后一章
            loadingPosition = position - mPreloadChapters.getPrelist().size()- mPreloadChapters.getNowlist().size();
            comic_size = mPreloadChapters.getNextlist().size();
            chapter_title = comic_chapter_title.get(comic_chapters+1);
            now_postion = position - mPreloadChapters.getPrelist().size() - mPreloadChapters.getNowlist().size();
            if(!isLoadingdata){
                isLoadingdata = true;
                loadNextData(comic_id,comic_chapters,Constants.LEFT_TO_RIGHT);;
            }
        }else {//当前章节
            isLoadingdata = false;
            comic_size = mPreloadChapters.getNowlist().size();
            chapter_title = comic_chapter_title.get(comic_chapters);
            now_postion = position - mPreloadChapters.getPrelist().size();
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
        if(Direct == Constants.LEFT_TO_RIGHT){
            title = comic_chapter_title+"-"+(position+1)+"/"+comic_size;
        }else{
            title = comic_chapter_title+"-"+(comic_size-position)+"/"+comic_size;
        }
        mView.setTitle(title);
    }

    /**
     * 加载后一章
     * @param id
     * @param chapter
     * @param direction
     */
    public void loadNextData(String id, int chapter, int direction){

        Subscriber subscriber = new Subscriber<Chapters>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(Chapters result) {
                if(isLoadingdata){
                    mPreloadChapters.setPrelist(mPreloadChapters.getNowlist());
                    mPreloadChapters.setNowlist(mPreloadChapters.getNextlist());
                    mPreloadChapters.setNextlist(result.getComiclist());
                    comic_chapters++;
                    mView.nextChapter(mPreloadChapters,loadingPosition);
                    mView.setTitle(comic_chapter_title.get(comic_chapters)+"-"+(1+loadingPosition)+"/"+ mPreloadChapters.getNowlist().size());
                    isLoadingdata = false;
                }
            }
        };
        comicService.getChapters(id,(chapter+2)+"")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }

    /***
     * 加载前一章
     * @param id
     * @param chapter
     * @param direction
     */
    public void loadPreData(String id, int chapter, int direction){

        Subscriber subscriber = new Subscriber<Chapters>() {
            @Override
            public void onCompleted() {
                mView.getDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mView.ShowToast("获取数据失败"+e.toString());
            }

            @Override
            public void onNext(Chapters result) {
                if(isLoadingdata){
                    mPreloadChapters.setNextlist(mPreloadChapters.getNowlist());
                    mPreloadChapters.setNowlist(mPreloadChapters.getPrelist());
                    mPreloadChapters.setPrelist(result.getComiclist());
                    comic_chapters--;
                    mView.preChapter(mPreloadChapters,loadingPosition);
                    mView.setTitle(comic_chapter_title.get(comic_chapters)+"-"+(mPreloadChapters.getNowlist().size()+loadingPosition)+"/"+ mPreloadChapters.getNowlist().size());
                    isLoadingdata = false;
                }
            }
        };
        comicService.getChapters(id,(chapter-2)+"")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }
}
