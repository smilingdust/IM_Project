package com.lib_im.pro.retrofit.config;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by songgx on 2017/9/26.
 * 网络请求接口
 */

public interface RetrofitClient {

    /**
     * 获取网络请求对象retrofit
     * @return
     */
    Retrofit getRetrofit();

    /**
     * 获取OkHttpClient对象
     * @return
     */
    OkHttpClient getHttpClient();
}
