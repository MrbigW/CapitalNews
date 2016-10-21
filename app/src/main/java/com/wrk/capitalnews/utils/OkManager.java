package com.wrk.capitalnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by MrbigW on 2016/10/17.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 封装Okhttp
 * -------------------=.=------------------------
 */

public class OkManager {

    private OkHttpClient mClient;

    private volatile static OkManager sOkManager;

    private final String TAG = OkManager.class.getSimpleName();

    private Handler mHandler;

    // 提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    // 提交字符串
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    private OkManager() {
        mClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());

    }

    public static OkManager getInstance() {
        OkManager instance = null;
        if (sOkManager == null) {
            synchronized (OkManager.class) {
                if (instance == null) {
                    instance = new OkManager();
                    sOkManager = instance;
                }
            }
        }
        return instance;
    }



    /**
     * 向服务器端提交表单数据
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void sendComplexFrom(String url, Map<String, String> params, final Func4 callBack) {
        FormBody.Builder form_builder = new FormBody.Builder();// 表单对象，包含以input开始的对象，以html表单为主
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                form_builder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody requestBody = form_builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build(); // 使用post的方式提交
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                }
            }
        });

    }

    /**
     * 向服务器提交字符串数据
     *
     * @param url
     * @param content
     * @param callBack
     */
    public void sendStringByPostMethod(String url, String content, final Func4 callBack) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, content)).build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                }
            }
        });

    }

    /**
     * 请求指定的url返回的结果是json字符串
     *
     * @param url
     * @param callBack
     */
    public void asyncJsonStrngByURL(String url, final Func1 callBack) {
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 请求指定的url返回的结果是jsonObject
     *
     * @param url
     * @param callBack
     */
    public void asyncJsonObjectByURL(final String url, final Func4 callBack) {
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 请求指定的url返回的结果是bytes
     *
     * @param url
     * @param callBack
     */
    public void asyncBytesByURL(final String url, final Func2 callBack) {
        final Request request = new Request.Builder().url(url).build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessBytesMethod(response.body().bytes(), callBack);
                }
            }
        });

    }

    public void asyncBitmapByURL(final String url, final Func3 callBack) {
        final Request request = new Request.Builder().url(url).build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    onSuccrssBitmapMethod(bitmap, callBack);
                }
            }
        });

    }

    /**
     * 请求返回的结果是json字符串
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonValue, final Func1 callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 返回响应的结果是json对象
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonObjectMethod(final String jsonValue, final Func4 callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(new JSONObject(jsonValue));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 返回响应的结果是byte数组
     *
     * @param data
     * @param callBack
     */
    private void onSuccessBytesMethod(final byte[] data, final Func2 callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(data);
                }
            }
        });
    }

    /**
     * 返回响应的结果是bitmap
     *
     * @param bm
     * @param callBack
     */
    private void onSuccrssBitmapMethod(final Bitmap bm, final Func3 callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(bm);
                }
            }
        });
    }

    interface Func1 {
        void onResponse(String result);
    }

    interface Func2 {
        void onResponse(byte[] result);
    }

    interface Func3 {
        void onResponse(Bitmap bitmap);
    }

    interface Func4 {
        void onResponse(JSONObject jsonObject);
    }

}

















































