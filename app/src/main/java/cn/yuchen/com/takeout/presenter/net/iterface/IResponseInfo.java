package cn.yuchen.com.takeout.presenter.net.iterface;

import cn.yuchen.com.takeout.presenter.net.bean.ResponseInfo;
import cn.yuchen.com.takeout.presenter.net.bean.Seller;
import cn.yuchen.com.takeout.utils.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：用于维护网络请求的API:（发送网络请求的完整地址，请求方式，请求参数，请求响应结果）
 */
public interface IResponseInfo {


    /**
     * ----构建请求体----
     * 构建主页面数据获取网络请求体
     * <p>
     * 指定请求方式 @GET
     * 请求完整的链接地址
     * http://192.168.1.104:8080/TakeoutServiceVersion2/home?
     * longitude=调用此方法传递过来的值longitude&latitude=调用此方法传递过来的值latitude
     * //请求的参数
     * -----------------@Query 指定发给服务器的参数。
     * -------请求回掉 Call<数据格式实例></>
     *
     *
     * @param longitude longitude
     * @param latitude latitude
     * @return ResponseInfo
     */
    @GET(Constant.HOME_URL)
    Call<ResponseInfo> getHomeInfo(@Query("longitude") String longitude, @Query("latitude") String latitude);

    /**
     * 构建商品类型列表界面数据请求体
     *
     * @param sellerId sellerId
     * @return ResponseInfo
     */
    @GET(Constant.BUSINESS_URL)
    Call<ResponseInfo> getGoogsInfo(@Query("sellerId") long sellerId);

    /**
     * 构建登陆数据请求体
     * @param username userName
     * @param password userPass
     * @param phone phone
     * @param type 验证类型，邮箱，手机验证码。。。。
     * @return ResponseInfo
     */
    @GET(Constant.Login)
    Call<ResponseInfo> getloginInfo(@Query("username") String username,
                                    @Query("password") String password,
                                    @Query("phone") String phone,
                                    @Query("type") int type);
}
