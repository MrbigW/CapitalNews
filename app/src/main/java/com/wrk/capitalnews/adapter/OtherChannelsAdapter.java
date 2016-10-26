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

public class OtherChannelsAdapter extends BaseAdapter {

    private Context mContext;


    private ArrayList<NewsContentBean.DataEntity.ChildrenEntity> mQuickChannels;

    private ArrayList<NewsContentBean.DataEntity.ChildrenEntity> mOtherChannels;


    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    private boolean isEditing = false;

    public OtherChannelsAdapter(Context context, ArrayList<NewsContentBean.DataEntity.ChildrenEntity> OtherChannels) {
        this.mContext = context;
        this.mOtherChannels = OtherChannels;
        mQuickChannels = new ArrayList<>();
    }

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

        tvChannelsName.setText(mOtherChannels.get(position).getTitle());

        ivChannelsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOtherChannels.remove(position);
                mQuickChannels.add(mQuickChannels.get(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public ArrayList<NewsContentBean.DataEntity.ChildrenEntity> getQuickChannels() {
        return mQuickChannels;
    }

    public void setQuickChannels(ArrayList<NewsContentBean.DataEntity.ChildrenEntity> quickChannels) {
        mQuickChannels = quickChannels;
    }


}
