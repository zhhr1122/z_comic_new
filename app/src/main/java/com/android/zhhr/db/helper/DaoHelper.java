package com.android.zhhr.db.helper;

import android.content.Context;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.DownState;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.data.entity.db.DBSearchResult;
import com.android.zhhr.data.entity.db.DownInfo;
import com.android.zhhr.db.manager.DaoManager;
import com.android.zhr.greendao.gen.ComicDao;
import com.android.zhr.greendao.gen.DBDownloadItemsDao;
import com.android.zhr.greendao.gen.DBSearchResultDao;
import com.android.zhr.greendao.gen.DaoMaster;
import com.android.zhr.greendao.gen.DaoSession;
import com.android.zhr.greendao.gen.DownInfoDao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by 皓然 on 2017/8/20.
 */

public class DaoHelper<T> {
    private DaoManager manager;
    private Class<T> clazz;
    public DaoHelper(Context context) {
        manager = DaoManager.getInstance(context);
    }
    private Class<T> getClazz() {
        if (clazz == null) {//获取泛型的Class对象
            clazz = ((Class<T>) (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
        }
        return clazz;
    }
    // 插入增加
    public boolean insert(T t) {
        return manager.getDaoSession().insert(t) != -1;
    }
    //插入集合
    public boolean insertList(final List<T> datas) {
        boolean flag = false;
        try {
            manager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T t : datas) {
                        manager.getDaoSession().insertOrReplace(t);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    //删除
    public boolean delete(T t) {
        try {
            manager.getDaoSession().delete(t);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // 删除所有
    public boolean deleteAll() {
        try {
            manager.getDaoSession().deleteAll(clazz);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAllSearch() {
        try {
            manager.getDaoSession().getDBSearchResultDao().deleteAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //列出所有
    public List<T> listAll() {
        return manager.getDaoSession().loadAll(getClazz());
    }

    public List<Comic> listComicAll() {
        return  manager.getDaoSession().getComicDao().loadAll();
    }
    public T find(long id) {
        return manager.getDaoSession().load(clazz, id);
    }

    public T findComic(long id) {
        return (T) manager.getDaoSession().getComicDao().load(id);
    }

    public List<T> findSearchByTitle(String title) {
        return (List<T>) manager.getDaoSession().getDBSearchResultDao().queryBuilder().where(DBSearchResultDao.Properties.Title.eq(title));
    }

    //更新
    public boolean update(T t) {
        try {
            manager.getDaoSession().update(t);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Comic> queryCollect(){
        List<Comic> list= manager.getDaoSession().getComicDao().queryBuilder()
                .where(ComicDao.Properties.IsCollected.eq(true))
                .limit(10000)
                .orderAsc(ComicDao.Properties.CrateTime)
                .list();
        return list;
    }

    public List<DBSearchResult> querySearch(){
        List<DBSearchResult> list= manager.getDaoSession().getDBSearchResultDao().queryBuilder()
                .orderDesc(DBSearchResultDao.Properties.Search_time)
                .list();
        return list;
    }

    public List<DownInfo> queryDownInfo(long comic_id){
        List<DownInfo> list= manager.getDaoSession().getDownInfoDao().queryBuilder()
                .where(DownInfoDao.Properties.Comic_id.eq(comic_id))
                .list();
        return list;
    }

    public DBDownloadItems findDBDownloadItems(long id){
        DBDownloadItems items =  manager.getDaoSession().getDBDownloadItemsDao().load(id);
        return items;
    }

    public List<DBDownloadItems> queryDownloaditmes(long comic_id){
        List<DBDownloadItems> list= manager.getDaoSession().getDBDownloadItemsDao().queryBuilder()
                .where(DBDownloadItemsDao.Properties.Comic_id.eq(comic_id))
                .list();
        return list;
    }

    //queryRaw查询
    public List<T> queryAll(String where, String... selectionArgs) {
        List<T> list = manager.getDaoSession().queryRaw(clazz, where, selectionArgs);
        return list;
    }
    //biuld查询
    public List<T> queryBuilder() {
        List<T> list = manager.getDaoSession().queryBuilder(clazz).list();
        return list;
    }
    //查询全部，dao查询
    public List<T> queryDaoAll(Class clazz) {
        DaoMaster daoMaster = manager.getDaoMaster();
        DaoSession session = daoMaster.newSession();
        List<T> list = session.loadAll(clazz);
        return list;
    }


}

