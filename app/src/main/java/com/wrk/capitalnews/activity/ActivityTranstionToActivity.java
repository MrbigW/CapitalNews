package com.wrk.capitalnews.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ActivityTranstionToActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_photo)
    PhotoView ivPhoto;

    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transtion);
        ButterKnife.bind(this);

        mAttacher = new PhotoViewAttacher(ivPhoto);

        Glide.with(this).load(Constants.BASE_URL + getIntent().getStringExtra("imageurl")).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                tvTitle.setText(getIntent().getStringExtra("title"));
                ivPhoto.setImageBitmap(resource);
                mAttacher.update();
            }
        });


    }

}
