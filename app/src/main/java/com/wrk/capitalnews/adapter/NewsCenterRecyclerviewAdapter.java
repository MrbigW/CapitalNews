package com.wrk.capitalnews.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.activity.ActivityTranstionToActivity;
import com.wrk.capitalnews.bean.PhotosDetailPagerBean;
import com.wrk.capitalnews.utils.Constants;

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

        View itemView = View.inflate(mContext, R.layout.item_recycler, null);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_title.setText(mNewsEntities.get(position).getTitle());
        holder.img.setImageURI(Constants.BASE_URL + mNewsEntities.get(position).getLargeimage());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transition(holder.img, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView img;
        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (SimpleDraweeView) itemView.findViewById(R.id.sdv_recycler_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_recycler_title);


        }
    }

    private void transition(View view, int pos) {
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(mContext, "版本过低", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(mContext, ActivityTranstionToActivity.class);
            intent.setData(Uri.parse(mNewsEntities.get(pos).getLargeimage()));
            ActivityOptionsCompat optionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, view, "photos");
            mContext.startActivity(intent, optionsCompat.toBundle());

        }

    }

}



















