package cn.yuchen.com.takeout.ui.fragment;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.presenter.HomePresenter;
import cn.yuchen.com.takeout.ui.adapter.HomeRecyclerViewAdapter;
import cn.yuchen.com.takeout.utils.LogUtil;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：xxxxxxx
 */
public class HomeFragment extends BaseFragment {
    private int mY = 0;
    //梯度--->定值
    private static final float DEFINITE_VALUE = 300.0f;
    private ArgbEvaluator mArgbEvaluator;

    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.home_tv_address)
    TextView homeTvAddress;
    @BindView(R.id.ll_title_search)
    LinearLayout llTitleSearch;
    @BindView(R.id.ll_title_container)
    LinearLayout llTitleContainer;
    Unbinder unbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //每次进入这个页面都让其为0：
        mY = 0;
        mArgbEvaluator = new ArgbEvaluator();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //创建adapter----首页数据适配器
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(getActivity());
        //绑定adapter
        rvHome.setAdapter(adapter);
        //创建layoutManager,设置方向，是否反转
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        //设置layoutManager
        rvHome.setLayoutManager(llm);
        //设置滚动监听
        rvHome.addOnScrollListener(new MyOnScrollListener());
        //网络请求-----初始化数据--网络获取
        new HomePresenter(adapter).getHomeData("12.343", "23.442");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 通过内部类方式--提供绑定的滚动监听器
     */
    class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //滚动时调用
            LogUtil.d("onScrolled: ------");
            //dy---> 垂直滚动多少个像素，根据这个值的大小可以设置顶部 title 的变化/透明度/颜色/位置/
            //dy---> 0 ; 小于 0.
            //dy--->大于等于 定值===300自定义。
            //dy--->0-->300 颜色#XXXX-->#YYYY
            int bgColor = 0X553190E8;//初始颜色
            int bgColor_end = 0XFF3190E0;
            mY += dy;
            if (mY <= 0) {
                //没有移动
                bgColor = 0X553190E8;
            } else if (mY >= 300) {
                //颜色最大值
                bgColor = 0XFF3190E0;
            } else {
                //0-->300 颜色渐变
                //一定要用小数，
               bgColor = (int) mArgbEvaluator.evaluate(mY/DEFINITE_VALUE,bgColor, bgColor_end);
            }
            llTitleContainer.setBackgroundColor(bgColor);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //滚动状态放生变化调用
            LogUtil.d("onScrollStateChanged: ------");
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
