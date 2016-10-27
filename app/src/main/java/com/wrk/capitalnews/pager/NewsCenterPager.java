package com.wrk.capitalnews.pager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.adapter.NewsCenterRecyclerviewAdapter;
import com.wrk.capitalnews.base.BasePager;
import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.bean.PhotosDetailPagerBean;
import com.wrk.capitalnews.utils.Constants;
import com.wrk.capitalnews.utils.DownLoaderUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 新闻页面
 * -------------------=.=------------------------
 */

public class NewsCenterPager extends BasePager {
    private Context mContext;

    private NewsContentBean.DataEntity newsCenterPagerData;

    private List<PhotosDetailPagerBean.DataEntity.NewsEntity> mNewsEntities;

    private DownLoaderUtils mDownLoaderUtils;

    private RecyclerView newscenter_recyclerView;

    private SwipeRefreshLayout newscenter_swiperefrelayput;

    private boolean isGrid = false;

    private NewsCenterRecyclerviewAdapter mAdapter;
    private View mView;
    private String photosUrl;


    public NewsCenterPager(Context context, int type) {
        super(context, type);

        this.mContext = context;

        mNewsEntities = new ArrayList<>();
    }


    @Override
    public void initData() {
        super.initData();

        getData();

        mView = View.inflate(mContext, R.layout.newscenter_framlayout, null);

        newscenter_recyclerView = (RecyclerView) mView.findViewById(R.id.newscenter_recyclerView);
        newscenter_swiperefrelayput = (SwipeRefreshLayout) mView.findViewById(R.id.newscenter_swiperefrelayput);

        initSwipeRefreshLayout();

        ib_basepager_switch.setVisibility(View.VISIBLE);
        tvBasepagerTitle.setText("新闻");

    }

    private void initSwipeRefreshLayout() {
        // 设置小圈圈的颜色
        newscenter_swiperefrelayput.
                setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_blue_bright);

        newscenter_swiperefrelayput.setSize(2);

        // 设置下拉刷新的监听
        newscenter_swiperefrelayput.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                newscenter_swiperefrelayput.setRefreshing(false);
            }
        });

    }

    private void getData() {
        mDownLoaderUtils = new DownLoaderUtils();
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

                        List<NewsContentBean.DataEntity> data = new Gson().fromJson(s, NewsContentBean.class).getData();

                        newsCenterPagerData = data.get(2);

                        photosUrl = Constants.BASE_URL + newsCenterPagerData.getUrl();

                        Log.e("111", photosUrl);

                        getPhotosData(photosUrl);

                    }
                });

    }


    public void getPhotosData(String url) {
        mDownLoaderUtils = new DownLoaderUtils();
        mDownLoaderUtils.getJsonResult(url)
                .subscribeOn(Schedulers.io()) // 让subscribe的操作执行在异步线程
                .observeOn(AndroidSchedulers.mainThread()) // 让订阅者代码执行在主线程
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        newscenter_swiperefrelayput.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        PhotosDetailPagerBean bean = new Gson().fromJson(s, PhotosDetailPagerBean.class);

                        mNewsEntities = bean.getData().getNews();
                        Log.e("111", mNewsEntities.toString());
                        if (mNewsEntities != null && mNewsEntities.size() > 0) {
                            ib_basepager_switch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (!isGrid) {
                                        newscenter_recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                                        isGrid = true;
                                        ib_basepager_switch.setImageResource(R.drawable.icon_pic_list_type);
                                    } else {
                                        newscenter_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                        isGrid = false;
                                        ib_basepager_switch.setImageResource(R.drawable.icon_pic_grid_type);
                                    }

                                }
                            });
                            newscenter_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            mAdapter = new NewsCenterRecyclerviewAdapter(mContext, mNewsEntities);
                            newscenter_recyclerView.setAdapter(mAdapter);
                            flBasepagerContent.addView(mView);

                        }

                    }
                });


    }


}

























