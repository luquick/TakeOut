package cn.yuchen.com.takeout.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.List;

import butterknife.ButterKnife;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.presenter.GoodsPresenter;
import cn.yuchen.com.takeout.presenter.net.bean.GoodsInfo;
import cn.yuchen.com.takeout.presenter.net.bean.GoodsTypeInfo;
import cn.yuchen.com.takeout.presenter.net.bean.Seller;
import cn.yuchen.com.takeout.ui.adapter.GoodsAdapter;
import cn.yuchen.com.takeout.ui.adapter.GoodsTypeAdapter;
import cn.yuchen.com.takeout.utils.Constant;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：xxxxxxx
 */
public class GoodsFragment extends BaseFragment {
    @butterknife.BindView(R.id.rv_goods_type)
    RecyclerView rvGoodsType;
    @butterknife.BindView(R.id.slhlv)
    StickyListHeadersListView slhlv;
    private Seller mSeller;
    private GoodsAdapter mGoodsAdapter;
    private GoodsTypeAdapter mGoodsTypeAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mSeller = (Seller) arguments.getSerializable(Constant.INTENT_SELLER);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //-----------------------左侧----------------------------------
        //创建_绑定Adapter,传递上下文进去
        mGoodsTypeAdapter = new GoodsTypeAdapter(getActivity(), this);
        rvGoodsType.setAdapter(mGoodsTypeAdapter);
        //创建_设置_绑定LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvGoodsType.setLayoutManager(linearLayoutManager);

        //------------------------右侧---------------------------------
        mGoodsAdapter = new GoodsAdapter(getActivity());
        slhlv.setAdapter(mGoodsAdapter);
        slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //todo 可以在结束滚动的时候在进行处理一次，解决由于快速滑动且切换左侧选项卡导致的不对应问题
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //获取到两侧的数据集合
                List<GoodsInfo> goodsList = mGoodsAdapter.getData();
                List<GoodsTypeInfo> goodsTypeList = mGoodsTypeAdapter.getData();
                //集合判空
                if (goodsList != null && goodsTypeList != null && goodsList.size() > 0 && goodsTypeList.size() > 0) {
                    //右侧可视界面第一个itemId对应的typeId就是需要被改变的左侧条目属性的Item对应的商品分类Id
                    /*--->得到可见区域第一个分类商品信息--->获取该商品信息的typeId*/
                    GoodsInfo goodsInfo = goodsList.get(firstVisibleItem);
                    int goodsInfoTypeId = goodsInfo.getTypeId();
                    /*--->得到左侧选中的itemId--->通过itemId找到对应商品分类信息--->通过这个商品分类信息找到商品分类信息的Id*/
                    int leftItemid = mGoodsTypeAdapter.getCurrentIndex();
                    GoodsTypeInfo goodsTypeInfo = goodsTypeList.get(leftItemid);
                    int goodsTypeInfoId = goodsTypeInfo.getId();
                    /*只处理左右商品信息不对应展示的问题*/
                    if (goodsTypeInfoId != goodsInfoTypeId) {
                        for (GoodsTypeInfo gTI : goodsTypeList) {
                            if (gTI.getId() == goodsInfoTypeId) {
                                //更新左侧被选中的Item属性--->刷新列表--->被更改属性的Item滚动到可见区域--->跳出循环
                                int idex = goodsTypeList.indexOf(gTI);
                                /*此方法内部已经提供刷新左侧列表*/
                                mGoodsTypeAdapter.setCurrentIndex(idex);
                                /*更新左侧选中的Items属性存在可视区域 <---滚动左侧列表*/
                                rvGoodsType.smoothScrollToPosition(idex);
                                /*找到对应的id跳出循环*/
                                break;
                            }
                        }
                    }
                }
            }
        });
        //-------------------------------------------------------------
        //获取数据--触发网络请求数据
        GoodsPresenter goodsPresenter = new GoodsPresenter(mGoodsTypeAdapter, mGoodsAdapter, mSeller);
        goodsPresenter.getBusinessData(mSeller.getId());
    }

    /**
     * @param typeId 商品类型id---> typeId 用于内部两个Adapter【GoodsTypeAdapter】，【GoodsAdapter】间传递的 商品类型 typeId
     *               用于管理右侧商品列表的位置锁定
     */
    public void setTypeId(int typeId) {
        List<GoodsInfo> goodsInfos = mGoodsAdapter.getData();
        //首先判不为空
        if (goodsInfos != null && goodsInfos.size() > 0) {
            //左侧选中的商品分类Id --> 右侧在遍历商品过程中有对应左侧的类型typeId,则右侧listView就需要滚动到索引i的位置
            for (GoodsInfo goodsInfo : goodsInfos) {
                int id = goodsInfo.getTypeId();
                if (typeId == id) {
                    //滚动到当前索引位置
                    slhlv.setSelection(goodsInfos.indexOf(goodsInfo));
                    //找到对应的id停止最外层的循环。
                    break;
                }
            }
        }
    }
}
