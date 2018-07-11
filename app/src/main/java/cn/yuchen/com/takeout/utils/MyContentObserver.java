package cn.yuchen.com.takeout.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：Created by Luquick on 2018/7/10.
 * 作用：xxxxxxx
 */
public class MyContentObserver extends ContentObserver {
    private String mCode;
    private Context mContext;
    private Handler mHandler;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public MyContentObserver(Context context,Handler handler) {

        super(handler);
        mContext = context;
        mHandler = handler;
    }

    /**
     * 回调函数, 当监听的Uri发生改变时，会回调该方法
     * 需要注意的是当收到短信的时候会回调两次
     * 收到短信一般来说都是执行了两次onchange方法.第一次一般都是raw的这个.
     * 虽然收到了短信.但是短信并没有写入到收件箱里
     *
     * @param selfChange 是否收到短信
     * @param uri 信息
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        /*if (uri.toString().equals("content://sms/raw")) {
            return;
        }*/

        Uri inboxUri = Telephony.Sms.Inbox.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(
                inboxUri,
                null,
                null,
                null,
                Telephony.Sms.DEFAULT_SORT_ORDER
        );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.BODY));
                if (address.equals("1008611")) {
                    return;
                }
                /*正则表达式匹配验证码*/
                Pattern pattern = Pattern.compile("(\\d{4})");
                Matcher matcher = pattern.matcher(body);
                mCode = matcher.group(0);
                Message message = Message.obtain();
                message.what =Constant.CHECK_CODE_SUCCESS;
                message.obj = mCode;
                mHandler.sendMessage(message);
            }
            cursor.close();
        }
    }
}
