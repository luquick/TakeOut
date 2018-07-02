package cn.yuchen.com.takeout.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.presenter.net.bean.Seller;
import cn.yuchen.com.takeout.ui.adapter.BusinessFragmentPagerAdapter;
import cn.yuchen.com.takeout.utils.Constant;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：xxxxxxx
 */
public class BusinessActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_menu)
    ImageButton ibMenu;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.bottomSheetLayout)
    BottomSheetLayout bottomSheetLayout;
    @BindView(R.id.imgCart)
    ImageView imgCart;
    @BindView(R.id.tvSelectNum)
    TextView tvSelectNum;
    @BindView(R.id.tvCountPrice)
    TextView tvCountPrice;
    @BindView(R.id.tvDeliveryFee)
    TextView tvDeliveryFee;
    @BindView(R.id.tvSendPrice)
    TextView tvSendPrice;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.fl_Container)
    FrameLayout flContainer;

    private String[] strings = new String[]{"商品","评价","商家"};
    private Seller mSeller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness);
        ButterKnife.bind(this);

        //得到传递过来的序列化对象
        mSeller = (Seller) getIntent().getSerializableExtra(Constant.INTENT_SELLER);
        //顶部的 TabLayout + ViewPager 联动效果
        initTab();
        //填充ViewPager
        initViewPager();
        //选项卡和ViewPager绑定
        tabs.setupWithViewPager(vp);
        
    }

    /**
     * 初始化选项卡
     */
    private void initTab() {
        for (int i = 0; i < strings.length; i++) {
            tabs.addTab(tabs.newTab().setText(strings[i]));
        }
    }

    /**
     *  初始化 viewPager
     */
    private void initViewPager() {
        //pagerAdapter--->viewPager中直接指定添加的view对象
        //FragmentPagerAdapter --->viewPager中添加的是 fragment, onCreateView 方法中返回的 view 对象。
        BusinessFragmentPagerAdapter adapter = new BusinessFragmentPagerAdapter(getSupportFragmentManager(),strings,mSeller);
        vp.setAdapter(adapter);
    }



    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
