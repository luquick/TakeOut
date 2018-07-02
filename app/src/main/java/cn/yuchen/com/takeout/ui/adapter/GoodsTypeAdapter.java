package cn.yuchen.com.takeout.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yuchen.com.takeout.R;
import cn.yuchen.com.takeout.presenter.net.bean.GoodsTypeInfo;
import cn.yuchen.com.takeout.ui.fragment.GoodsFragment;

/**
 * 作者：Created by Luquick on 2018/6/30.
 * 邮箱: xxxxxx@163.com
 * QQ号：930982728
 * 微信：p11225630
 * 作用：商品分类列表适配器
 */
public class GoodsTypeAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<GoodsTypeInfo> mGoodsTypes;
    private GoodsFragment mGoodsFragment;
    //定义一个当前item选择状态的索引
    private int mCurrentIndex = 0;


    /**
     * @param context 构造器传递上下文
     */
    public GoodsTypeAdapter(Context context, GoodsFragment goodsFragment) {
        this.mContext = context;
        this.mGoodsFragment = goodsFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_type, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).type.setText(mGoodsTypes.get(position).getName());
        //默认首个Item选中状态
        if (position == mCurrentIndex) {
            //背景颜色变白，文字变红
            ((ViewHolder) holder).type.setTextColor(Color.RED);
            //拿到当前TextView的所在视图，也就是通过layout.layout的布局填充后的view对象，
            // 这个view对象传递给了holder，所以通过holder.itemView()就可以得到。
            ((ViewHolder) holder).itemView.setBackgroundColor(Color.WHITE);
        } else {
            ((ViewHolder) holder).type.setTextColor(Color.BLACK);
            ((ViewHolder) holder).itemView.setBackgroundColor(Color.LTGRAY);
        }

        //给每个 holder 设置一个索引 index- position
        ((ViewHolder) holder).setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (mGoodsTypes != null && mGoodsTypes.size() > 0) {
            return mGoodsTypes.size();
        }
        return 0;
    }

    /**
     * 数据来源，方便外部注入
     *
     * @param goodsTypes 商品分类列表--数据
     */
    public void setData(List<GoodsTypeInfo> goodsTypes) {
        mGoodsTypes = goodsTypes;
        notifyDataSetChanged();//更新数据
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCount)
        TextView tvCount;
        @BindView(R.id.type)
        TextView type;
        private int position;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //给选中的当前item对应的 view 注册点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更新当前所引值
                    mCurrentIndex = position;
                    //刷新适配器
                    notifyDataSetChanged();
                    //点中某一个分类item--->右侧展示相同类目的商品
                    //分类Id
                    //商品 typeId---对应的就是分类Id
                    /**
                     * 获取左侧item的分类Id
                     * 将这个Id从当前GoodsTypeAdapter传递到GoodsFragment在传递到GoodsAdapter
                     * 用来更新--相同类目产品的滑动位置信息。
                     */
                    int typeId = mGoodsTypes.get(position).getId();
                    //传递给GoodsFragment
                    mGoodsFragment.setTypeId(typeId);

                }
            });

        }

        void setPosition(int position) {

            this.position = position;
        }
    }
}
