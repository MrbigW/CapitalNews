package com.wrk.capitalnews.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.adapter.ShoppingCartAdapter;
import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.bean.ShoppingCart;
import com.wrk.capitalnews.pay.MyAliPay;
import com.wrk.capitalnews.utils.CartProvider;

import java.util.List;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 购物车页面
 * -------------------=.=------------------------
 */

public class ShoppingCartPager extends BasePager {

    private MyAliPay mAlipay;

    private CartProvider mCartProvider;
    private RecyclerView shoppingcart_recyclerView;
    private CheckBox checkbox_all;
    private TextView tv_total_price;
    private Button btn_order;
    private Button btn_delete;
    private TextView tv_nodata;

    private ShoppingCartAdapter mAdpater;

    // 编辑状态
    private static final String ACTION_EDIT = "1";
    // 完成状态
    private static final String ACTION_COMPLETE = "2";


    public ShoppingCartPager(Context context, int type) {
        super(context, type);
        mCartProvider = new CartProvider(mContext);
        mAlipay = new MyAliPay(mContext);
    }

    @Override
    public void initData() {
        super.initData();

        btn_cart.setVisibility(View.VISIBLE);

        tvBasepagerTitle.setText("购物车");
        View view = View.inflate(mContext, R.layout.shopping_cart_pager, null);

        shoppingcart_recyclerView = (RecyclerView) view.findViewById(R.id.shoppingcart_recyclerView);
        checkbox_all = (CheckBox) view.findViewById(R.id.checkbox_all);
        tv_total_price = (TextView) view.findViewById(R.id.tv_total_price);
        btn_order = (Button) view.findViewById(R.id.btn_order);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        flBasepagerContent.removeAllViews();

        flBasepagerContent.addView(view);


        btn_cart.setTag(ACTION_EDIT);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = (String) btn_cart.getTag();
                if (state.equals(ACTION_EDIT)) {
                    showDeleteButton();
                } else if (state.equals(ACTION_COMPLETE)) {
                    hideDeleteButton();
                }
            }

        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdpater.deleteData();
                mAdpater.checkAll_none();
                mAdpater.ShowTotalPrice();

                if (mAdpater != null && mAdpater.getCount() > 0) {
                    tv_nodata.setVisibility(View.GONE);
                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }
            }
        });

        // 去结算-支付
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlipay.pay(v, "首都新闻-购物车结算", "", mAdpater.getTotalPrice()+"");
            }
        });

        showData();
    }

    private void showDeleteButton() {
        btn_cart.setTag(ACTION_COMPLETE);
        btn_cart.setText("完成");
        btn_delete.setVisibility(View.VISIBLE);
        btn_order.setVisibility(View.GONE);
        mAdpater.checkAll_none(false);
        mAdpater.checkAll_none();
        mAdpater.ShowTotalPrice();
    }

    /**
     * 显示数据
     */
    private void showData() {
        // 购物车所有的数据
        List<ShoppingCart> shoppingCarts = mCartProvider.getAllData();
        tv_nodata.setVisibility(View.GONE);
        if (shoppingCarts != null && shoppingCarts.size() > 0) {
            mAdpater = new ShoppingCartAdapter(mContext, shoppingCarts, checkbox_all, tv_total_price);
            // 设置适配器
            shoppingcart_recyclerView.setAdapter(mAdpater);

            shoppingcart_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        } else {
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }


    private void hideDeleteButton() {
        btn_cart.setTag(ACTION_EDIT);
        btn_cart.setText("编辑");
        btn_delete.setVisibility(View.GONE);
        btn_order.setVisibility(View.VISIBLE);
        mAdpater.checkAll_none(true);
        mAdpater.checkAll_none();
        mAdpater.ShowTotalPrice();
    }


}
