package com.wrk.capitalnews.utils;

/**
 * Created by MrbigW on 2016/10/17.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 联网请求地址配置
 * -------------------=.=------------------------
 */

public class Constants {

    /**
     * 请求网络的公共请求地址
     */
    public static final String BASE_URL = "http://10.0.2.2:8080/web_home";
    /**
     * 新闻中心请求地址
     */
    public static final String NEWS_CONTENER_URL = BASE_URL + "/static/api/news/categories.json";

    /**
     * 商城请求地址
     */
    public static final String WARES_HOT_URL = "http://112.124.22.238:8081/course_api/wares/hot?";


}
