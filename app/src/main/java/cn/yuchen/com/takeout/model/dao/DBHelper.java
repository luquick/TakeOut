package cn.yuchen.com.takeout.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.yuchen.com.takeout.model.dao.bean.UserInfo;

/**
 * 作者：Created by Luquick on 2018/7/10.
 * 作用：用来操作数据库 ，创建，表的增删该查。
 * 为了数据库操作安全，使用单例，多线程安全，使用 Synchronized.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static DBHelper instances = null;
    /*存储 dao 对象*/
    private HashMap<String, Dao> mDaos = new HashMap<>();

    //线程安全
    public static DBHelper getInstances(Context context) {
        if (instances == null) {
            synchronized (DBHelper.class) {
                if (instances == null) {
                    return new DBHelper(
                            context,
                            "takeout.db",
                            null,
                            1);
                }
                return instances;
            }
        }
        return instances;
    }

    /**
     * @param context         上下文
     * @param databaseName    数据库名称
     * @param factory         用于创建游标Cursor对象
     * @param databaseVersion 数据库版本号
     */
    private DBHelper(Context context,
                     String databaseName,
                     SQLiteDatabase.CursorFactory factory,
                     int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    /**
     * 创建表
     *
     * @param database         数据库
     * @param connectionSource 创建和使用数据库所需的连接
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        //原生创建表做法，编写Sql语句
        //使用ormLite之后的做法
        //传递进来的 java bean 已经由注解替代掉了原有的 sql 语句。
        try {
            TableUtils.createTable(connectionSource, UserInfo.class/*Java bean字节码文件*/);
            /*创建地址表*/
            //TableUtils.createTable(connectionSource, 地址.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新表
     *
     * @param database         数据库
     * @param connectionSource 创建和使用数据库所需的连
     * @param oldVersion       旧的版本
     * @param newVersion       新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource,
                          int oldVersion,
                          int newVersion) {

    }


    /**
     * 获取用于操作对应表 的 Dao, 用来操作表
     * <p>
     * 获取 UserInfo (t_user)表的 Dao 对象，用来增删改查表数据。
     * Dao对象会有多个，对应每一个张表。
     * 这里传进来表所对应的 Java 字节码文件来确定不同的表
     *
     * @param clazz 表对应的字节码
     * @return 字节码对应表对应的 Dao
     */
    public Dao getDao(Class clazz) {
        String className = clazz.getSimpleName();
        Dao dao = mDaos.get(className);

        if (dao == null) {
            //获取clazz对应的Dao对象
            //每次创建对象比较耗费资源，所以创建之后直接保存起来,,,创建一个hashMap()来存储dao对象
            try {
                dao = super.getDao(clazz);
                mDaos.put(className, dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    /**
     * 关闭数据库
     * 销毁所有的 Dao
     */
    @Override
    public void close() {
        for (Dao dao : mDaos.values()) {
            dao = null;
        }
        super.close();
    }
}
