package com.wrk.capitalnews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrk.capitalnews.R;
import com.wrk.capitalnews.bean.NewsContentBean;

import java.util.ArrayList;

/**
 * Created by MrbigW on 2016/10/20.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class QuickChannelsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NewsContentBean.DataBean.ChildrenBean> mQuickChannels;

    private ArrayList<NewsContentBean.DataBean.ChildrenBean> mOtherChannels;


    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    private boolean isEditing = false;

    public QuickChannelsAdapter(Context context, ArrayList<NewsContentBean.DataBean.ChildrenBean> quickChannles) {
        this.mContext = context;
        this.mQuickChannels = quickChannles;
        mOtherChannels = new ArrayList<>();
    }

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

        convertView = View.inflate(mContext, R.layout.gvitem_channels_layout, null);

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
                mQuickChannels.remove(position);
                mOtherChannels.add(mQuickChannels.get(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public ArrayList<NewsContentBean.DataBean.ChildrenBean> getOtherChannels() {
        return mOtherChannels;
    }

    public void setOtherChannels(ArrayList<NewsContentBean.DataBean.ChildrenBean> otherChannels) {
        mOtherChannels = otherChannels;
    }
}
