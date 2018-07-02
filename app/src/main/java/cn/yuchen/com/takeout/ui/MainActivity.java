package cn.yuchen.com.takeout.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.ui.fragment.HomeFragment;
import cn.yuchen.com.takeout.ui.fragment.MoreFragment;
import cn.yuchen.com.takeout.ui.fragment.OrderFragment;
import cn.yuchen.com.takeout.ui.fragment.UserFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.main_bottom_switcher_container)
    LinearLayout mainBottomSwitcherContainer;
    @BindView(R.id.main_fragment_container)
    FrameLayout mainFragmentContainer;

    private ArrayList<Fragment> mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initClick();

        //默认第一个选中
        View childView = mainBottomSwitcherContainer.getChildAt(0);
        onClick(childView);

    }

    /**
     * view绑定点击事件
     */
    private void initClick() {
        int childCount = mainBottomSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            FrameLayout fl = (FrameLayout) mainBottomSwitcherContainer.getChildAt(i);
            fl.setOnClickListener(this);
        }
    }


    /**
     * 初始化 fragment
     */
    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new OrderFragment());
        mFragments.add(new UserFragment());
        mFragments.add(new MoreFragment());
    }


    /**
     * 替换fragment
     *
     * @param index
     */
    private void changeFragment(int index) {
        //获取FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //替换fragment
        ft.replace(R.id.main_fragment_container, mFragments.get(index));
        //提交事务
        ft.commit();
    }

    /**
     * todo 需要优化
     *
     * @param index 指定此索引位置的控件，以及子节点上的控件都不可用（蓝色）
     */
    private void changeUI(int index) {
        int childCount = mainBottomSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mainBottomSwitcherContainer.getChildAt(i);
            if (i == index) {
                setStateEnable(view, false);
            } else {
                setStateEnable(view, true);
            }
        }
    }


    /**
     * todo 需要优化
     *
     * @param view 将View设置成 b(true可用，false不可用);
     *             将 View中的子类设置称为 b(true可以，false不可用）
     * @param b
     */
    private void setStateEnable(View view, boolean b) {
        //将View设置为
        view.setEnabled(b);
        //处理子view的节点状态，只有ViewGroup才有子view结点
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = ((ViewGroup) view).getChildAt(i);
                setStateEnable(childView, b);
            }
        }
    }

    /**
     * 点击事件回掉
     *
     * @param v 选中的视图
     */
    @Override
    public void onClick(View v) {
        int index = mainBottomSwitcherContainer.indexOfChild(v);
        changeUI(index);
        changeFragment(index);
    }
}
