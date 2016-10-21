package com.wrk.capitalnews.pager.detailPager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.base.HomeDetailBasePager;
import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.bean.TabDetailPagerBean;
import com.wrk.capitalnews.utils.CacheUtils;
import com.wrk.capitalnews.utils.Constants;
import com.wrk.capitalnews.utils.DownLoaderUtils;
import com.wrk.capitalnews.utils.LogUtil;

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


    private ListView lv_tabdetail;

    private DownLoaderUtils mDownUtils;

    private NewsContentBean.DataBean.ChildrenBean children;

    private List<TabDetailPagerBean.DataBean.NewsBean> mNewsBeanList;

    private TabDetailAdapter mAdapter;

    private String mUrl;

    public TabDetailPager(Context context, NewsContentBean.DataBean.ChildrenBean childrenBean) {
        super(context);

        this.children = childrenBean;
        mDownUtils = new DownLoaderUtils();
        mNewsBeanList = new ArrayList<>();
    }

    @Override
    public View initView() {

        View view = View.inflate(mContext, R.layout.tabdetail_pager, null);
        lv_tabdetail = (ListView) view.findViewById(R.id.lv_tabdetail);

        return view;
    }


    @Override
    public void initData() {
        super.initData();

        mUrl = Constants.BASE_URL + children.getUrl();

        String saveJson = CacheUtils.getJsonString(mContext, null);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        getDataFromNet(mUrl);
    }

    private void getDataFromNet(final String url) {

        mDownUtils.getJsonResult(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        CacheUtils.putJsonString(mContext, url, s);
                        processData(s);
                    }
                });
    }


    private void processData(String s) {
        TabDetailPagerBean tabDetailPagerBean = parseJson(s);


        mNewsBeanList = tabDetailPagerBean.getData().getNews();

        mAdapter = new TabDetailAdapter();

        lv_tabdetail.setAdapter(mAdapter);
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
