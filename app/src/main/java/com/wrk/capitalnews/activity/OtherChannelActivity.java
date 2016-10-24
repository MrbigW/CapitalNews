package com.wrk.capitalnews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.pager.detailPager.TabDetailPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherChannelActivity extends Activity {

    @BindView(R.id.fg_otherchannel)
    FrameLayout fgOtherchannel;
    @BindView(R.id.ib_basepager_menu)
    ImageButton ibBasepagerMenu;
    @BindView(R.id.tv_basepager_title)
    TextView tvBasepagerTitle;

    private NewsContentBean.DataBean.ChildrenBean mChildrenBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherchannel);
        ButterKnife.bind(this);

        mChildrenBean = (NewsContentBean.DataBean.ChildrenBean) getIntent().getSerializableExtra("otherchannel");
        TabDetailPager tabDetailPager = new TabDetailPager(this, mChildrenBean);

        tabDetailPager.initData();

        ibBasepagerMenu.setImageResource(R.drawable.back);
        ibBasepagerMenu.setVisibility(View.VISIBLE);

        tvBasepagerTitle.setText(mChildrenBean.getTitle());

        fgOtherchannel.addView(tabDetailPager.rootView);

    }


    @OnClick(R.id.ib_basepager_menu)
    public void back() {
        finish();
    }
}
