package com.wrk.capitalnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class HorizontalScrollViewPager extends ViewPager {

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    private float startX;
    private float startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 反拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                // 1.记录按下的坐标
                startX = ev.getX();
                startY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float endY = ev.getY();

                // 计算距离
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);

                if (distanceX > distanceY) {

                    if (getCurrentItem() == 0 && endX - startX > 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (getCurrentItem() == getAdapter().getCount() - 1 && endX - startX < 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                } else {
                    // 竖直滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}








