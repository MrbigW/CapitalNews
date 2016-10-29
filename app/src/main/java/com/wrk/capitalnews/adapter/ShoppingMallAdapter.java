package com.wrk.capitalnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.bean.ShoppingMallBean;

import java.util.List;

/**
 * Created by MrbigW on 2016/10/28.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class ShoppingMallAdapter extends RecyclerView.Adapter<ShoppingMallAdapter.ViewHolder> {

    private Context mContext;

    private List<ShoppingMallBean.Wares> mWaresList;

    public ShoppingMallAdapter(Context context, List<ShoppingMallBean.Wares> beanList) {
        this.mContext = context;
        this.mWaresList = beanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(mContext, R.layout.item_shoppingmall_layout, null);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mWaresList.get(position).getImgUrl()).placeholder(R.drawable.news_pic_default).into(holder.iv_icon);
        holder.tv_name.setText(mWaresList.get(position).getName());
        holder.tv_price.setText(mWaresList.get(position).getPrice() + "");
        holder.tv_sale.setText("销量：" + mWaresList.get(position).getSale() + "");
    }

    @Override
    public int getItemCount() {
        return mWaresList.size();
    }

    /**
     * 清除数据
     */
    public void clearData() {
        mWaresList.clear();
        notifyItemRangeRemoved(0, mWaresList.size());
    }

    /**
     * 添加新的数据
     *
     * @param i
     * @param beanList
     */
    public void addData(int i, List<ShoppingMallBean.Wares> beanList) {
        mWaresList.addAll(i, beanList);
        notifyItemRangeChanged(i, mWaresList.size());
    }

    public int getCount() {
        return mWaresList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_price;
        private TextView tv_sale;
        private Button btn_buy_now;
        private Button btn_buy_cart;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_sale = (TextView) itemView.findViewById(R.id.tv_sale);
            btn_buy_now = (Button) itemView.findViewById(R.id.btn_buy_now);
            btn_buy_cart = (Button) itemView.findViewById(R.id.btn_buy_cart);

        }
    }

}
