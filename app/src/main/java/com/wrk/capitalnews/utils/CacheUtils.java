package com.wrk.capitalnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MrbigW on 2016/10/18.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 缓存软件的一些参数和数据
 * -------------------=.=------------------------
 */

public class CacheUtils {


    /**
     * 得到缓存值
     *
     * @param context
     * @param ksy
     * @return
     */
    public static boolean getBoolean(Context context, String ksy) {

        SharedPreferences sp = context.getSharedPreferences("capital_news", Context.MODE_PRIVATE);

        return sp.getBoolean(ksy, false);
    }

    /**
     * 保存软件参数
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("capital_news", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }
}
