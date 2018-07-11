package cn.yuchen.com.takeout.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.presenter.LoginPresenter;
import cn.yuchen.com.takeout.utils.Constant;
import cn.yuchen.com.takeout.utils.LogUtil;
import cn.yuchen.com.takeout.utils.MyContentObserver;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 作用：xxxxxxx
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.iv_user_back)
    ImageView ivUserBack;
    @BindView(R.id.iv_user_password_login)
    TextView ivUserPasswordLogin;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.tv_user_code)
    TextView tvUserCode;
    @BindView(R.id.et_user_code)
    EditText etUserCode;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.et_user_pas)
    EditText etUserPas;

    /**
     * 获取权限的请求码
     */
    private int mRequestCode = 0;
    private EventHandler mEventHandler;
    private String mPNumber;
    /*用于验证码倒计时*/
    private int time = 60;
    private MyHandler mHandler = new MyHandler();
    private MyContentObserver mContentObserver;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //检查权限
        checkPermission();
        //SMS SDK初始化回调 todo 绕过验证码
        //initSMSSDK();
        mContentObserver = new MyContentObserver(this, mHandler);
        this.getContentResolver().registerContentObserver(
                Telephony.Sms.CONTENT_URI,
                true,
                mContentObserver);

        mLoginPresenter = new LoginPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mEventHandler);
        getContentResolver().unregisterContentObserver(mContentObserver);
    }

    /**
     * 获取权限回调
     *
     * @param requestCode  获取权限发起请求的请求吗
     * @param permissions  发起请求的相关权限的权限数组
     * @param grantResults 对应权限数组的结果数组
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mRequestCode && grantResults.length > 0) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请先取得相关权限", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    @OnClick({R.id.tv_user_code, R.id.login})
    public void getVerificationCode(View view) {
        mPNumber = etUserPhone.getText().toString().trim();
        boolean isOk = !TextUtils.isEmpty(mPNumber) && mPNumber.matches(Constant.PHONE_NUMBER_REG);
        switch (view.getId()) {
            case R.id.tv_user_code:
                if (isOk) {
                    SMSSDK.getVerificationCode("86", mPNumber);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (time > 0) {
                                mHandler.sendEmptyMessage(Constant.KEEP_TIME_MIN);
                                try {
                                    Thread.sleep(999);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "非法手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login:
                //用户输入的手机号码+验证码是否匹配
                //验证码+手机号+密码
                checkLogin(mPNumber, isOk);
                break;
        }
    }

    private void checkLogin(String mPNumber, boolean isOk) {
        //手机号码不为空，且正确
        //isOk;

        //密码不为空
        String pass = etUserPas.getText().toString().trim();
        boolean passNoEmpty = !TextUtils.isEmpty(pass);
        //验证码不为空
        String verificationCode = etUserCode.getText().toString().trim();
        boolean verificationNoEmpty = !TextUtils.isEmpty(verificationCode);
        //todo 越过验证码校验, 这里直接登陆
        login();
        /*if (isOk && passNoEmpty && verificationNoEmpty) {
            //输入内容是否合法，验证短信验证码
            SMSSDK.submitVerificationCode("86", mPNumber, verificationCode);
        } else {
            Toast.makeText(this, "非法手机号码", Toast.LENGTH_SHORT).show();
        }*/
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_RECEIVE_CODE:
                    if (msg.obj != null) {
                        Object code = msg.obj;
                        etUserCode.setText(code.toString());
                    }
                case Constant.KEEP_TIME_MIN:
                    time--;
                    //跟新UI
                    if (time > 0) {
                        tvUserCode.setText("请稍后(" + time + ")");
                    } else {
                        tvUserCode.setText("重新获取验证码");
                    }
                    break;
                case Constant.REGAIN_VERIFICATION_CODE:
                    time = 60;
                    tvUserCode.setText("重新获取验证码");
                    break;
               /* case Constant.SEND_CODE_SUCCESS:
                    //请求验证码成功
                    time = 60;
                    tvUserCode.setText("获取验证码成功");
                    break;*/
                case Constant.CHECK_CODE_SUCCESS:
                    //验证码校验成功
                    //用户注册，登陆
                    login();

                    break;
                case Constant.CHECK_CODE_FAIL:
                    //验证码校验失败
                    break;
            }
        }
    }

    /**
     * 登陆
     */
    private void login() {
        //密码不为空
        String pass = etUserPas.getText().toString().trim();
        if (!TextUtils.isEmpty(mPNumber) && mPNumber.matches(Constant.PHONE_NUMBER_REG) && !TextUtils.isEmpty(pass)) {
            mLoginPresenter.getLoginData(mPNumber, mPNumber, pass, 2);
        } else {
            Toast.makeText(this, "登陆信息异常，请检查", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 先初始化权限
     */
    private void checkPermission() {
        //如果 >= 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
            int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            ArrayList<String> permissions = new ArrayList<>();
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                mRequestCode |= 1;
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                mRequestCode |= 1 << 1;
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSms != PackageManager.PERMISSION_GRANTED) {
                mRequestCode |= 1 << 2;
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (readContacts != PackageManager.PERMISSION_GRANTED) {
                mRequestCode |= 1 << 3;
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                mRequestCode |= 1 << 4;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (mRequestCode > 0) {
                String[] permission = new String[permissions.size()];
                this.requestPermissions(permissions.toArray(permission), mRequestCode);
            }
        }
    }

    private void initSMSSDK() {
        mEventHandler = new EventHandler() {//子线程执行
            @Override
            public void afterEvent(int event, int result, Object data) {

                LogUtil.i(data.toString());
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        //验证手机号码是否匹配，做用户注册和登陆
                        mHandler.sendEmptyMessage(Constant.CHECK_CODE_SUCCESS);

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        mHandler.sendEmptyMessage(Constant.SEND_CODE_SUCCESS);

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //重新获取验证码
                        mHandler.sendEmptyMessage(Constant.REGAIN_VERIFICATION_CODE);
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //验证码不匹配
                        mHandler.sendEmptyMessage(Constant.CHECK_CODE_FAIL);
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(mEventHandler);
    }
}
