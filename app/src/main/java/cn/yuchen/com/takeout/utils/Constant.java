package cn.yuchen.com.takeout.utils;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：xxxxxxx
 */
public class Constant {

    //网络请求
    public static final String BASE_URL = "http://192.168.1.104:8080/TakeoutServiceVersion2/";
    public static final String HOME_URL = "home";
    public static final String BUSINESS_URL = "business";

    /*登陆*/
    public static final String Login = "login";

    //界面跳转 Intent <--- Key
    //界面数据传递----> key
    public static final String INTENT_SELLER = "seller";//传递 Seller对象

    //拼接访问本机
    public static final String LOCALHOST = "http://192.168.1.104";


    //手机号码正则表达式
    public static String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    /*标记消息---用于Handler处理*/
    public static final int KEEP_TIME_MIN = 1;
    /*重新获取验证码*/
    public static final int REGAIN_VERIFICATION_CODE = 3;
    /*请求验证码成功*/
    public static final int SEND_CODE_SUCCESS = 5;
    /*验证码校验成功*/
    public static final int CHECK_CODE_SUCCESS = 7;
    /*验证码校验失败*/
    public static final int CHECK_CODE_FAIL = 9;


    public static final int MSG_RECEIVE_CODE = 11;

}
