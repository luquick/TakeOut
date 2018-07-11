package cn.yuchen.com.takeout.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.global.MyApplication;
import cn.yuchen.com.takeout.presenter.net.bean.GoodsInfo;
import cn.yuchen.com.takeout.ui.BusinessActivity;
import cn.yuchen.com.takeout.utils.Constant;
import cn.yuchen.com.takeout.utils.LogUtil;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 作用：展示右侧商品列表--适配器
 */
public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private Context mContext;
    private ArrayList<GoodsInfo> mGoods;

    public GoodsAdapter(Context context) {
        this.mContext = context;
    }


    /**
     * 得到适配器数据
     *
     * @param goods -->所有的商品
     */
    public void setData(ArrayList<GoodsInfo> goods) {
        mGoods = goods;
        //只要有数据进来 --->一定要刷新页面
        notifyDataSetChanged();
    }

    /**
     * @return 当前的所有商品
     */
    public List<GoodsInfo> getData() {
        return mGoods;
    }

    /**
     * @return 商品总数
     */
    @Override
    public int getCount() {
        if (mGoods != null && mGoods.size() > 0) {
            return mGoods.size();
        }
        return 0;
    }

    /**
     * @param position 当前item位置
     * @return 当前Item索引位置返回当前对象。
     */
    @Override
    public Object getItem(int position) {

        return mGoods.get(position);
    }

    /**
     * @param position item索引
     * @return 当前Item索引
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogUtil.i("--------getView-------");

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GoodsInfo goodsInfo = mGoods.get(position);
        //viewHolder中的控件赋值
        //商品名称
        viewHolder.tvName.setText(goodsInfo.getName());
        //现在的价格，历史价格
        viewHolder.tvNewprice.setText(goodsInfo.getNewPrice() + "");
        viewHolder.tvOldprice.setText(goodsInfo.getOldPrice() + "");
        //使用 picasso 做图片异步加载
        //---传递环境--->指定加载图片URL地址--->指定图片放置位置
        Picasso.with(mContext)
                .load(Constant.LOCALHOST + goodsInfo.getIcon().substring(15))
                .into(viewHolder.ivIcon);
        //商品数量大于零，展示商品数量。
        if (goodsInfo.getCount() > 0) {
            /*显示减号*/
            viewHolder.ibMinus.setVisibility(View.VISIBLE);
            /*显示用来展示的商品数量控件*/
            viewHolder.tvCount.setVisibility(View.VISIBLE);
            /*显示数量—[更新数量]*/
            viewHolder.tvCount.setText(String.valueOf(goodsInfo.getCount()));
        } else {
            viewHolder.ibMinus.setVisibility(View.GONE);
            viewHolder.tvCount.setVisibility(View.GONE);
        }

        /*当前点中的Item位置，传递给当前view'Holder对象*/
        viewHolder.setPosition(position);
        return convertView;
    }

    //-----------------------------------------implement-------------------------
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_type_header, null);
        textView.setText(mGoods.get(position).getTypeName());
        //返回头的item的View对象。
        return textView;
    }

    @Override
    public long getHeaderId(int position) {
        //返回分类头的item总个数---即：分类个数。
        //项目中分类有11个
        return mGoods.get(position).getTypeId();
    }

    //-----------------------------------------implement-------------------------
    class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_zucheng)
        TextView tvZucheng;
        @BindView(R.id.tv_yueshaoshounum)
        TextView tvYueshaoshounum;
        @BindView(R.id.tv_newprice)
        TextView tvNewprice;
        @BindView(R.id.tv_oldprice)
        TextView tvOldprice;
        @BindView(R.id.ib_minus)
        ImageButton ibMinus;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.ib_add)
        ImageButton ibAdd;

        /*当前被点击Item的位置*/
        private int mPosition;
        private GoodsInfo mGoodsInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.ib_add, R.id.ib_minus})
        public void OnClick(View view) {
            //视图在动画过程中不能多次点击，需要等到上一个动画完成。这里通过控制视图的点击来满足条件
            //动画完成后在可以点击
            view.setEnabled(false);
            switch (view.getId()) {
                case R.id.ib_add://增加商品
                    addGoods(view);
                    break;
                case R.id.ib_minus://减少商品
                    deleteGoods(view);
                    break;
            }
        }

        /**
         * 向购物车添加商品
         * 主要走处理动画效果，
         * 【平抛动画中的 view小球是在整个界面的上层一个视图层出现的小圆球，通过将整个视图层放置在 帧布局中。】
         *
         * @param view 点击的当前视图---添加按钮-->位置信息
         */
        private void addGoods(View view) {
            //添加商品数量为空的时候出现滚动平移动画,通过当前商品的被添加数量来判断
            mGoodsInfo = GoodsAdapter.this.mGoods.get(mPosition);
            if (mGoodsInfo != null && mGoodsInfo.getCount() == 0) {
                //构建三组动画-->动画集合
                AnimationSet aSet = generateAnimation(true,null,null);
                ibMinus.startAnimation(aSet);
                ibMinus.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
            }

            //获取当前view的X，Y坐标。
            int[] sourceLocation = new int[2];
            view.getLocationInWindow(sourceLocation);
            //LogUtil.d("addGoods: X------ " + sourceLocation[0] + "\nY------ " + sourceLocation[1]);

            //在点击加号的位置添加一个飞行动画的图片
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.mipmap.button_add);
            //设置初始位置
            imageView.setX(sourceLocation[0]);
            imageView.setY(sourceLocation[1] - MyApplication.STATUS_BAR_HEIGHT);

            //获取最外图层添加这个view,这里的mContext就是GoodsFragment传递进来的最外层视图
            //将view也就是点击的那张图片的宽高信息设置进去--当前ImageView的位置，宽高，信息与当前点击的view信息一致
            ((BusinessActivity) mContext).addImageView(imageView, view.getWidth(), view.getHeight());

            //获取目标位置---数组
            int[] destinationLocation = ((BusinessActivity) mContext).getShopCartLocation();
            //imageView向目标位置平抛运动
            startMove(imageView, sourceLocation, destinationLocation, view);

            /*给选中的商品数量 + 1 */
            int newCount = mGoodsInfo.getCount() + 1;
            mGoodsInfo.setCount(newCount);
            /*刷新数据适配器-刷新页面*/
            /*这里的刷新getView方法将得到调用，修改界面属性信息*/
            notifyDataSetChanged();
        }

        /**
         * 删除商品的操作
         *
         * @param view 删除按钮视图
         */
        private void deleteGoods(View view) {
            view.setEnabled(true);
            int tempCount = mGoodsInfo.getCount() - 1;
            mGoodsInfo.setCount(tempCount);
            tvCount.setText(String.valueOf(tempCount));
            if (tempCount == 0) {
                AnimationSet aSet = generateAnimation(false, tvCount, ibMinus);
                ibMinus.startAnimation(aSet);
            }
        }

        /**
         * 设置当前位置
         *
         * @param position 点中item的当前位置
         */
        private void setPosition(int position) {
            mPosition = position;
        }
    }

    /**
     * 开始移动-->抛物
     *
     * @param imageView           索要移动的对象
     * @param sourceLocation      移动开始位置
     * @param destinationLocation 终止移动位置
     */
    private void startMove(final ImageView imageView, int[] sourceLocation, int[] destinationLocation, final View view) {
        //分别获取开始--终止 的X、Y轴坐标
        int startX = sourceLocation[0];
        int startY = sourceLocation[1];
        int endX = destinationLocation[0];
        int endY = destinationLocation[1];
        //x轴、Y轴--长度
        //构建X轴平移动画---轴不动---匀速运动
        TranslateAnimation translateAnimationX = new TranslateAnimation(
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, endX - startX,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());//X轴匀速
        //构建Y轴平移动画---X轴不懂---且 加速运动
        TranslateAnimation translateAnimationY = new TranslateAnimation(
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, endY - startY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());//Y轴加速运动

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(translateAnimationX);
        animationSet.addAnimation(translateAnimationY);
        animationSet.setDuration(500);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //本次动画完成后恢复可点击事件
                view.setEnabled(true);
                //移除平抛运动的视图
                ((BusinessActivity) mContext).removeImageView(imageView);//内部类中使用局部变量要加 final
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(animationSet);
    }

    /**
     * @return 动画集合
     */
    private AnimationSet generateAnimation(boolean isToVisible, final TextView tv, final ImageButton ib) {

        /*旋转、透明、平移*/
        RotateAnimation rotateAnimation = new RotateAnimation(
                0,
                720,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation aA;
        TranslateAnimation tA;
        if (isToVisible) {
            aA = new AlphaAnimation(0, 1);//不可见-->可见
            tA = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    2,
                    Animation.RELATIVE_TO_SELF,
                    0,
                    Animation.RELATIVE_TO_SELF,
                    0,
                    Animation.RELATIVE_TO_SELF,
                    0);
        }else {
            aA = new AlphaAnimation(1, 0);
            tA = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    0,
                    Animation.RELATIVE_TO_SELF,
                    2,
                    Animation.RELATIVE_TO_SELF,
                    0,
                    Animation.RELATIVE_TO_SELF,
                    0);
        }

        /*动画集合--->放置三组动画--->设置持续时间【这里传递false表示不共享同一个插值器】*/
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(aA);
        animationSet.addAnimation(tA);
        if (!isToVisible) {
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tv.setVisibility(View.GONE);
                    ib.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        animationSet.setDuration(500);
        return animationSet;
    }
}
