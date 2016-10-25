package com.wrk.capitalnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wrk.capitalnews.bean.PhotosDetailPagerBean;

import java.util.List;

/**
 * Created by MrbigW on 2016/10/25.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: NewsCenterRecyclerviewAdapter适配器
 * -------------------=.=------------------------
 */

public class NewsCenterRecyclerviewAdapter extends RecyclerView.Adapter<NewsCenterRecyclerviewAdapter.ViewHolder> {

    private Context mContext;

    private List<PhotosDetailPagerBean.DataEntity.NewsEntity> mNewsEntities;

    public NewsCenterRecyclerviewAdapter(Context context, List<PhotosDetailPagerBean.DataEntity.NewsEntity> newsEntities) {
        this.mContext = context;
        this.mNewsEntities = newsEntities;
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
