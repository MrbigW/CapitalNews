package com.wrk.capitalnews.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.utils.DensityUtil;

/**
 * Created by MrbigW on 2016/10/24.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 自定义PopUpWindow
 * -------------------=.=------------------------
 */

public class NewsDetialPopUpWindow extends PopupWindow {

    private Context mContext;

    private LayoutInflater mInflater;

    private View mContentView;

    public NewsDetialPopUpWindow(Context context) {
        super(context);

        this.mContext = context;

        // 渲染布局
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.newsdetail_popup_layout, null);

        // 设置View
        setContentView(mContentView);

        // 设置宽高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(DensityUtil.dip2px(mContext, 300));

        // 设置进出动画
//        setAnimationStyle(R.style.NewsDetailPopUp);

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

        initView();
    }

    private void initView() {
        LinearLayout ll_comment = (LinearLayout) mContentView.findViewById(R.id.ll_comment);
        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "评论", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        LinearLayout ll_like = (LinearLayout) mContentView.findViewById(R.id.ll_like);
        ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点赞", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        LinearLayout ll_dislike = (LinearLayout) mContentView.findViewById(R.id.ll_dislike);
        ll_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "讨厌", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        FloatingActionButton fab_popup_more = (FloatingActionButton) mContentView.findViewById(R.id.fab_popup_more);
        fab_popup_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}






























