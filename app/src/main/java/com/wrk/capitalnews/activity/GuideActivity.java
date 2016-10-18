package com.wrk.capitalnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.WelcomeActivity;
import com.wrk.capitalnews.utils.CacheUtils;
import com.wrk.capitalnews.utils.DensityUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends Activity {


    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    @BindView(R.id.ll_guide_pointgroup)
    LinearLayout llGuidePointgroup;
    @BindView(R.id.btn_guide_start)
    Button btnGuideStart;
    @BindView(R.id.iv_guide_redpoint)
    ImageView ivGuideRedpoint;

    private ArrayList<ImageView> mImageViews;

    // 两点的间距
    private int mLeftMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        ButterKnife.bind(this);

        int[] ids = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };

        mImageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imag = new ImageView(this);
            // 设置背景
            imag.setBackgroundResource(ids[i]);

            mImageViews.add(imag);

            // 创建底部指示点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(this, 8);
            }
            point.setLayoutParams(params);

            llGuidePointgroup.addView(point);

        }

        // 设置viewpager的适配器
        vpGuide.setAdapter(new GuidePagerAdapter());

        // 根据View的生命周期，当视图执行到OnLayout或者onDraw的时候，视图的高和宽，边距都有了
        ivGuideRedpoint.getViewTreeObserver().addOnGlobalLayoutListener(new GuideOnGlobalLayoutListener());

        // 设置viewpager的滑动监听，得到屏幕滑动的百分比
        vpGuide.addOnPageChangeListener(new GuideOnPageChangeListner());
    }

    class GuideOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            // 执行不止一次
            if (ivGuideRedpoint.getViewTreeObserver().isAlive()) {
                ivGuideRedpoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mLeftMax = llGuidePointgroup.getChildAt(1).getLeft() - llGuidePointgroup.getChildAt(0).getLeft();
            }
        }
    }

    class GuideOnPageChangeListner implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 两点间移动的距离 = 屏幕滑动百分比 * 间距
            int leftMargin = (int) (positionOffset * mLeftMax);

            // 两点间滑动距离对应的坐标 = 原来的起始位置  + 两点间移动的距离
            leftMargin = leftMargin + position * mLeftMax;

            // params.leftMargin = 两点间滑动距离对应的坐标
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivGuideRedpoint.getLayoutParams();
            params.leftMargin = leftMargin;
            ivGuideRedpoint.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
            if (position == mImageViews.size() - 1) {
                // 最后一个页面
                btnGuideStart.setVisibility(View.VISIBLE);
            } else {
                btnGuideStart.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class GuidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = mImageViews.get(position);
            container.addView(imageView);

            return imageView;
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

    @OnClick(R.id.btn_guide_start)
    public void startMain() {
        // 1.保存进入页面
        CacheUtils.putBoolean(GuideActivity.this, WelcomeActivity.START_MAIN, true);

        // 2，跳转到主页面
        startActivity(new Intent(GuideActivity.this, MainActivity.class));

        // 3.关闭当前页面
        finish();
    }

}








































