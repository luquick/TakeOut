package cn.yuchen.com.takeout.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import cn.yuchen.com.takeout.model.dao.DBHelper;
import cn.yuchen.com.takeout.model.dao.bean.UserInfo;
import cn.yuchen.com.takeout.presenter.net.bean.ResponseInfo;
import cn.yuchen.com.takeout.ui.LoginActivity;
import cn.yuchen.com.takeout.utils.LogUtil;
import retrofit2.Call;

/**
 * 作者：Created by Luquick on 2018/7/9.
 * 作用：登陆业务网络请求
 */
public class LoginPresenter extends BasePresenter {
    private Context mContext;
    private AndroidDatabaseConnection mADC;
    private Savepoint mStartPoint;

    public LoginPresenter(Context context) {
        mContext = context;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    protected void parseJson(String json) {
        LogUtil.i(json);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
        //1、获取bean中字段
        //2、创建数据库takeout.db,创建用户表
        //3、向表中写数据
        //以上可以通过关系型数据库管理框架来完成 ormLite--->注解的形势替换原生的 sql 语句。
        //1和2被合并执行
        /*因为会有切换账号的操作*/
        /*所以，先将表中所有用户登陆的状态置为未登录状态，再将登陆请求成功的用户登陆状态置为登陆状态*/
        if (userInfo != null) {
            //1、获取DBHelper对象、获取 UserInfo指向t_user表的操作对象 Dao
            DBHelper dbHelper = DBHelper.getInstances(mContext);
            Dao<UserInfo, Integer> dao = dbHelper.getDao(UserInfo.class);//这行代码指定了两个泛型，第一个泛型UserInfo用来指定UserInfo对应的t_user表的Dao，用来操作 t_user这张表;第二个泛型 Integer用来保持对应的UserInfo类中自增Id字段_id类型。


            //------------------一定要使用事务，中途发生错误可以回滚----------------------
            try {
                /*让 ormLite中的sql语句绑定事务*/
                mADC = new AndroidDatabaseConnection(dbHelper.getWritableDatabase(), true);
                /*指定事务的回滚点，try中的代码*/
                mStartPoint = mADC.setSavePoint("start");
                /*告知ormLite无需自动管理事务*/
                mADC.setAutoCommit(false);

                List<UserInfo> userInfos = dao.queryForAll();//select * from t_user;面向对象掉方法，省去了SQL语句的编写。但是原生的 Sql语句效率高。此框架使用反射等等,效率降低，web框架 SSH-S(Struts)-S(Spring)-H(Hibernate)
                /*所有用户置为未登录 --- 0 ，查询，修改*/
                for (UserInfo uif : userInfos) {
                    if (uif.getLogin() == 1) {
                        uif.setLogin(0);
                        //更新数据库
                        dao.update(uif);
                    }
                }
                //===========此处发生错误就会中断数据库操作，后面的数据库操作代码不会执行===========
                /*将请求登陆成功者置为登陆状态*/
                //此用户在数据库中已经存在，只修改登陆状态
                UserInfo userBean = dao.queryForId(userInfo.get_id());//select * from t_user where _id = ?
                if (userBean != null) {
                    userBean.setLogin(1);
                    dao.update(userBean);//更新
                } else {
                    //此用户不存在，插入此用户的数据
                    userInfo.setLogin(1);
                    dao.create(userInfo);//等同 insert into.
                }
                /*没有异常提交事务*/
                mADC.commit(mStartPoint);
                /*提交事务成功开始界面业务处理，关闭当前页*/
                ((LoginActivity)mContext).finish();
            } catch (SQLException e) {
                e.printStackTrace();
                /*如果 Try块出现问题，就让事务回滚到安全点*/
                try {
                    mADC.rollback(mStartPoint);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            //-----------------------------事务结束--------------------------
        }
    }

    //请求网络触发
    public void getLoginData(String username, String userPass, String phone, int type) {
        Call<ResponseInfo> loginInfo = responseInfoAPI.getloginInfo(username, userPass, phone, type);
        loginInfo.enqueue(new CallBackAdapter());
    }
}
