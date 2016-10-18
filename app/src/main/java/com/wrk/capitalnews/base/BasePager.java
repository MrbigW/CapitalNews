package com.wrk.capitalnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wrk.capitalnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 各个页面的公共类
 * -------------------=.=------------------------
 */

public class BasePager {


    // 代表每个页面的视图
    public View rootView;

    public Context mContext;
    @BindView(R.id.ib_basepager_menu)
    public ImageButton ibBasepagerMenu;
    @BindView(R.id.tv_basepager_title)
    public TextView tvBasepagerTitle;
    @BindView(R.id.fl_basepager_content)
    public FrameLayout flBasepagerContent;

    public BasePager(Context context) {
        this.mContext = context;
        rootView = initView();
    }

    /**
     * 初始化公共视图
     *
     * @return
     */
    private View initView() {

        View view = View.inflate(mContext, R.layout.basepager_layout, null);
        ButterKnife.bind(this, view);

        return view;
    }

    /**
     * 由子类重写该方法，子视图和FrameLayout结合成一个页面；绑定数据
     */
    public void initData() {

    }

}
