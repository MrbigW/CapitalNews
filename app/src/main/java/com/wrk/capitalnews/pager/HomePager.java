package com.wrk.capitalnews.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wrk.capitalnews.ChannelsActivity;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.utils.Constants;
import com.wrk.capitalnews.utils.DensityUtil;
import com.wrk.capitalnews.utils.DownLoaderUtils;
import com.wrk.capitalnews.utils.LogUtil;
import com.wrk.capitalnews.view.ViewPagerIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 主页面
 * -------------------=.=------------------------
 */

public class HomePager extends BasePager {

    // 指示器
    private ViewPagerIndicator idt_homepager;
    // viewpager
    private ViewPager vp_homepager;
    // more
    private ImageButton ib_leftmenu_more;

    private DownLoaderUtils mDownLoaderUtils;

    private NewsContentBean mNewsContentBean;

    private List<NewsContentBean.DataBean.ChildrenBean> mChildrenBeanList;

    private ArrayList<String> mTitles;

    public HomePager(Context context, int type) {
        super(context, type);
        mDownLoaderUtils = new DownLoaderUtils();
        mTitles = new ArrayList<>();
    }

    @Override
    public void initData() {
        super.initData();

        tv_leftmenu_title.setText("首页");

        // 侧滑菜单
        lv_leftmenu_menu.setAdapter(new LeftMenuAdapter());

        ib_leftmenu_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftemenu_drawer.openDrawer(GravityCompat.START);
            }
        });

        // 填充主要内容
        initFramContent();


    }

    private void initFramContent() {
        // 联网请求数据
        mDownLoaderUtils.getJsonResult(Constants.NEWS_CONTENER_URL)
                .subscribeOn(Schedulers.io()) // 让subscribe的操作执行在异步线程
                .observeOn(AndroidSchedulers.mainThread()) // 让订阅者代码执行在主线程
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {

                        View view = initContentView();

                        mNewsContentBean = new Gson().fromJson(s, NewsContentBean.class);

                        mChildrenBeanList = mNewsContentBean.getData().get(0).getChildren();
                        LogUtil.e(mChildrenBeanList.toString());
                        for (int i = 0; i < mChildrenBeanList.size(); i++) {
                            mTitles.add(mChildrenBeanList.get(i).getTitle());
                            LogUtil.e(mChildrenBeanList.get(i).getTitle());
                        }

                        idt_homepager.setVisibleTabCount(5);
                        idt_homepager.setTabItemTitles(mTitles);
                        vp_homepager.setAdapter(new HomePagerAdapter());
                        idt_homepager.setViewPager(vp_homepager, 0);

                        // 设置ImageButton点击事件
                        ib_leftmenu_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, ChannelsActivity.class);
                                Bundle data = new Bundle();
                                data.putSerializable("channels", (Serializable) mChildrenBeanList);
                                intent.putExtras(data);
                                mContext.startActivity(intent);
                            }
                        });

                        fl_leftmenu_content.addView(view);
                    }
                });


    }

    @NonNull
    private View initContentView() {
        View view = View.inflate(mContext, R.layout.homepager_framlayout, null);
        idt_homepager = (ViewPagerIndicator) view.findViewById(R.id.idt_homepager);
        vp_homepager = (ViewPager) view.findViewById(R.id.vp_homepager);
        ib_leftmenu_more = (ImageButton) view.findViewById(R.id.ib_leftmenu_more);
        return view;
    }

    class HomePagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            TextView tempView = new TextView(mContext);
            tempView.setText(mTitles.get(position));
            tempView.setTextColor(Color.YELLOW);
            tempView.setTextSize(DensityUtil.dip2px(mContext, 15));
            tempView.setGravity(Gravity.CENTER);

            container.addView(tempView);

            return tempView;
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
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
