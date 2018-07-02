package cn.yuchen.com.takeout.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.presenter.net.bean.GoodsInfo;
import cn.yuchen.com.takeout.utils.Constant;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：xxxxxxx
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

    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
