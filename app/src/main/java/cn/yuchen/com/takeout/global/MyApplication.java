package cn.yuchen.com.takeout.global;

import android.app.Application;

import com.mob.MobSDK;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：xxxxxxx
 */
public class MyApplication extends Application {
    private static final MyApplication instances = new MyApplication();
    public static int STATUS_BAR_HEIGHT;
    public static int USER_ID = -1;

    public static MyApplication getInstances() {
            return instances;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        //获取状态栏高的资源
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            //滚据资源ID获取相应的尺寸值
            STATUS_BAR_HEIGHT = getResources().getDimensionPixelSize(identifier);
        }

        //初始化MobSDK--->短信验证SDK
        MobSDK.init(this);
    }
}
