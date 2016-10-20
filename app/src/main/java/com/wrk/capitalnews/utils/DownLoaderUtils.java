package com.wrk.capitalnews.utils;

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

    private OkManager okManager;

    public DownLoaderUtils() {

        okManager = OkManager.getInstance();
    }

    public Observable<String> getJsonResult(final String path) {

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    okManager.asyncJsonStrngByURL(path, new OkManager.Func1() {
                        @Override
                        public void onResponse(String result) {
                            subscriber.onNext(result);
                            subscriber.onCompleted();
                        }

                    });
                }
            }
        });
    }
}