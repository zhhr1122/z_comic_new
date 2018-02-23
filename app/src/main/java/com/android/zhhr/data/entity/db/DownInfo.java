package com.android.zhhr.data.entity.db;

import com.android.zhhr.data.entity.BaseBean;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.net.ComicService;
import com.android.zhhr.net.ProgressListener;
import com.android.zhhr.net.download.HttpDownOnNextListener;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 张皓然 on 2018/2/13.
 */
@Entity
public class DownInfo extends BaseBean{

    @Id
    protected long id;
    /*漫画ID*/
    protected long comic_id;
    /*存储位置*/
    protected String savePath;
    /*文件总长度*/
    protected long countLength;
    /*下载长度*/
    protected long readLength;
    /*下载唯一的HttpService*/
    @Transient
    protected ComicService service;
    /*回调监听*/
    @Transient
    protected HttpDownOnNextListener listener;
    /*超时设置*/
    protected  int connectonTime=6;
    /*state状态数据库保存*/
    protected int stateInte;
    /*url*/
    private String url;
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getStateInte() {
        return this.stateInte;
    }
    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }
    public int getConnectonTime() {
        return this.connectonTime;
    }
    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }
    public long getReadLength() {
        return this.readLength;
    }
    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }
    public long getCountLength() {
        return this.countLength;
    }
    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }
    public String getSavePath() {
        return this.savePath;
    }
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 588015662)
    public DownInfo(long id, long comic_id, String savePath, long countLength,
            long readLength, int connectonTime, int stateInte, String url) {
        this.id = id;
        this.comic_id = comic_id;
        this.savePath = savePath;
        this.countLength = countLength;
        this.readLength = readLength;
        this.connectonTime = connectonTime;
        this.stateInte = stateInte;
        this.url = url;
    }
    @Generated(hash = 928324469)
    public DownInfo() {
    }

    public DownInfo(String url,HttpDownOnNextListener listener) {
        setUrl(url);
        setListener(listener);
    }

    public DownInfo(String url) {
        setUrl(url);
    }

    public HttpDownOnNextListener getListener() {
        return listener;
    }

    public void setListener(HttpDownOnNextListener listener) {
        this.listener = listener;
    }

    public ComicService getService() {
        return service;
    }

    public void setService(ComicService service) {
        this.service = service;
    }

    public DownState getState() {
        switch (getStateInte()){
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
            default:
                return DownState.FINISH;
        }
    }

    public void setState(DownState state) {
        setStateInte(state.getState());
    }
    public long getComic_id() {
        return this.comic_id;
    }
    public void setComic_id(long comic_id) {
        this.comic_id = comic_id;
    }

}
