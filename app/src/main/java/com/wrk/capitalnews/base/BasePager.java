package com.wrk.capitalnews.base;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
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

    public static final int VIEW_TYPE_NOML = 1;
    public static final int VIEW_TYPE_DRAW = 2;

    // 代表每个页面的视图
    public View rootView;

    public Context mContext;
    @BindView(R.id.ib_basepager_menu)
    public ImageButton ibBasepagerMenu;
    @BindView(R.id.tv_basepager_title)
    public TextView tvBasepagerTitle;
    @BindView(R.id.fl_basepager_content)
    public FrameLayout flBasepagerContent;


    public ImageButton ib_leftmenu_menu;
    public TextView tv_leftmenu_title;
    public FrameLayout fl_leftmenu_content;
    public ListView lv_leftmenu_menu;
    public DrawerLayout leftemenu_drawer;

    public BasePager(Context context, int type) {
        this.mContext = context;
        rootView = initView(type);
    }

    /**
     * 初始化公共视图
     *
     * @return
     */
    private View initView(int type) {


        switch (type) {
            case VIEW_TYPE_NOML:
                View nomalView = View.inflate(mContext, R.layout.basepager_layout, null);
                ButterKnife.bind(this, nomalView);
                return nomalView;
            case VIEW_TYPE_DRAW:
                View DrawView = View.inflate(mContext, R.layout.left_drawmenu_layout, null);
                ib_leftmenu_menu = (ImageButton) DrawView.findViewById(R.id.ib_leftmenu_menu);
                tv_leftmenu_title = (TextView) DrawView.findViewById(R.id.tv_leftmenu_title);
                fl_leftmenu_content = (FrameLayout) DrawView.findViewById(R.id.fl_leftmenu_content);
                lv_leftmenu_menu = (ListView) DrawView.findViewById(R.id.lv_leftmenu_menu);
                leftemenu_drawer = (DrawerLayout) DrawView.findViewById(R.id.leftemenu_drawer);
                return DrawView;
        }

        return null;
    }

    /**
     * 由子类重写该方法，子视图和FrameLayout结合成一个页面；绑定数据
     */
    public void initData() {

    }


}
