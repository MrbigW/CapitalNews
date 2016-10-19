package com.wrk.capitalnews.fragment;

import android.support.v4.view.PagerAdapter;
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

    @Override
    public View initView() {

        View view = View.inflate(mContext, R.layout.content_fragment_layout, null);
        mBind = ButterKnife.bind(this, view);
        return view;
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

    class ContentFragPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            BasePager pager = mPagers.get(position);

            View rootView = pager.rootView;

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }
}























