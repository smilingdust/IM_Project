package com.lib_im.pro.retrofit.config;

import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加网络请求固定参数
 */
public class HeadInterceptor implements Interceptor {

    private Map<String, String> map;//存储固定参数map

    public HeadInterceptor(Map<String, String> map) {
        this.map = map;
    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request firstRequest = chain.request();
        //设置网络请求固定参数
        HttpUrl.Builder builder = firstRequest
                .url()
                .newBuilder();
        if (map != null && map.size() > 0) {
            for (String key : map.keySet()) {
                String value = map.get(key);
                builder.addQueryParameter(key, value);
            }
        }
        //设置网络请求头参数
        HttpUrl url = builder.build();
        Request request = firstRequest.newBuilder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("Accept", "application/json")
                .build();
        return chain.proceed(request);
    }


}
