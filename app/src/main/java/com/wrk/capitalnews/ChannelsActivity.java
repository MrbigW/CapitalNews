package com.wrk.capitalnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrk.capitalnews.activity.OtherChannelActivity;
import com.wrk.capitalnews.bean.NewsContentBean;
import com.wrk.capitalnews.utils.CacheUtils;
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
    private ArrayList<NewsContentBean.DataEntity.ChildrenEntity> mQuickChannels;
    // 其他频道
    private ArrayList<NewsContentBean.DataEntity.ChildrenEntity> mOtherChannels;

    private boolean isEditing = false;

    private QuickChannelsAdapter1 mQuickChannelsAdapter;
    private OtherChannelsAdapter1 mOtherChannelsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        ButterKnife.bind(this);

        mQuickChannels = new ArrayList<>();
        mQuickChannels = (ArrayList<NewsContentBean.DataEntity.ChildrenEntity>) getIntent().getSerializableExtra("channels");

        mOtherChannels = new ArrayList<>();

        for (int i = 0; i < mQuickChannels.size(); i++) {
            if (!CacheUtils.getChannelsString(this, mQuickChannels.get(i).getUrl()).equals(mQuickChannels.get(i).getTitle())) {
                mOtherChannels.add(mQuickChannels.get(i));
            }
        }

        mQuickChannels.removeAll(mOtherChannels);

        mQuickChannelsAdapter = new QuickChannelsAdapter1();
        mOtherChannelsAdapter = new OtherChannelsAdapter1();


        gvChannels.setAdapter(mQuickChannelsAdapter);
        gvChannelsDelete.setAdapter(mOtherChannelsAdapter);

        gvChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("channel", mQuickChannels.get(position).getTitle());
                intent.putExtra("count", mQuickChannels.size());
                ChannelsActivity.this.setResult(1, intent);
                finish();
            }
        });

        gvChannelsDelete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChannelsActivity.this, OtherChannelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("otherchannel", mOtherChannels.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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

            ivChannelsDelete.setImageResource(R.drawable.delete);

            if (isEditing) {
                if (position != 0) {
                    ivChannelsDelete.setVisibility(View.VISIBLE);
                    ivChannelsDelete.setEnabled(true);
                }
                gvChannels.setEnabled(false);
                gvChannelsDelete.setEnabled(false);
            } else {
                if (position != 0) {
                    ivChannelsDelete.setVisibility(View.INVISIBLE);
                    ivChannelsDelete.setEnabled(false);
                }
            }

            tvChannelsName.setText(mQuickChannels.get(position).getTitle());

            ivChannelsDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsContentBean.DataEntity.ChildrenEntity childrenBean = mQuickChannels.get(position);
                    mQuickChannels.remove(childrenBean);
                    // 修改Sp中频道信息
                    CacheUtils.putChannelsString(ChannelsActivity.this, childrenBean.getUrl(), "");

                    mOtherChannels.add(childrenBean);

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

            ImageView ivChannelsAdd = (ImageView) convertView.findViewById(R.id.iv_channels_delete);


            TextView tvChannelsName = (TextView) convertView.findViewById(R.id.tv_channels_name);

            ivChannelsAdd.setImageResource(R.drawable.add);

            if (isEditing) {
                ivChannelsAdd.setVisibility(View.VISIBLE);
                ivChannelsAdd.setEnabled(true);
            } else {
                ivChannelsAdd.setVisibility(View.INVISIBLE);
                ivChannelsAdd.setEnabled(false);
            }

            tvChannelsName.setText(mOtherChannels.get(position).getTitle());

            ivChannelsAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsContentBean.DataEntity.ChildrenEntity childrenBean = mOtherChannels.get(position);
                    mOtherChannels.remove(childrenBean);
                    // 修改Sp中频道信息
                    CacheUtils.putChannelsString(ChannelsActivity.this, childrenBean.getUrl(), childrenBean.getTitle());
                    mQuickChannels.add(childrenBean);
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
                Intent intent = new Intent();
                Bundle data = new Bundle();
                data.putSerializable("quickchannels", mQuickChannels);
                intent.putExtras(data);
                ChannelsActivity.this.setResult(2, intent);
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




































