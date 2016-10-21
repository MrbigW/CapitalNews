package com.wrk.capitalnews.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by MrbigW on 2016/10/19.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 获取网络数据返回Json---通过RxAndroid与Okhttp
 * -------------------=.=------------------------
 */

public class DownLoaderUtils {

    private OkHttpClient mClient;

    public DownLoaderUtils() {
        mClient = new OkHttpClient();
    }

    public Observable<String> getJsonResult(final String path) {

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    // 访问网络操作、
                    Request request = new Request.Builder().url(path).build();
                    mClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                subscriber.onNext(response.body().string());
                            }
                            // 结束
                            mClient = null;
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });
    }


}































