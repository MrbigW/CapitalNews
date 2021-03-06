package com.wrk.capitalnews.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.adapter.ShoppingMallAdapter;
import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.bean.ShoppingMallBean;
import com.wrk.capitalnews.utils.CacheUtils;
import com.wrk.capitalnews.utils.Constants;
import com.wrk.capitalnews.utils.DownLoaderUtils;
import com.wrk.capitalnews.utils.LogUtil;
import com.wrk.capitalnews.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 商城页面
 * -------------------=.=------------------------
 */

public class ShoppingMallPager extends BasePager {

    private MaterialRefreshLayout shopping_refresh;

    private RecyclerView shopping_recyclerview;

    private RelativeLayout rl_view;

    private ProgressBar pb_loading;

    private DownLoaderUtils mDownLoaderUtils;

    // 每页请求多少数据
    private int pageSize = 10;
    // 第几页
    private int curPage = 1;
    // 共多少页
    private int mTotalPager;


    // 默认状态
    private static final int STATE_NORMAL = 1;
    // 下拉刷新
    private static final int STATE_REFRESH = 2;
    // 上拉加载
    private static final int STATE_LOADMORE = 3;

    private int currentState = STATE_NORMAL;


    private List<ShoppingMallBean.Wares> mBeanList;
//    private List<ShoppingMallBean.Wares> refreshData;

    private ShoppingMallAdapter mAdapter;
    private String mUrl;

    public ShoppingMallPager(Context context, int type) {
        super(context, type);
        mBeanList = new ArrayList<>();
    }

    @Override
    public void initData() {
        super.initData();

        tvBasepagerTitle.setText("商城");

        View view = View.inflate(mContext, R.layout.shopping_pager, null);

        shopping_refresh = (MaterialRefreshLayout) view.findViewById(R.id.shopping_refresh);
        shopping_recyclerview = (RecyclerView) view.findViewById(R.id.shopping_recyclerview);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        rl_view = (RelativeLayout) view.findViewById(R.id.rl_view);

        flBasepagerContent.removeAllViews();

        flBasepagerContent.addView(view);

        shopping_recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));


        initMaterialRefresh();

        getDataFromNet();

    }

    private void getDataFromNet() {
        curPage = 1;
        mUrl = Constants.WARES_HOT_URL + "pageSize=" + pageSize + "&curPage=" + curPage;

        String saveJson = CacheUtils.getString(mContext, Constants.WARES_HOT_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        mDownLoaderUtils = new DownLoaderUtils();
        mDownLoaderUtils.getJsonResult(mUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        pb_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        CacheUtils.putString(mContext, Constants.WARES_HOT_URL, s);
                        processData(s);
                    }
                });

    }


    private void processData(String json) {
        ShoppingMallBean mallBean = new Gson().fromJson(json, ShoppingMallBean.class);

        curPage = mallBean.getCurrentPage();
        pageSize = mallBean.getPageSize();
        mTotalPager = mallBean.getTotalCount();
        mBeanList = mallBean.getList();
        if (mBeanList != null && mBeanList.size() > 0) {
            // 设置适配器
            showData();
        }
        pb_loading.setVisibility(View.GONE);
    }

    private void showData() {

        switch (currentState) {
            case STATE_NORMAL:
                // 设置适配器
                mAdapter = new ShoppingMallAdapter(mContext, mBeanList, rl_view);
                shopping_recyclerview.setAdapter(mAdapter);
                shopping_recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                break;
            case STATE_REFRESH:
                // 清除数据
                mAdapter.clearData();
                mAdapter.addData(0, mBeanList);
                shopping_refresh.finishRefresh(); // 还原下拉刷新的状态

                break;
            case STATE_LOADMORE:
                mAdapter.addData(mAdapter.getItemCount(), mBeanList);
                shopping_refresh.finishRefreshLoadMore();
                break;
        }

    }


    private void initMaterialRefresh() {
        shopping_refresh.setSunStyle(true);
        shopping_refresh.setMaterialRefreshListener(new ShoppingMallMaterialRefreshListener());
    }

    class ShoppingMallMaterialRefreshListener extends MaterialRefreshListener {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            refreshData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);
            loadMore();
        }
    }

    private void loadMore() {
        currentState = STATE_LOADMORE;
        if (curPage < mTotalPager) {
            curPage += 1;
            mUrl = Constants.WARES_HOT_URL + "pageSize=" + pageSize + "&curPage=" + curPage;
            mDownLoaderUtils = new DownLoaderUtils();
            mDownLoaderUtils.getJsonResult(mUrl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            shopping_refresh.finishRefreshLoadMore();
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                        }

                        @Override
                        public void onNext(String s) {
                            CacheUtils.putString(mContext, Constants.WARES_HOT_URL, s);
                            mAdapter.addData(mBeanList.size(), new Gson().fromJson(s, ShoppingMallBean.class).getList());
                        }
                    });
        } else {
            shopping_refresh.finishRefreshLoadMore();
            ToastUtil.showToast(mContext, "无更多数据");
        }
    }

    private void refreshData() {
        currentState = STATE_REFRESH;
        curPage = 1;
        mUrl = Constants.WARES_HOT_URL + "pageSize=" + pageSize + "&curPage=" + curPage;
        mDownLoaderUtils = new DownLoaderUtils();
        mDownLoaderUtils.getJsonResult(mUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        shopping_refresh.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        CacheUtils.putString(mContext, Constants.WARES_HOT_URL, s);
                    }
                });
    }
}








































