package com.wrk.capitalnews.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wrk.capitalnews.R;
import com.wrk.capitalnews.utils.Constants;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ActivityTranstionToActivity extends AppCompatActivity {

    private PhotoView mPhotoView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transtion);

        mPhotoView = (PhotoView) findViewById(R.id.iv_photo);

        mAttacher = new PhotoViewAttacher(mPhotoView);

        Glide.with(this).load(Constants.BASE_URL + getIntent().getData()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mPhotoView.setImageBitmap(resource);
                mAttacher.update();
            }
        });

    }

}
