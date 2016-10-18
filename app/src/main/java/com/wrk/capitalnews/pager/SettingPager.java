package com.wrk.capitalnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.utils.DensityUtil;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 设置页面
 * -------------------=.=------------------------
 */

public class SettingPager extends BasePager {

    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        TextView textView = new TextView(mContext);
        textView.setText("Setting Pager");
        textView.setTextSize(DensityUtil.dip2px(mContext,20));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);

        tvBasepagerTitle.setText("设置");

        flBasepagerContent.addView(textView);

    }
}
