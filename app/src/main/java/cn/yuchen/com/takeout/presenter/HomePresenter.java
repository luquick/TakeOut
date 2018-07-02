package cn.yuchen.com.takeout.presenter;

import android.util.Log;

import com.google.gson.Gson;

import cn.yuchen.com.takeout.presenter.net.bean.HomeInfo;
import cn.yuchen.com.takeout.presenter.net.bean.ResponseInfo;
import cn.yuchen.com.takeout.ui.adapter.HomeRecyclerViewAdapter;
import retrofit2.Call;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：主页面数据获取的业务职责类--
 */
public class HomePresenter extends BasePresenter {

   private HomeRecyclerViewAdapter mAdapter;

    public HomePresenter(HomeRecyclerViewAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    protected void parseJson(String json) {
        Log.i("",json);
        Gson gson = new Gson();
        //得到Json数据映射到 Java Bean 实例
        HomeInfo hInfo = gson.fromJson(json, HomeInfo.class);

        mAdapter.getData(hInfo);
        //获取首页数据之首页轮番图数据
        hInfo.getHead();
        //获取首页数据之首页列表数据
        hInfo.getBody();




    }


    /**
     * 触发网络请求
     */
    public void getHomeData(String longitude,String latitude) {
        Call<ResponseInfo> homeInfo = responseInfoAPI.getHomeInfo(longitude, latitude);
        //触发网络请求--异步--，传递回掉实例
        homeInfo.enqueue(new CallBackAdapter());
    }
}
