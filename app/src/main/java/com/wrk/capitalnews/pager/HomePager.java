package com.wrk.capitalnews.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wrk.capitalnews.ChannelsActivity;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.activity.MainActivity;
import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.base.HomeDetailBasePager;
import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.pager.detailPager.TabDetailPager;
import com.wrk.capitalnews.utils.CacheUtils;
import com.wrk.capitalnews.utils.Constants;
import com.wrk.capitalnews.utils.DownLoaderUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.wrk.capitalnews.R.id.idt_homepager;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 主页面
 * -------------------=.=------------------------
 */

public class HomePager extends BasePager {

    private static final String START_HOME = "start_home";
    // 指示器
    private TabLayout mTabLayout;
    // viewpager
    private ViewPager vp_homepager;
    // more
    private ImageButton ib_leftmenu_more;

    public DownLoaderUtils mDownLoaderUtils;

    private NewsContentBean mNewsContentBean;

    private List<NewsContentBean.DataBean.ChildrenBean> mChildrenBeanList;

    private List<HomeDetailBasePager> mPagers;

    private ArrayList<String> mTitles;
    private View mView;

    public HomePager(Context context, int type) {
        super(context, type);
        mDownLoaderUtils = new DownLoaderUtils();
        mTitles = new ArrayList<>();
        mPagers = new ArrayList<>();
    }

    @Override
    public void initData() {
        super.initData();

        tv_leftmenu_title.setText("首页");

        // 侧滑菜单
        lv_leftmenu_menu.setAdapter(new LeftMenuAdapter());

        lv_leftmenu_menu.setOnItemClickListener(new drawerOnItemClickListener());

        ib_leftmenu_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftemenu_drawer.openDrawer(GravityCompat.START);
            }
        });


        // 填充主要内容
        initFramContent();

    }

    class drawerOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView textView = (TextView) view.findViewById(R.id.leftmenu_name);
            Toast.makeText(mContext, textView.getText(), Toast.LENGTH_SHORT).show();
            leftemenu_drawer.closeDrawer(GravityCompat.START);
        }
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
                    }

                    @Override
                    public void onNext(String s) {

                        View view = initContentView();

                        mNewsContentBean = new Gson().fromJson(s, NewsContentBean.class);

                        mChildrenBeanList = mNewsContentBean.getData().get(0).getChildren();

                        for (int i = 0; i < mChildrenBeanList.size(); i++) {

                            /**
                             * 保存频道信息
                             */
                            // 判断是否进入过主页面
                            boolean isStartHome = CacheUtils.getBoolean(mContext, START_HOME);
                            if (isStartHome) {
                                String channels = CacheUtils.getChannelsString(mContext, mChildrenBeanList.get(i).getUrl());
                                if (!TextUtils.isEmpty(channels)) {
                                    mTitles.add(channels);
                                    if (channels.equals(mChildrenBeanList.get(i).getTitle())) {
                                        mPagers.add(new TabDetailPager(mContext, mChildrenBeanList.get(i)));
                                    }

                                }

                            } else {
                                CacheUtils.putChannelsString(mContext, mChildrenBeanList.get(i).getUrl(), mChildrenBeanList.get(i).getTitle());
                                mTitles.add(mChildrenBeanList.get(i).getTitle());
                                mPagers.add(new TabDetailPager(mContext, mChildrenBeanList.get(i)));
                            }

                        }


                        mTabLayout.setupWithViewPager(vp_homepager);
                        vp_homepager.setAdapter(new HomePagerAdapter());

                        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


                        // 设置ImageButton点击事件
                        ib_leftmenu_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, ChannelsActivity.class);
                                Bundle data = new Bundle();
                                data.putSerializable("channels", (Serializable) mChildrenBeanList);
                                intent.putExtras(data);
                                ((MainActivity) mContext).startActivityForResult(intent, 0);

                            }
                        });

                        fl_leftmenu_content.addView(view);
                        CacheUtils.putBoolean(mContext, START_HOME, true);
                    }
                });
    }


    @NonNull
    private View initContentView() {
        mView = View.inflate(mContext, R.layout.homepager_framlayout, null);
        mTabLayout = (TabLayout) mView.findViewById(idt_homepager);
        vp_homepager = (ViewPager) mView.findViewById(R.id.vp_homepager);
        ib_leftmenu_more = (ImageButton) mView.findViewById(R.id.ib_leftmenu_more);
        return mView;
    }


    class HomePagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            TabDetailPager tabDetailPager = (TabDetailPager) mPagers.get(position);

            View rootView = tabDetailPager.rootView;

            tabDetailPager.initData();

            container.addView(rootView);

            return rootView;
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
