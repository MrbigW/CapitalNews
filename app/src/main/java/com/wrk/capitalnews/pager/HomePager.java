package com.wrk.capitalnews.pager;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.base.BasePager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 主页面
 * -------------------=.=------------------------
 */

public class HomePager extends BasePager {


    public HomePager(Context context, int type) {
        super(context, type);
    }

    @Override
    public void initData() {
        super.initData();

        tv_leftmenu_title.setText("首页");

        lv_leftmenu_menu.setAdapter(new LeftMenuAdapter());

        ib_leftmenu_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftemenu_drawer.openDrawer(GravityCompat.START);
            }
        });

//        fl_leftmenu_content.addView();

    }


    private int[] images = {R.drawable.home_press,
            R.drawable.newscenter_press,
            R.drawable.smartservice_press,
            R.drawable.govaffair_press,
            R.drawable.setting_press};
    private List<String> items = Arrays.asList("首页", "帐号", "喜爱", "朋友", "设置");

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(mContext, R.layout.item_leftmenu_layout, null);

            TextView leftmenu_name = (TextView) convertView.findViewById(R.id.leftmenu_name);
            ImageView leftmenu_image = (ImageView) convertView.findViewById(R.id.leftmenu_image);

            leftmenu_image.setImageResource(images[position]);
            leftmenu_name.setText(items.get(position));

            return convertView;
        }
    }
}
