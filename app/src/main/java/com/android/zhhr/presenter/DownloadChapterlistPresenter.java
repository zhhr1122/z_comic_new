package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.db.DBChapters;
import com.android.zhhr.db.helper.DaoHelper;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.custom.CustomDialog;
import com.android.zhhr.ui.view.IDownloadlistView;
import com.android.zhhr.utils.IntentUtil;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by DELL on 2018/2/12.
 */

public class DownloadChapterlistPresenter extends BasePresenter<IDownloadlistView>{
    public static boolean isLoading;
    private Comic mComic;
    private ComicModule mModel;
    private ArrayList<DBChapters> mLists;
    private DaoHelper helper;
    //下载图片队列
    private LinkedHashMap<String, DownloadComicDisposableObserver> subMap;
    //下载章节队列
    private TreeMap<Integer,DBChapters> downloadMap;
    //从上个页面获取的map
    private HashMap<Integer,Integer> mMap;
    //保存自己选择状态的MAP
    private HashMap<Integer,Integer> selectMap;
    //下载章节数，同时允许存在四个
    private final static int downloadNum = 1;
    //已经下载完成的个数
    int downloadedNum = 0;
    //是否选择了全部
    private boolean isSelectedAll;
    //选择个数
    private int SelectedNum;
    /**
     * 0 下载中
     * 1 停止下载
     * 2 下载完成
     */
    public static final int DOWNLOADING = 0;
    public static final int STOP_DOWNLOAD = 1;
    public static final int FINISH = 2;
    public int isAllDownload = DOWNLOADING;

