package com.wrk.capitalnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by MrbigW on 2016/10/20.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 各个详情页面的公共类
 * -------------------=.=------------------------
 */

public abstract class HomeDetailBasePager {

    public Context mContext;

    public View rootView;

    public HomeDetailBasePager(Context context) {
        this.mContext = context;
        rootView = initView();

    }

    /**
     * 强制子类实现该方法
     *
     * @return
     */
    public abstract View initView();


    /**
     * 绑定数据
     */
    public void initData() {

    }

}
