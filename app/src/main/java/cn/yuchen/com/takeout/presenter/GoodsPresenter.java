package cn.yuchen.com.takeout.presenter;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yuchen.com.takeout.presenter.net.bean.BusinessInfo;
import cn.yuchen.com.takeout.presenter.net.bean.GoodsInfo;
import cn.yuchen.com.takeout.presenter.net.bean.GoodsTypeInfo;
import cn.yuchen.com.takeout.presenter.net.bean.ResponseInfo;
import cn.yuchen.com.takeout.presenter.net.bean.Seller;
import cn.yuchen.com.takeout.ui.adapter.GoodsAdapter;
import cn.yuchen.com.takeout.ui.adapter.GoodsTypeAdapter;
import retrofit2.Call;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：获取商品信息--商品类型列表数据，--商品详细信息
 */
public class GoodsPresenter extends BasePresenter {
    private static final String TAG = GoodsPresenter.class.getSimpleName();
    private GoodsAdapter mGoodsAdapter;
    private Seller mSeller;
    private GoodsTypeAdapter mGoodsTypeAdapter;
    private ArrayList<GoodsInfo> goodsInfos;

    /**
     * 主要用于传递过来 商品类型列表的适配器
     *
     * @param gTAdapter --商品类型列表适配器
     */
    public GoodsPresenter(GoodsTypeAdapter gTAdapter,GoodsAdapter gAdapter, Seller seller) {
        this.mGoodsTypeAdapter = gTAdapter;
        this.mGoodsAdapter = gAdapter;
        this.mSeller = seller;
    }


    @Override
    protected void showError(String message) {

    }

    @Override
    protected void parseJson(String json) {
        Log.i(TAG, "parseJson: " + json);
        Gson gson = new Gson();
        //BusinessInfo 包含左侧商品分类、右侧相同分类商品的列表
        BusinessInfo businessInfo = gson.fromJson(json, BusinessInfo.class);
        //商品分类--核心字段 分类Id、用于计算此分类商品选中的数量count
        //获取商品分类的集合
        List<GoodsTypeInfo> goodsTypes = businessInfo.getList();
        //商品分类数据集合需要填充商品分类适配器
        mGoodsTypeAdapter.setData(goodsTypes);

        //右侧商品信息展示列表
        //商品列表--核心字段所属分类TypeId,此TypeId和商品分类Id对应、用于记录当前商品购买的数量count
        //sellerId用于标记此商品属于那个商家
        //typeName用于标记此商品属于那个商品分类。
        //生成用于右侧商品展示的集合
        initGoodsList(goodsTypes);
        //将商品集合在数据适配器中展示
        mGoodsAdapter.setData(goodsInfos);

    }

    /**
     * 生成用于右侧商品展示的集合
     *
     * @param goodsTypes --商品分类集合---->所有的商品
     */
    private void initGoodsList(List<GoodsTypeInfo> goodsTypes) {
        goodsInfos = new ArrayList<>();

        //获取每个商品分类对象
        for (int i = 0; i < goodsTypes.size(); i++) {
            GoodsTypeInfo goodsTypeInfo = goodsTypes.get(i);
            //获取每个分类对象中的对应商品列表,并遍历每一个商品
            for (GoodsInfo gI : goodsTypeInfo.getList()) {
                //设置每个商品对应的所属分类名称
                gI.setTypeName(goodsTypeInfo.getName());
                //设置每个商品对应的所属分类Id
                gI.setTypeId(goodsTypeInfo.getId());
                //设置每个商品对应的所属商家
                gI.setSellerId((int) mSeller.getId());
                goodsInfos.add(gI);
            }
        }
    }


    /**
     * 触发网络请求获取商品数据--商品列表--商品详细信息。
     *
     * @param sellerId look over{@link cn.yuchen.com.takeout.presenter.net.bean.Seller}
     */
    public void getBusinessData(long sellerId) {
        Call<ResponseInfo> businessInfo = responseInfoAPI.getGoogsInfo(sellerId);
        businessInfo.enqueue(new CallBackAdapter());
    }
}
