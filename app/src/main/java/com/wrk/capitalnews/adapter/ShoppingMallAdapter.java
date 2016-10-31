package com.wrk.capitalnews.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.bean.ShoppingCart;
import com.wrk.capitalnews.bean.ShoppingMallBean;
import com.wrk.capitalnews.pay.MyAliPay;
import com.wrk.capitalnews.utils.CartProvider;
import com.wrk.capitalnews.utils.ToastUtil;
import com.wrk.capitalnews.view.BuyPopUpWindow;
import com.wrk.capitalnews.view.NumAddSubView;

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

    private CartProvider mCartProvider;

    private MyAliPay mAliPay;

    private RelativeLayout rl_view;

    private BuyPopUpWindow mBuyPopUpWindow;

    public ShoppingMallAdapter(Context context, List<ShoppingMallBean.Wares> beanList, RelativeLayout rl_view) {
        this.mContext = context;
        this.mWaresList = beanList;
        this.mCartProvider = new CartProvider(mContext);
        this.rl_view = rl_view;
        mAliPay = new MyAliPay(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(mContext, R.layout.item_shoppingmall_layout, null);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(mContext).load(mWaresList.get(position).getImgUrl()).placeholder(R.drawable.pic_item_list_default).into(holder.iv_icon);
        holder.tv_name.setText(mWaresList.get(position).getName());
        holder.tv_price.setText(mWaresList.get(position).getPrice() + "");
        holder.tv_sale.setText("销量：" + mWaresList.get(position).getSale() + "");

        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBuyPopWindow(holder, position);
            }
        });

    }

    private void initBuyPopWindow(ViewHolder holder, final int position) {
        mBuyPopUpWindow = new BuyPopUpWindow(mContext);

        mBuyPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        ImageView iv_icon = (ImageView) mBuyPopUpWindow.getContentView().findViewById(R.id.iv_icon);
        TextView tv_name = (TextView) mBuyPopUpWindow.getContentView().findViewById(R.id.tv_name);
        TextView tv_price = (TextView) mBuyPopUpWindow.getContentView().findViewById(R.id.tv_price);
        TextView tv_sale = (TextView) mBuyPopUpWindow.getContentView().findViewById(R.id.tv_sale);
        final NumAddSubView count = (NumAddSubView) mBuyPopUpWindow.getContentView().findViewById(R.id.nasv_count);
        Button btn_buy_now = (Button) mBuyPopUpWindow.getContentView().findViewById(R.id.btn_buy_now);
        Button btn_buy_cart = (Button) mBuyPopUpWindow.getContentView().findViewById(R.id.btn_buy_cart);

        iv_icon.setImageDrawable(holder.iv_icon.getDrawable());
        tv_name.setText(holder.tv_name.getText());
        tv_price.setText(holder.tv_price.getText());
        tv_sale.setText(holder.tv_sale.getText());
        count.setMaxValue(15);
        count.setMinValue(1);
        count.setValue(1);

        btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliPay.pay(v, mWaresList.get(position).getName(), "", mWaresList.get(position).getPrice() * count.getValue() + "");
                if (TextUtils.equals(mAliPay.getResultStatus(), "9000")) {
                    mBuyPopUpWindow.dismiss();
                }
            }
        });

        btn_buy_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < count.getValue(); i++) {
                    ShoppingMallBean.Wares wares = mWaresList.get(position);
                    ShoppingCart cart = mCartProvider.conversion(wares);
                    mCartProvider.addData(cart);
                }
                mBuyPopUpWindow.dismiss();
                ToastUtil.showToast(mContext, "亲,添加成功啦,去购物车结算吧~~~");
            }
        });

        mBuyPopUpWindow.showAtLocation(rl_view, Gravity.BOTTOM, 0, 0);
        if (mBuyPopUpWindow.isShowing()) {
            lightOff();
        }

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //文艺青年中的文青
            Bundle payload = (Bundle) payloads.get(0);//取出我们在getChangePayload（）方法返回的bundle
//            ShoppingMallBean.Wares bean = mWaresList.get(position);//取出新数据源，（可以不用）
            for (String key : payload.keySet()) {
                switch (key) {
                    case "KEY_NAME":
                        //这里可以用payload里的数据，不过data也是新的 也可以用
                        holder.tv_name.setText(payload.getString(key));
                        break;
                    case "KEY_IMGURL":
                        Glide.with(mContext).load(payload.getString(key)).placeholder(R.drawable.news_pic_default).into(holder.iv_icon);
                        break;
                    case "KEY_PRICE":
                        holder.tv_price.setText(payload.getString(key));
                        break;
                    case "KEY_SALE":
                        holder.tv_sale.setText(payload.getString(key));
                        break;
                    default:
                        break;
                }
            }
        }
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

    // 更新数据
    public void setData(List<ShoppingMallBean.Wares> beanList) {
        this.mWaresList = beanList;
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


    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_price;
        private TextView tv_sale;
        private Button btn_buy;

        public ViewHolder(final View itemView) {
            super(itemView);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_sale = (TextView) itemView.findViewById(R.id.tv_sale);
            btn_buy = (Button) itemView.findViewById(R.id.btn_buy);


        }
    }

    /**
     * 显示时屏幕变暗
     */
    private void lightOff() {

        WindowManager.LayoutParams layoutParams = ((Activity) mContext).getWindow().getAttributes();

        layoutParams.alpha = 0.3f;

        ((Activity) mContext).getWindow().setAttributes(layoutParams);

    }

    /**
     * 显示时屏幕变亮
     */
    private void lightOn() {

        WindowManager.LayoutParams layoutParams = ((Activity) mContext).getWindow().getAttributes();

        layoutParams.alpha = 1.0f;

        ((Activity) mContext).getWindow().setAttributes(layoutParams);

    }

}
