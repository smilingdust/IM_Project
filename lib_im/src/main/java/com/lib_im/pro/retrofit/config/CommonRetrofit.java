package com.lib_im.pro.retrofit.config;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求工具类
 */

public class CommonRetrofit implements RetrofitClient {

    public static  int TIMEOUT = 0;
    public static  String BASE_URL="";
    public Map<String,String> paramsMap;
    public Retrofit retrofit;
    public OkHttpClient okHttpClient;
    public static CommonRetrofit commonRetrofit;

    public CommonRetrofit(int timeOut,String url,Map<String,String> map) {
         TIMEOUT=timeOut;
         BASE_URL=url;
         paramsMap=map;
         okHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new HeadInterceptor(map))
                .addInterceptor(new LoggingInterceptor())
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }
    @Override
    public Retrofit getRetrofit() {
        if (commonRetrofit == null) {
            synchronized (CommonRetrofit.class) {
                if (commonRetrofit == null) {
                    commonRetrofit = new CommonRetrofit(TIMEOUT,BASE_URL,paramsMap);
                }
            }
        }
        return commonRetrofit.retrofit;
    }

    public  OkHttpClient getHttpClient() {
        if (commonRetrofit == null) {
            synchronized (CommonRetrofit.class) {
                if (commonRetrofit == null) {
                    commonRetrofit = new CommonRetrofit(TIMEOUT,BASE_URL,paramsMap);
                }
            }
        }
        return commonRetrofit.okHttpClient;
    }
}
