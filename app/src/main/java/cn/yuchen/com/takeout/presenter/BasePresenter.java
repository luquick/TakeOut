package cn.yuchen.com.takeout.presenter;

import java.util.HashMap;

import cn.yuchen.com.takeout.presenter.net.iterface.IResponseInfo;
import cn.yuchen.com.takeout.presenter.net.bean.ResponseInfo;
import cn.yuchen.com.takeout.utils.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：业务基类，网络请求相关的基类
 * 参考接口文档
 */
public abstract class BasePresenter {

    public IResponseInfo responseInfoAPI;
    private HashMap<String, String> errorMap;


    public BasePresenter() {
        //服务器返回非成功类型结果的处理
        errorMap = new HashMap<>();
        errorMap.put("1", "服务器数据没有更新");
        errorMap.put("2", "服务器异常");
        errorMap.put("3", "服务器挂了");
        //初始化Retrofit
        //指定访问主机
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //指定具体的网络请求
        responseInfoAPI = retrofit.create(IResponseInfo.class);
    }

    //请求结果的适配器
    class CallBackAdapter implements Callback<ResponseInfo> {

        @Override
        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
            //成功，获取服务器返回的Json串
            ResponseInfo body = response.body();
            if (body.getCode().equals("0")) {
                //成功
                //得到 data
                String json = body.getData();
                //解析 data
                /*因为不同的地方得到的请求结果的 json 数据不同，所以提供一个抽象的 json 文件解析器。
                在各个子类中具体解析实现.所以提供一个抽象的方法*/
                parseJson(json);

            } else {
                //服务器返回的未成功代码的处理。
                String errorMessage = errorMap.get(body.getCode());
                //反馈给用户异常信息，通过调用回掉方法 onFailure();
                onFailure(call,new RuntimeException(errorMessage));
            }
        }

        @Override
        public void onFailure(Call<ResponseInfo> call, Throwable t) {
            //失败
            String message = t.getMessage();
            //用户去处理如何显示错误信息
            showError(message);
        }
    }

    protected abstract void showError(String message);

    protected abstract void parseJson(String json);
}
