package com.wrk.capitalnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.utils.DensityUtil;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 新闻页面
 * -------------------=.=------------------------
 */

public class NewsCenterPager extends BasePager {


    public NewsCenterPager(Context context, int type) {
        super(context, type);
    }

    @Override
    public void initData() {
        super.initData();

        TextView textView = new TextView(mContext);
        textView.setText("News Pager");
        textView.setTextSize(DensityUtil.dip2px(mContext, 20));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);

        tvBasepagerTitle.setText("新闻");
        ibBasepagerMenu.setVisibility(View.VISIBLE);

        flBasepagerContent.addView(textView);

    }
}