    private boolean isEditing;

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public DownloadChapterlistPresenter(Activity context, IDownloadlistView view, Intent intent) {
        super(context, view);
        mComic= (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
        selectMap = new HashMap<>();
        this.mModel = new ComicModule(mContext);
        helper = new DaoHelper(context);
        mLists = new ArrayList<>();
        subMap = new LinkedHashMap<>();
        downloadMap = new TreeMap<>();
    }

    public Comic getmComic() {
        return mComic;
    }

    /**
     * 初始化按照章節下載
     */
    public void initData() {
        isLoading = true;
        downloadedNum = 0;
        mLists = new ArrayList<>();
        //把数据存入数据库/从数据库拉取数据
        mModel.getDownloadItemsFromDB(mComic,mMap, new DisposableObserver<List<DBChapters>>() {
            @Override
            public void onNext(@NonNull List<DBChapters> items) {
                if(items!=null&&items.size()!=0){
                    //刷新列表
                    mLists.addAll(items);
                    mView.fillData(mLists);
                    //初始化选择
                    clearSelect();
                    //判断有多少是之前已经下载过的
                    for(int i=0;i<items.size();i++){
                        if(items.get(i).getState() == DownState.FINISH){
                            downloadedNum++;
                        }
                    }
                    //判断是否全部下载完了
                    if(downloadedNum == mLists.size()){
                        mView.onDownloadFinished();
                        mComic.setState(DownState.FINISH);
                    }else{
                        mComic.setState(DownState.DOWN);
                    }
                    //如果不是重新选择了下载章节数进去，不需要更新下载时间
                    if(mMap.size()!=0){
                        mComic.setDownloadTime(getCurrentTime());
                    }
                    mComic.setDownload_num_finish(downloadedNum);
                    mComic.setDownload_num(items.size());
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
                LogUtil.e(mComic.getTitle()+"从数据库中拉取本地下载列表数据失败"+e.toString());
            }

            @Override
            public void onComplete() {
                //拉取之后把新添加的数据整理一下
            }
        });
    }


    /**
     * 开始所有下载
     */
    public void startAll() {
        //isLoading = true;
        //找出最前面四个可以下载的bean
        for(int i=0;i<mLists.size();i++){
            if(mLists.get(i).getState()==DownState.NONE){
                if(downloadMap.size()<downloadNum){
                    startDown(mLists.get(i),i);
                }
            }
        }
    }

    /**
     * 重新下载
     */
    public void ReStartAll() {
        //找出最前面四个可以下载的bean
        //isLoading = true;
        for(int i=0;i<mLists.size();i++){
            if(mLists.get(i).getState()!=DownState.FINISH){
                mLists.get(i).setState(DownState.NONE);
                if(downloadMap.size()<downloadNum){
                    startDown(mLists.get(i),i);
                }else{
                    mView.getDataFinish();
                }
            }
        }
    }

    /**
     * 暂停某个下载
     * @param info
     */
    public void pause(DBChapters info, int position) {
        LogUtil.d("testA","点击了暂停");
        if (info == null) return;
        info.setState(DownState.PAUSE);
        String url = info.getComiclist().get(info.getCurrent_num()+1);
        if (subMap.containsKey(url)){
            DownloadComicDisposableObserver subscriber = subMap.get(url);
            subscriber.dispose();//解除请求
            subMap.remove(url);
            LogUtil.v(url+":暂停下载");
        }
        helper.update(info);
        mView.updateView(position);
    }

    /**
     * 暂停某个下载
     * @param info
     * @param position
     * @param isContinue 是否继续下载,自动去寻找下一个能下载的
     */
    public void stop(DBChapters info, int position, boolean isContinue) {
        if (info == null) return;
        isLoading = false;
        info.setState(DownState.STOP);
        //停止单张图片的下载
        if(info.getComiclist()!=null&&info.getCurrent_num()+1<info.getComiclist().size()){
            String url = info.getComiclist().get(info.getCurrent_num()+1);
            if (subMap.containsKey(url)){
                DownloadComicDisposableObserver subscriber = subMap.get(url);
                subscriber.dispose();//解除请求
                subMap.remove(url);
                LogUtil.v(url+":停止下载");
            }
        }
        //中断整个章节的下载，并且切换章节
        if (downloadMap.containsKey(info.getChapters())&&isContinue){
            downloadMap.remove(info.getChapters());
            for(int i=0;i<mLists.size();i++){
                if(mLists.get(i).getState()==DownState.NONE){
                    downloadMap.put(mLists.get(i).getChapters(),mLists.get(i));
                    startDown(mLists.get(i),i);
                    break;
                }
            }
        }
        helper.update(info);
        mView.updateView(position);
    }

    /**
     * 开始某个下载
     * @param info
     */
    public void startDown(final DBChapters info, final int position) {
        isLoading = true;
        //加入到下载队列中
        downloadMap.put(info.getChapters(),info);
        //首先判断是否已经获取过下载地址
        if(info.getNum()==0){
            //获取下载地址
            mModel.getDownloadChaptersList(mComic,info.getChapters(),new Observer<DBChapters>(){

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    //修改状态
                    info.setState(DownState.START);
                    mView.updateView(position);
                }

                @Override
                public void onNext(@NonNull DBChapters chapters) {
                    //修改状态
                   if(info.getState()!=DownState.STOP){
                       info.setState(DownState.DOWN);
                       //设置下载地址
                       info.setComiclist(chapters.getComiclist());
                       info.setNum(chapters.getComiclist().size());
                       info.setCurrent_num(0);
                       //把获取到的下载地址存进数据库
                       helper.update(info);
                       mView.updateView(position);
                       if(mLists!=null&&mLists.size()!=0){
                           DownloadChapter(info,info.getCurrent_num(),position);
                       }
                   }
                }


                @Override
                public void onError(@NonNull Throwable e) {
                    info.setState(DownState.ERROR);
                    if(downloadMap.containsKey(info.getChapters())){
                        downloadMap.remove(info.getChapters());
                    }
                    mView.updateView(position);
                    //寻找下一话并开始下载
                    for(int i=0;i<mLists.size();i++){
                        if(mLists.get(i).getState()==DownState.NONE){
                            downloadMap.put(mLists.get(i).getChapters(),mLists.get(i));
                            //开始下载下一话
                            startDown(mLists.get(i),i);
                            break;
                        }
                    }
                    LogUtil.e(e.toString());
                }

                @Override
                public void onComplete() {

                }
            });
        }else{
            //修改状态
            info.setState(DownState.DOWN);
            helper.update(info);
            mView.updateView(position);
            DownloadChapter(info,info.getCurrent_num(),position);
        }
    }

    /**
     * 递归下载每一话的所有图片
     * @param info
     * @param page
     */
    private void DownloadChapter(final DBChapters info, final int page, int postion) {
        DownloadComicDisposableObserver observer = new DownloadComicDisposableObserver(info,page,postion);
        //mModel.download(info.getChapters_url().get(page), FileUtil.SDPATH + FileUtil.COMIC + mComic.getId() + "/" + info.getChapters()+"/", page+".png", observer);
        mModel.download(info, page, observer);
        subMap.put(info.getComiclist().get(page),observer);
    }

    public void updateComic() {
        helper.update(mComic);
    }

    public void getResultComic(int resultCode, Intent data) {
        if(resultCode == Constants.OK){
            Comic comic = (Comic) data.getSerializableExtra(Constants.COMIC);
            this.mComic = comic;
        }
    }

    public void onItemClick(DBChapters info, int position) {
        if(isEditing()){
            uptdateToSelected(position);
        }else{
            switch (info.getState()){
                case NONE:
                    stop(info,position,false);
                    break;
                case START:
                    stop(info,position,true);
                    break;
                case PAUSE:
                    //mPresenter.startDown(info,position);
                    break;
                case DOWN:
                    stop(info,position,true);
                    break;
                case STOP:
                    ready(info,position);
                    //mPresenter.startDown(info,position);
                    break;
                case ERROR:
                    ready(info,position);
                    break;
                case  FINISH:
                    ToComicChapter(info);
                    break;
            }
        }
    }

    /**
     * 下载图片的回调
     */
    public class DownloadComicDisposableObserver extends DisposableObserver<DBChapters> {
        int page;
        DBChapters info;
        int position;
        public DownloadComicDisposableObserver(DBChapters info, int page, int position){
            this.page = page;
            this.position = position;
            this.info = info;
        }

        @Override
        public void onNext(@NonNull DBChapters dbChapters) {
            info = dbChapters;
            LogUtil.d(page+"/"+info.getNum()+"下载完成");
            //从队列中移除
            if(subMap.containsKey(info.getComiclist().get(page))){
                subMap.remove(info.getComiclist().get(page));
            }
            //写一个递归继续去下载这一话的下一张图片
            if(page<info.getNum()-1){
                if(info.getState() == DownState.DOWN){
                    DownloadChapter(info,page+1,position);
                }
            }else {
                //如果这一话下载完成
                downloadedNum++;
                //修改mComic的状态
                mComic.setDownload_num_finish(downloadedNum);
                //修改状态
                info.setState(DownState.FINISH);
                if(downloadMap.containsKey(info.getChapters())){
                    downloadMap.remove(info.getChapters());
                }
                //遍历去寻找下一话
                for(int i=0;i<mLists.size();i++){
                    if(mLists.get(i).getState()==DownState.NONE){
                        downloadMap.put(mLists.get(i).getChapters(),mLists.get(i));
                        //开始下载下一话
                        startDown(mLists.get(i),i);
                        break;
                    }
                }
                if(downloadedNum == mLists.size()){
                    mView.ShowToast(mComic.getTitle()+"下载完成,共下载"+downloadedNum+"话");
                    mView.onDownloadFinished();
                    isAllDownload = FINISH;
                    mComic.setState(DownState.FINISH);
                }
                //helper.update(mComic);
            }
            //把已经下载完成的写入
            info.setCurrent_num(page+1);
            //更新数据库
            helper.update(info);
            //为了防止点击了stop之后，仍然刷新UI，即使图片下载已经完成，仍不刷新UI
            if(info.getState() !=DownState.STOP){
                mView.updateView(position);
            }

        }

        @Override
        public void onError(@NonNull Throwable e) {
            info.setState(DownState.ERROR);
            if(downloadMap.containsKey(info.getChapters())){
                downloadMap.remove(info.getChapters());
            }
            mView.updateView(position);
            for(int i=0;i<mLists.size();i++){
                if(mLists.get(i).getState()==DownState.NONE){
                    downloadMap.put(mLists.get(i).getChapters(),mLists.get(i));
                    //开始下载下一话
                    startDown(mLists.get(i),i);
                    break;
                }
            }
            LogUtil.e(e.toString());

        }

        @Override
        public void onComplete() {

        }
    }

    /**
     * 设置为可以下载的等待状态
     * @param items
     * @param position
     */
    public void ready(DBChapters items, int position) {
        if(downloadMap.size()<downloadNum){
            startDown(items,position);
        }else{
            items.setState(DownState.NONE);
            helper.update(items);
            mView.updateView(position);
        }
    }

    public void ToComicChapter(DBChapters info) {
        IntentUtil.ToComicChapterForResult(mContext,info.getChapters(),mComic);
    }

    /**
     * 暂停所有下载
     */
    public void pauseAll() {
        isLoading = false;
        mModel.updateDownloadItemsList(mLists,new DisposableObserver<Boolean>() {

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if(aBoolean){
                    LogUtil.d("所有状态保存在数据库成功");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtil.e(e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 停止所有下载
     */
    public void stopAll() {
        isLoading = false;
        for(int i=0;i<mLists.size();i++){
            DBChapters items = mLists.get(i);
            if (items.getState() != DownState.FINISH){
                items.setState(DownState.STOP);
                if(items.getState() == DownState.DOWN){
                    stop(items,i,false);
                }
                downloadMap.clear();
            }
        }
        mView.getDataFinish();
    }

    /**
     * 选择相关方法
     */
    /**
     * 清除map信息
     */
    public void clearSelect(){
        SelectedNum = 0;
        isSelectedAll = false;
        for(int i=0;i<mLists.size();i++){
            selectMap.put(i,Constants.CHAPTER_FREE);
        }
    }

    /**
     * 选择或者取消选择
     * @param position
     */
    public void uptdateToSelected(int position){
        if(selectMap.get(position)!=null&&selectMap.get(position).equals(Constants.CHAPTER_FREE)){
            SelectedNum++;
            selectMap.put(position,Constants.CHAPTER_SELECTED);
            if(SelectedNum == mLists.size()){
                mView.addAll();
                isSelectedAll = true;
            }
        }else if(selectMap.get(position)!=null&&selectMap.get(position).equals(Constants.CHAPTER_SELECTED)){
            selectMap.put(position,Constants.CHAPTER_FREE);
            SelectedNum--;
            isSelectedAll = false;
            mView.removeAll();
        }
        mView.updateListItem(selectMap,position);
    }

    /**
     * 选择或者移除全部
     */
    public void SelectOrMoveAll(){
        if(!isSelectedAll){
            if(mLists!=null&&mLists.size()!=0){
                for(int i=0;i<mLists.size();i++){
                    if(selectMap.get(i) == Constants.CHAPTER_FREE){
                        selectMap.put(i, Constants.CHAPTER_SELECTED);
                        SelectedNum++;
                    }
                }
                mView.addAll();
            }
        }else{
            if(mLists!=null&&mLists.size()!=0){
                for(int i=0;i<mLists.size();i++){
                    if(selectMap.get(i) == Constants.CHAPTER_SELECTED){
                        selectMap.put(i, Constants.CHAPTER_FREE);
                    }
                }
                SelectedNum = 0;
                mView.removeAll();
            }
        }
        isSelectedAll = !isSelectedAll;
        mView.updateList(selectMap);
    }


    public void ShowDeteleDialog(){
        if(SelectedNum>0){
            final CustomDialog customDialog = new CustomDialog(mContext,mComic.getTitle(),"确认删除选中的漫画章节？");
            customDialog.setListener(new CustomDialog.onClickListener() {
                @Override
                public void OnClickConfirm() {
                    deleteComic();
                    if (customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                }

                @Override
                public void OnClickCancel() {
                    if (customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                }
            });
            customDialog.show();
        }else{
            mView.ShowToast("请选择需要删除的章节数");
        }
    }

    public void deleteComic() {
        List<DBChapters> mDeleteComics = new ArrayList<>();
        for(int i=0;i<mLists.size();i++){
            if(selectMap.get(i) == Constants.CHAPTER_SELECTED){
                mDeleteComics.add(mLists.get(i));
                if(downloadMap.containsKey(mLists.get(i).getChapters())){
                    downloadMap.remove(mLists.get(i).getChapters());//如果删除的的正好在下载队列中，则移除
                }
            }
        }
        mModel.deleteDownloadItem(mDeleteComics, mComic,new DisposableObserver<List<DBChapters>>() {

            @Override
            public void onNext(@NonNull List<DBChapters> items) {
                downloadedNum = 0;
                mLists.clear();
                if(items!=null&&items.size()!=0){
                    //刷新列表
                    mLists.addAll(items);
                    mView.fillData(mLists);
                    //初始化选择
                    clearSelect();
                    //判断有多少是之前已经下载过的
                    for(int i=0;i<items.size();i++){
                        if(items.get(i).getState() == DownState.FINISH){
                            downloadedNum++;
                        }
                    }
                    //判断是否全部下载完了
                    if(downloadedNum == mLists.size()){
                        mView.onDownloadFinished();
                        mComic.setState(DownState.FINISH);
                    }else{
                        mComic.setState(DownState.DOWN);
                    }
                    mComic.setDownload_num_finish(downloadedNum);
                    mComic.setDownload_num(items.size());
                }else{
                    mComic.setStateInte(-1);
                    mContext.finish();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                mView.quitEdit();
            }
        });
    }

}
