package cn.yuchen.com.takeout.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.presenter.net.bean.HomeInfo;
import cn.yuchen.com.takeout.presenter.net.bean.HomeItem;
import cn.yuchen.com.takeout.presenter.net.bean.Promotion;
import cn.yuchen.com.takeout.presenter.net.bean.Seller;
import cn.yuchen.com.takeout.ui.BusinessActivity;
import cn.yuchen.com.takeout.utils.Constant;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：xxxxxxx
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int ITEM_HEAD = 0;
    private static final int ITEM_SELLER = 1;
    private static final int ITEM_DIV = 2;


    private Context mContext;
    private HomeInfo mData;

    public HomeRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 得到数据
     *
     * @param hInfo
     */
    public void getData(HomeInfo hInfo) {
        this.mData = hInfo;
        //通知数据适配器刷新
        notifyDataSetChanged();
    }

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //viewHolder---管理view中所有控件。
        View view;
        if (viewType == ITEM_HEAD) {
            //头item >>>>>>> parent.getContext();-----> mContext;
            view = LayoutInflater.from(mContext).inflate(R.layout.item_title, null);
            //titleViewHolder ---> 所指向 item 布局转换成 View 对象。
            TitleViewHolder titleViewHolder = new TitleViewHolder(view);
            return titleViewHolder;
        } else if (viewType == ITEM_SELLER) {
            //商家item
            view = LayoutInflater.from(mContext).inflate(R.layout.item_seller, null);
            SellerHolder sellerHolder = new SellerHolder(view);
            return sellerHolder;
        } else {
            //分割线
            view = LayoutInflater.from(mContext).inflate(R.layout.item_division, null);
            DivHolder divHolder = new DivHolder(view);
            return divHolder;
        }
    }

    /**
     * ViewHolder绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //给 holder 中所有的控件绑定数据。
        if (position == 0) {
            //头item
        } else if (mData != null && mData.getBody().get(position - 1).getType() == 0) {
            //商家item
            HomeItem hItem = mData.getBody().get(position - 1);
            setSellerHolderData((SellerHolder) holder, hItem);

            ((SellerHolder) holder).setPostion(position - 1);

        } else {
            //分割线item
            List<String> recommendInfos = mData.getBody().get(position - 1).recommendInfos;
            setDivHolderData((DivHolder) holder, recommendInfos);
        }
    }

    /**
     * 商家Item赋值
     *
     * @param holder   viewHolder持有视图控件各部分的数据绑定
     * @param homeItem mData-->body-->HomeItem-->Seller商家相关信息
     */
    private void setSellerHolderData(SellerHolder holder, HomeItem homeItem) {
        //设置商家名称
        holder.tvTitle.setText(homeItem.seller.getName());
    }

    /**
     * 分割线Item赋值
     *
     * @param holder
     * @param recommendInfos mData-->body-->HomeItem-->RecommendInfos-->string
     */
    private void setDivHolderData(DivHolder holder, List<String> recommendInfos) {
        holder.tv1.setText(recommendInfos.get(0));
        holder.tv2.setText(recommendInfos.get(1));
        holder.tv3.setText(recommendInfos.get(2));
        holder.tv4.setText(recommendInfos.get(3));
        holder.tv5.setText(recommendInfos.get(4));
        holder.tv6.setText(recommendInfos.get(5));

    }

    /**
     * 获取数据条目数量
     * <p>
     * 列表中有 3 种Item类型：头item、基本item、分割线item；
     * itemList = 基本item + 分割线item;
     * item总数 = itemList + 1(头条目) ;
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (mData != null && mData.getHead() != null && mData.getBody() != null && mData.getBody().size() > 0) {
            //body中包括了分割线item.
            List<HomeItem> body = mData.getBody();
            //Head head = mData.getHead();
            return body.size() + 1;
        }
        return 0;
        //区分每一个 index 索引位 item 类型。
    }


    /**
     * 通过索引获取服务器返回的type值，判断条目类型的状态码
     *
     * @param position 0--->头 item ; 1
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //返回头部item类型视图
            return ITEM_HEAD;
        } else if (mData != null && mData.getBody().get(position - 1).getType() == 0) {
            //返回基本类型item视图
            return ITEM_SELLER;
        } else {
            //返回分割线item视图
            return ITEM_DIV;
        }
    }

    /**
     * 头 item 视图的 viewHolder 管理器，管理 头视图中所有控件。
     */
    class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.slider)
        SliderLayout slider; //指向实现顶部轮播图自定义控件

        public TitleViewHolder(View itemView) {
            super(itemView);
            //参数说明 ：this指代当前类TitleViewHolder、 itemView视图。
            ButterKnife.bind(this, itemView);
            //
            ArrayList<Promotion> promotionList = mData.getHead().getPromotionList();
            for (int i = 0; i < promotionList.size(); i++) {
                TextSliderView textSliderView = new TextSliderView(mContext);
                //todo 替换域名--手机端访问
                String url = Constant.LOCALHOST + promotionList.get(i).getPic().substring(15);/*http://10.0.2.2*/
                // initialize a SliderLayout
                textSliderView
                        .description(promotionList.get(i).getInfo())
                        .image(url)//todo 需要将 url 修改程本地域名访问，否则无法得到图片
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                //.setOnSliderClickListener(this);

                //add your extra information
                //textSliderView.bundle(new Bundle());
                //textSliderView.getBundle().putString("extra", name);

                //mDemoSlider相当于 ViewPager。放置 textSliderView-->比ImageView功能丰富
                slider.addSlider(textSliderView);
            }

            //传递默认指定的动画类型
            slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            //指定指示器位置
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            //指定自定义动画
            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setDuration(4000);
        }
    }

    /**
     * 商家 item 视图的 viewHolder 管理器，管理 商家item视图中所有控件。
     * ----------------------------------------------
     * ------创建多个对象，每个对象有指定的position-----
     * ------创建多个对象，每个对象有被指定监听回掉-----
     * ----------------------------------------------
     */
    class SellerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCount)
        TextView tvCount;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        private int postion;


        public SellerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BusinessActivity.class);
                    //传递对象---需要传递的对象所在的类需要实现序列化接口。
                    Seller seller = mData.getBody().get(postion).getSeller();
                    intent.putExtra(Constant.INTENT_SELLER, seller);
                    mContext.startActivity(intent);
                }
            });
        }

        /**
         * 设置当前 holder 的位置。
         *
         * @param position 已经去掉 头 Item 的位置，并且过滤掉了 分割线 Item position/
         */
        public void setPostion(int position) {
            this.postion = position;
        }
    }

    /**
     * 分割线 item 视图的 viewHolder 管理器，管理 分割线item视图中所有控件。
     */
    class DivHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_division_title)
        TextView tvDivisionTitle;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;
        @BindView(R.id.tv4)
        TextView tv4;
        @BindView(R.id.tv5)
        TextView tv5;
        @BindView(R.id.tv6)
        TextView tv6;

        public DivHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
