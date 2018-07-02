package cn.yuchen.com.takeout.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        GoodsTypeAdapter gTAdapter = new GoodsTypeAdapter(getActivity());
        rvGoodsType.setAdapter(gTAdapter);
        //创建_设置_绑定LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvGoodsType.setLayoutManager(linearLayoutManager);

        //-------------------------------------------------------------
        //------------------------右侧---------------------------------
        GoodsAdapter gAdapter = new GoodsAdapter(getActivity());
        slhlv.setAdapter(gAdapter);


        //-------------------------------------------------------------

        //获取数据--触发网络请求数据
        GoodsPresenter goodsPresenter = new GoodsPresenter(gTAdapter,gAdapter,mSeller);
        goodsPresenter.getBusinessData(mSeller.getId());
    }
}
