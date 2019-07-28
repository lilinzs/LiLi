package com.yueer.farmshop.kaoshitest.db;

import com.yueer.farmshop.kaoshitest.baiduditu.BaseApp;
import com.yueer.farmshop.kaoshitest.dao.DaoMaster;
import com.yueer.farmshop.kaoshitest.dao.DaoSession;
import com.yueer.farmshop.kaoshitest.dao.DbDataBeanDao;

import java.util.List;

public class DbUtils {

    private final DbDataBeanDao mDbDataBeanDao;

        public DbUtils() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(BaseApp.getApp(), "aa.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getReadableDb());
        DaoSession daoSession = daoMaster.newSession();
        mDbDataBeanDao = daoSession.getDbDataBeanDao();

    }

    private static DbUtils dbUtils;

    public static DbUtils getDbUtils() {
        if (dbUtils == null) {
            synchronized (DbUtils.class) {
                if (dbUtils == null) {
                    dbUtils = new DbUtils();

                }
            }
        }
        return dbUtils;
    }

    //查询
    public DbDataBean query(String title) {
        return mDbDataBeanDao.queryBuilder().where(DbDataBeanDao.Properties.Shop_name.eq(title)).unique();
    }
 //查询
    public DbDataBean queryId(int id) {
        return mDbDataBeanDao.queryBuilder().where(DbDataBeanDao.Properties.Id.eq(id)).unique();
    }

    //查询全部
    public List<DbDataBean> queryAll() {
        return mDbDataBeanDao.queryBuilder().list();
    }

    //插入
    public long insert(DbDataBean dbDataBean) {
        if (!has2(dbDataBean)) {
            return mDbDataBeanDao.insertOrReplace(dbDataBean);
        }
        return -1;

    }


    //插入全部
    public void insertAll(List<DbDataBean> dbDataBean) {
        if (!has()) {

            mDbDataBeanDao.insertOrReplaceInTx(dbDataBean);
        }
    }


    //删除
    public boolean delete(DbDataBean dbDataBean) {
        if (has2(dbDataBean)){

            mDbDataBeanDao.delete(dbDataBean);
            return true;
        }
        return false;
    }

    //删除所有
    public void deleteAll() {
        if (has()){

            mDbDataBeanDao.deleteAll();
        }
    }

    //更改
    public void update(DbDataBean dbDataBean) {
        if (has2(dbDataBean)){

            mDbDataBeanDao.update(dbDataBean);
        }
    }

    private boolean has() {
        List<DbDataBean> list = mDbDataBeanDao.queryBuilder().list();
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean has2(DbDataBean dbDataBean) {
        List<DbDataBean> list = mDbDataBeanDao.queryBuilder().where(DbDataBeanDao.Properties.Shop_name.eq(dbDataBean.getShop_name())).list();
        if (list.size() > 0 && list != null) {
            return true;
        }
        return false;
    }

}
