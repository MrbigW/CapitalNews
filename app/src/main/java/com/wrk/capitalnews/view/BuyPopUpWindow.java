package com.wrk.capitalnews.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.utils.DensityUtil;

/**
 * Created by MrbigW on 2016/10/24.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 自定义PopUpWindow
 * -------------------=.=------------------------
 */

public class BuyPopUpWindow extends PopupWindow {

    private Context mContext;

    private LayoutInflater mInflater;

    private View mContentView;

    public BuyPopUpWindow(Context context) {
        super(context);

        this.mContext = context;

        // 渲染布局
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.shopping_popup_layout, null);

        // 设置View
        setContentView(mContentView);

        // 设置宽高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(DensityUtil.dip2px(mContext, 375));

        // 设置进出动画
        setAnimationStyle(R.style.BuyPopAnim);

        /**
         * 设置这个背景才能点击外边和back消失
         */
        setBackgroundDrawable(new ColorDrawable());

        // 设置获取焦点
        setFocusable(true);

        // 设置点击外边可以消失
        setOutsideTouchable(true);

        // 设置可触摸
        setTouchable(true);

        // 设置点击外部消失
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // 判断是否点击了外部
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }

                return false;
            }
        });


    }

}






























