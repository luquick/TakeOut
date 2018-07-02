package cn.yuchen.com.takeout.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import cn.yuchen.com.takeout.presenter.net.bean.Seller;
import cn.yuchen.com.takeout.ui.fragment.BaseFragment;
import cn.yuchen.com.takeout.ui.fragment.GoodsFragment;
import cn.yuchen.com.takeout.ui.fragment.SellerFragment;
import cn.yuchen.com.takeout.ui.fragment.SuggestFragment;
import cn.yuchen.com.takeout.utils.Constant;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：xxxxxxx
 */
public class BusinessFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mStringArry;
    private ArrayList<Fragment> mFragments;
    private Seller mSeller;

    public BusinessFragmentPagerAdapter(FragmentManager fm, String[] strings, Seller seller) {
        super(fm);
        this.mStringArry = strings;
        mFragments = new ArrayList<>();
        this.mSeller = seller;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case 0:
                fragment = new GoodsFragment();
                break;
            case 1:
                fragment = new SuggestFragment();
                break;
            case 2:
                fragment = new SellerFragment();
                break;
        }

        //构建bundle对象
        //传递序列化数据
        //fragment设置传递的bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.INTENT_SELLER,mSeller);
        fragment.setArguments(bundle);

        if (!mFragments.contains(fragment)) {
            mFragments.add(fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mStringArry.length;
    }

    /**
     * viewPager顶部页面选项卡
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mStringArry[position];
    }
}
