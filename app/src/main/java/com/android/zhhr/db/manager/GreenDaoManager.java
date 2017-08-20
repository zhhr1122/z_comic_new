package com.android.zhhr.db.manager;

import android.content.Context;

import com.android.zhr.greendao.gen.DaoMaster;
import com.android.zhr.greendao.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by 皓然 on 2017/8/20.
 */

public class GreenDaoManager {
    private static GreenDaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private DaoMaster.DevOpenHelper devOpenHelper;


    private GreenDaoManager(Context context) {
        devOpenHelper = new DaoMaster.DevOpenHelper(context, "comic.db", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static GreenDaoManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GreenDaoManager(context.getApplicationContext());
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    //输出日志
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }
    //关闭
    public void close() {
        closeHelper();
        closeSession();
    }

    public void closeHelper() {
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }
    //关闭session
    public void closeSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }
}
