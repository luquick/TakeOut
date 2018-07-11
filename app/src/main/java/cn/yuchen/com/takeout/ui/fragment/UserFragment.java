package cn.yuchen.com.takeout.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.global.MyApplication;
import cn.yuchen.com.takeout.model.dao.DBHelper;
import cn.yuchen.com.takeout.model.dao.bean.UserInfo;
import cn.yuchen.com.takeout.ui.LoginActivity;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：个人模块
 */
public class UserFragment extends BaseFragment {
    @BindView(R.id.tv_user_setting)
    ImageView tvUserSetting;
    @BindView(R.id.iv_user_notice)
    ImageView ivUserNotice;
    @BindView(R.id.login)
    ImageView login;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @BindView(R.id.iv_address)
    ImageView ivAddress;
    Unbinder unbinder;

    private Dao<UserInfo, Integer> mDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DBHelper dbHelper = DBHelper.getInstances(getActivity());
        /*泛型中 Integer 代表 [UserInfo]_bean 所指向主键Id的类型*/
        mDao = dbHelper.getDao(UserInfo.class);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    /**
     * 重新获取焦点
     */
    @Override
    public void onResume() {
        super.onResume();
         /*根据用户Id去查，对应Id的用户，用户Id在登陆时，有返回bean对象。
        通过这个bean对象得到，将其Id保存到全局的MyApplication中，
        在LoginPresenter.class查看*/
        if (MyApplication.USER_ID != -1) {
            try {
                llUserinfo.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                UserInfo userInfo = mDao.queryForId(MyApplication.USER_ID);
                if (userInfo != null) {
                    username.setText(userInfo.getName());
                    phone.setText(userInfo.getPhone());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /*从数据库中获取数据展示*/
        } else {
            llUserinfo.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.login)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
