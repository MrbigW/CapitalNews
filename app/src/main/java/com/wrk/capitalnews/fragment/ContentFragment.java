package com.wrk.capitalnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.base.BaseFragment;
import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.pager.HomePager;
import com.wrk.capitalnews.pager.NewsCenterPager;
import com.wrk.capitalnews.pager.SettingPager;
import com.wrk.capitalnews.pager.ShoppingCartPager;
import com.wrk.capitalnews.pager.ShoppingMallPager;
import com.wrk.capitalnews.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class ContentFragment extends BaseFragment {


    @BindView(R.id.vp_main_content)
    NoScrollViewPager vpMainContent;
    @BindView(R.id.rg_main)
    public RadioGroup rgMain;
    private Unbinder mBind;

    // 各个页面的实例
    private ArrayList<BasePager> mPagers;
    private View mView;

    @Override
    public View initView() {

        mView = View.inflate(mContext, R.layout.content_fragment_layout, null);
        mBind = ButterKnife.bind(this, mView);
        return mView;
    }


    @Override
    public void initData() {
        super.initData();

        rgMain.check(R.id.rb_home);

        mPagers = new ArrayList<>();
        mPagers.add(new HomePager(mContext, BasePager.VIEW_TYPE_DRAW));
        mPagers.add(new NewsCenterPager(mContext, BasePager.VIEW_TYPE_NOML));
        mPagers.add(new ShoppingMallPager(mContext, BasePager.VIEW_TYPE_NOML));
        mPagers.add(new ShoppingCartPager(mContext, BasePager.VIEW_TYPE_NOML));
        mPagers.add(new SettingPager(mContext, BasePager.VIEW_TYPE_NOML));

        vpMainContent.setAdapter(new ContentFragPagerAdapter());

        rgMain.setOnCheckedChangeListener(new ContentFragOnCheckedChangeListener());

        // 取消数据预加载
        vpMainContent.addOnPageChangeListener(new MyOnpageChangeListenter());
//        mPagers.get(0).initData();
    }

    class MyOnpageChangeListenter implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 3) {
                mPagers.get(position).initData();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class ContentFragOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    vpMainContent.setCurrentItem(0, false);
                    break;
                case R.id.rb_news:
                    vpMainContent.setCurrentItem(1, false);
                    break;
                case R.id.rb_shopping_mall:
                    vpMainContent.setCurrentItem(2, false);
                    break;
                case R.id.rb_shopping_cart:
                    vpMainContent.setCurrentItem(3, false);
                    break;
                case R.id.rb_setting:
                    vpMainContent.setCurrentItem(4, false);
                    break;
            }
        }
    }


    View HomePagerRootView = null;

    class ContentFragPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            BasePager pager = mPagers.get(position);

            View rootView = pager.rootView;

            if (position == 0) {
                HomePagerRootView = mPagers.get(0).rootView;
            }

            pager.initData();

            container.addView(rootView);

            return rootView;
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

    public View getHomePagerRootView() {
        return HomePagerRootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }
}























