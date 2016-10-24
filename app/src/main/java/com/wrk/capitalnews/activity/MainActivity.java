package com.wrk.capitalnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.base.HomeDetailBasePager;
import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.fragment.ContentFragment;
import com.wrk.capitalnews.pager.detailPager.TabDetailPager;

import java.util.ArrayList;
import java.util.List;

import static com.wrk.capitalnews.R.id.idt_homepager;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";

    private TabLayout mTabLayout;
    private ViewPager vp_homepager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化Fragment
        initFragment();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentFragment content = (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_CONTENT_TAG);
        View homePagerRootView = content.getHomePagerRootView();
        vp_homepager = (ViewPager) homePagerRootView.findViewById(R.id.vp_homepager);
        mTabLayout = (TabLayout) homePagerRootView.findViewById(idt_homepager);

        if (requestCode == 0 && resultCode == 1) {

            String titleFromChannels = data.getStringExtra("channel");
            int countFromChannnels = data.getIntExtra("count", -1);
            if (!TextUtils.isEmpty(titleFromChannels) && countFromChannnels > 0) {
                for (int i = 0; i < countFromChannnels; i++) {

                    if (mTabLayout.getTabAt(i).getText().equals(titleFromChannels)) {
                        vp_homepager.setCurrentItem(i);
                    }

                }
            }
        }

        if (requestCode == 0 && resultCode == 2) {
            vp_homepager.removeAllViews();
            mQuickChannels = new ArrayList<>();
            mPagers = new ArrayList<>();
            mTitles = new ArrayList<>();

            mQuickChannels = (ArrayList<NewsContentBean.DataBean.ChildrenBean>) data.getSerializableExtra("quickchannels");

            Log.e("111", mQuickChannels.size() + mQuickChannels.toString());

            for (int i = 0; i < mQuickChannels.size(); i++) {
                mTitles.add(mQuickChannels.get(i).getTitle());
            }

            for (int i = 0; i < mQuickChannels.size(); i++) {
                mPagers.add(new TabDetailPager(this, mQuickChannels.get(i)));
            }

            mTabLayout.setupWithViewPager(vp_homepager);
            vp_homepager.setAdapter(new HomePagerAdapter());
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    private List<HomeDetailBasePager> mPagers;
    private ArrayList<NewsContentBean.DataBean.ChildrenBean> mQuickChannels;
    private ArrayList<String> mTitles;

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

    private void initFragment() {
        // 1. 得到Fragment
        FragmentManager mFragManager = getSupportFragmentManager();
        // 2. 开启事务
        FragmentTransaction transaction = mFragManager.beginTransaction();
        // 3. 替换
        transaction.replace(R.id.main_content, new ContentFragment(), MAIN_CONTENT_TAG); // 主页

        // 4.提交
        transaction.commit();

    }

}






















