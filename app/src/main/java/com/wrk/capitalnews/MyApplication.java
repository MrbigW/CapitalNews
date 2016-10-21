package com.wrk.capitalnews;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by MrbigW on 2016/10/21.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
