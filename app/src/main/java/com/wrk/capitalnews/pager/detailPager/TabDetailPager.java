package com.wrk.capitalnews.pager.detailPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.base.HomeDetailBasePager;
import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.bean.TabDetailPagerBean;
import com.wrk.capitalnews.utils.CacheUtils;
import com.wrk.capitalnews.utils.Constants;
import com.wrk.capitalnews.utils.DensityUtil;
import com.wrk.capitalnews.utils.DownLoaderUtils;
import com.wrk.capitalnews.view.HorizontalScrollViewPager;
import com.wrk.capitalnews.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by MrbigW on 2016/10/20.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class TabDetailPager extends HomeDetailBasePager {

    private static final String READ_ARRAY_ID = "read_array_id";
    private MyListView lv_tabdetail;

    private HorizontalScrollViewPager topnews_viewpager;
    private TextView tv_topnews_title;
    private LinearLayout ll_newstop_pg;

    private DownLoaderUtils mDownUtils;

    private NewsContentBean.DataBean.ChildrenBean children;

    private List<TabDetailPagerBean.DataBean.NewsBean> mNewsBeanList;

    private TabDetailAdapter mAdapter;

    private String mUrl;

    private String moreUrl;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> mTopnews;

    private InternalHandler mHandler;

    public TabDetailPager(Context context, NewsContentBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.children = childrenBean;
        mNewsBeanList = new ArrayList<>();
    }

    @Override
    public View initView() {

        View view = View.inflate(mContext, R.layout.tabdetail_pager, null);
        lv_tabdetail = (MyListView) view.findViewById(R.id.lv_tabdetail);

        View topNewsVP = View.inflate(mContext, R.layout.top_news_layout, null);
        topnews_viewpager = (HorizontalScrollViewPager) topNewsVP.findViewById(R.id.topnews_viewpager);
        tv_topnews_title = (TextView) topNewsVP.findViewById(R.id.tv_topnews_title);
        ll_newstop_pg = (LinearLayout) topNewsVP.findViewById(R.id.ll_newstop_pg);

        lv_tabdetail.addHeaderView(topNewsVP);

        lv_tabdetail.setOnLoadInterface(new TabDetailILoadListenner());

        lv_tabdetail.setOnItemClickListener(new TabDetailOnItemClickListener());

        return view;
    }

    class TabDetailOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int realPos = position - 2;

            TabDetailPagerBean.DataBean.NewsBean newsBean = mNewsBeanList.get(realPos);

            // 先把保存的取出来，如果没有保存过，就保存，并刷新适配器
            String read_array_id = CacheUtils.getString(mContext, READ_ARRAY_ID); // ""

            if (!read_array_id.contains(newsBean.getId() + "")) {
                // 将之前的要保存
                CacheUtils.putString(mContext, READ_ARRAY_ID, read_array_id + newsBean.getId() + ",");

                mAdapter.notifyDataSetChanged();
            }
        }
    }

    class TabDetailILoadListenner implements MyListView.ILoadListenner {

        @Override
        public void onLoad() {
            if (TextUtils.isEmpty(moreUrl)) {
                Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                lv_tabdetail.loadComplete();
            } else {
                // 开始加载
                getMoreDataFromNet();
            }
        }

        @Override
        public void onReflash() {
            getDataFromNet(mUrl);
        }
    }

    private void getMoreDataFromNet() {
        mDownUtils = new DownLoaderUtils();
        mDownUtils.getJsonResult(moreUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        lv_tabdetail.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        lv_tabdetail.loadComplete();
                    }

                    @Override
                    public void onNext(String s) {
                        isLoadMore = true;
                        processData(s);
                    }
                });

    }

    @Override
    public void initData() {
        super.initData();

        mUrl = Constants.BASE_URL + children.getUrl();
        Log.e("111", mUrl);
        String saveJson = CacheUtils.getString(mContext, null);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        Log.e("111", mUrl);
        getDataFromNet(mUrl);
    }

    private void getDataFromNet(final String url) {

        mDownUtils = new DownLoaderUtils();
        mDownUtils.getJsonResult(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        lv_tabdetail.reflashComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        lv_tabdetail.reflashComplete();
                    }

                    @Override
                    public void onNext(String s) {
                        CacheUtils.putString(mContext, url, s);
                        processData(s);
                    }
                });
    }

    private boolean isLoadMore;

    private void processData(String s) {
        TabDetailPagerBean tabDetailPagerBean = parseJson(s);

        mTopnews = tabDetailPagerBean.getData().getTopnews();

        moreUrl = "";
        if (TextUtils.isEmpty(tabDetailPagerBean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + tabDetailPagerBean.getData().getMore();
        }

        if (!isLoadMore) {
            if (mTopnews != null && mTopnews.size() > 0) {
                topnews_viewpager.setAdapter(new TopNewsPagerAdapter());
                topnews_viewpager.addOnPageChangeListener(new TopNewsOnPageChangeListener());
                tv_topnews_title.setText(mTopnews.get(prePosintion).getTitle());

                ll_newstop_pg.removeAllViews();
                for (int i = 0; i < mTopnews.size(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setBackgroundResource(R.drawable.point_selector);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 8), DensityUtil.dip2px(mContext, 8));

                    if (i == 0) {
                        imageView.setEnabled(true);
                    } else {
                        imageView.setEnabled(false);
                        params.leftMargin = DensityUtil.dip2px(mContext, 5);
                    }

                    imageView.setLayoutParams(params);

                    ll_newstop_pg.addView(imageView);
                }
            }

            mNewsBeanList = tabDetailPagerBean.getData().getNews();
            mAdapter = new TabDetailAdapter();
            lv_tabdetail.setAdapter(mAdapter);
        } else {
            // 加载更多
            isLoadMore = false;
            mNewsBeanList.addAll(tabDetailPagerBean.getData().getNews());

            mAdapter.notifyDataSetChanged();
        }


        if (mHandler == null) {
            mHandler = new InternalHandler();
        }

        // 先移除后发
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new MyRunnable(), 4000);
    }

    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mTopnews != null && mTopnews.size() > 0) {
                int item = (topnews_viewpager.getCurrentItem() + 1) % mTopnews.size();
                topnews_viewpager.setCurrentItem(item);
                mHandler.postDelayed(new MyRunnable(), 4000);
            }
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }

    // 之前高亮的点
    private int prePosintion;
    // 是否正在拖拽
    private boolean isDragging;

    class TopNewsOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 将之前高亮的点设置为默认
            ll_newstop_pg.getChildAt(prePosintion).setEnabled(false);
            // 把现在的点设置为高亮
            ll_newstop_pg.getChildAt(position).setEnabled(true);
            // 注意
            prePosintion = position;
        }

        @Override
        public void onPageSelected(int position) {
            tv_topnews_title.setText(mTopnews.get(position).getTitle());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
                if (mHandler != null) {

                    mHandler.removeCallbacksAndMessages(null);
                }

            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {
                isDragging = false;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new MyRunnable(), 4000);
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                isDragging = false;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new MyRunnable(), 4000);
            }
        }
    }

    class TopNewsPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mTopnews != null && mTopnews.size() > 0) {
                return mTopnews.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            SimpleDraweeView draweeView = new SimpleDraweeView(mContext);
            draweeView.setBackgroundResource(R.drawable.news_pic_default);
            draweeView.setImageURI(Constants.BASE_URL + mTopnews.get(position).getTopimage());

            container.addView(draweeView);

            draweeView.setOnTouchListener(new TopNewsOnTouchListener());

            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "被点击", Toast.LENGTH_SHORT).show();
                }
            });

            return draweeView;
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

    class TopNewsOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    if (mHandler != null) {
                        mHandler.postDelayed(new MyRunnable(), 4000);
                    }
                    break;
            }

            return false;
        }
    }

    private TabDetailPagerBean parseJson(String s) {
        return new Gson().fromJson(s, TabDetailPagerBean.class);
    }

    class TabDetailAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TabDetailPagerBean.DataBean.NewsBean newsBean = mNewsBeanList.get(position);

            viewHolder.tvTitle.setText(newsBean.getTitle());
            viewHolder.tvTime.setText(newsBean.getPubdate());
            viewHolder.ivIcon.setImageURI(Constants.BASE_URL + newsBean.getListimage());

            String saveReadArrayId = CacheUtils.getString(mContext, READ_ARRAY_ID);
            if (saveReadArrayId.contains(newsBean.getId() + "")) {
                viewHolder.tvTitle.setTextColor(Color.GRAY);
            } else {
                viewHolder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        SimpleDraweeView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
