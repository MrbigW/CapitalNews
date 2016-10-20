package com.wrk.capitalnews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.utils.LogUtil;
import com.wrk.capitalnews.view.NoScrollGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 频道Activity
 */

public class ChannelsActivity extends Activity {


    @BindView(R.id.ib_channels_back)
    ImageButton ibChannelsBack;
    @BindView(R.id.tv_channels_edit)
    TextView tvChannelsEdit;
    @BindView(R.id.gv_channels)
    NoScrollGridView gvChannels;
    @BindView(R.id.gv_channels_delete)
    NoScrollGridView gvChannelsDelete;

    // 快捷频道
    private ArrayList<NewsContentBean.DataBean.ChildrenBean> mQuickChannels;
    // 其他频道
    private ArrayList<NewsContentBean.DataBean.ChildrenBean> mOtherChannels;

    private boolean isEditing = false;

    private QuickChannelsAdapter1 mQuickChannelsAdapter;
    private OtherChannelsAdapter1 mOtherChannelsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        ButterKnife.bind(this);

        mQuickChannels = new ArrayList<>();
        mQuickChannels = (ArrayList<NewsContentBean.DataBean.ChildrenBean>) getIntent().getSerializableExtra("channels");

        mOtherChannels = new ArrayList<>();

        mQuickChannelsAdapter = new QuickChannelsAdapter1();
        mOtherChannelsAdapter = new OtherChannelsAdapter1();

        gvChannels.setAdapter(mQuickChannelsAdapter);
        gvChannelsDelete.setAdapter(mOtherChannelsAdapter);


    }

    class QuickChannelsAdapter1 extends BaseAdapter {
        @Override
        public int getCount() {
            return mQuickChannels.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(ChannelsActivity.this, R.layout.gvitem_channels_layout, null);

            ImageView ivChannelsDelete = (ImageView) convertView.findViewById(R.id.iv_channels_delete);

            TextView tvChannelsName = (TextView) convertView.findViewById(R.id.tv_channels_name);


            if (isEditing) {
                ivChannelsDelete.setVisibility(View.VISIBLE);
                ivChannelsDelete.setEnabled(true);
            } else {
                ivChannelsDelete.setVisibility(View.INVISIBLE);
                ivChannelsDelete.setEnabled(false);
            }

            tvChannelsName.setText(mQuickChannels.get(position).getTitle());

            ivChannelsDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsContentBean.DataBean.ChildrenBean childrenBean = mQuickChannels.get(position);
                    mQuickChannels.remove(childrenBean);
                    mOtherChannels.add(childrenBean);
                    LogUtil.e(childrenBean.toString());
                    mOtherChannelsAdapter.notifyDataSetChanged();
                    mQuickChannelsAdapter.notifyDataSetChanged();

                }
            });

            return convertView;
        }
    }

    class OtherChannelsAdapter1 extends BaseAdapter {

        @Override
        public int getCount() {
            return mOtherChannels.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(ChannelsActivity.this, R.layout.gvitem_channels_layout, null);

            ImageView ivChannelsDelete = (ImageView) convertView.findViewById(R.id.iv_channels_delete);

            TextView tvChannelsName = (TextView) convertView.findViewById(R.id.tv_channels_name);


            if (isEditing) {
                ivChannelsDelete.setVisibility(View.VISIBLE);
                ivChannelsDelete.setEnabled(true);
            } else {
                ivChannelsDelete.setVisibility(View.INVISIBLE);
                ivChannelsDelete.setEnabled(false);
            }

            tvChannelsName.setText(mOtherChannels.get(position).getTitle());

            ivChannelsDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsContentBean.DataBean.ChildrenBean childrenBean = mOtherChannels.get(position);
                    mOtherChannels.remove(childrenBean);
                    mQuickChannels.add(childrenBean );
                    mQuickChannelsAdapter.notifyDataSetChanged();
                    mOtherChannelsAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }


    @OnClick({R.id.ib_channels_back, R.id.tv_channels_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_channels_back:
                finish();
                break;
            case R.id.tv_channels_edit:
                if (!isEditing) {
                    tvChannelsEdit.setText("完成");
                    isEditing = true;
                    mQuickChannelsAdapter.notifyDataSetChanged();
                    mOtherChannelsAdapter.notifyDataSetChanged();
                } else {
                    tvChannelsEdit.setText("编辑");
                    isEditing = false;
                    mQuickChannelsAdapter.notifyDataSetChanged();
                    mOtherChannelsAdapter.notifyDataSetChanged();
                }

                break;

        }
    }
}




































