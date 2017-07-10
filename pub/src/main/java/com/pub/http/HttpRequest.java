package com.pub.http;


import com.pub.base.BaseApp;
import com.pub.utils.ConfigUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhkj on 16/3/11.
 */
public class HttpRequest {

    private static Retrofit retrofitInstance;

    public static Retrofit GetRetrofitInstance(String server) {
        if (retrofitInstance == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BaseApp.DEBUG) {//DEBUG模式打印日志
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {//非DEBUG模式不打印日志
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .build();

            retrofitInstance = new Retrofit.Builder().baseUrl(server).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofitInstance;
    }

    private static Retrofit retrofitInstanceNetAction;

    public static Retrofit GetRetrofitInstanceNetAction(String server) {
        if (retrofitInstanceNetAction == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BaseApp.DEBUG) {//DEBUG模式打印日志
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {//非DEBUG模式不打印日志
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .build();

            retrofitInstanceNetAction = new Retrofit.Builder().baseUrl(server).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofitInstanceNetAction;
    }

    private static IResourceOA newsIResource;

    public static IResourceOA getIResourceOA() {
        if (newsIResource == null) {
            String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
            newsIResource = GetRetrofitInstance(server).create(IResourceOA.class);
        }
        return newsIResource;
    }

    private static IResourceOA newsIResourceNetAction;

    public static IResourceOA getIResourceOANetAction() {
        if (newsIResourceNetAction == null) {
            String server = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer_ACTION);
            newsIResourceNetAction = GetRetrofitInstanceNetAction(server).create(IResourceOA.class);
        }
        return newsIResourceNetAction;
    }
}
