package cn.yuchen.com.takeout.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.presenter.GoodsPresenter;
import cn.yuchen.com.takeout.presenter.net.bean.GoodsInfo;
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
        GoodsTypeAdapter gTAdapter = new GoodsTypeAdapter(getActivity(), this);
        rvGoodsType.setAdapter(gTAdapter);
        //创建_设置_绑定LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvGoodsType.setLayoutManager(linearLayoutManager);

        //------------------------右侧---------------------------------
        mGoodsAdapter = new GoodsAdapter(getActivity());
        slhlv.setAdapter(mGoodsAdapter);
        //-------------------------------------------------------------
        //获取数据--触发网络请求数据
        GoodsPresenter goodsPresenter = new GoodsPresenter(gTAdapter, mGoodsAdapter, mSeller);
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
