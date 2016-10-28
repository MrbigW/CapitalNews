package com.wrk.capitalnews.pager;

import android.content.Context;
import android.view.View;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.base.BasePager;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 商城页面
 * -------------------=.=------------------------
 */

public class ShoppingMallPager extends BasePager {


    public ShoppingMallPager(Context context, int type) {
        super(context, type);
    }

    @Override
    public void initData() {
        super.initData();


        View view = View.inflate(mContext, R.layout.shopping_pager, null);

        flBasepagerContent.addView(view);

    }
}
