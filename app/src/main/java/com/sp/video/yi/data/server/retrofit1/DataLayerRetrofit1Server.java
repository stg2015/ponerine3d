package com.sp.video.yi.data.server.retrofit1;

import android.util.Log;

import com.jakewharton.retrofit.Ok3Client;
import com.nd.hy.android.commons.data.ObjectMapperUtils;
import com.sp.video.yi.data.server.DataLayer;

import okhttp3.OkHttpClient;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Weichao Wang on 2016/4/12.
 */
public class DataLayerRetrofit1Server implements DataLayer<GithubRetrofit1Api> {
    private static Class CLS_BUILDER;
    private static RequestInterceptor interceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {

        }
    };
    private static GithubRetrofit1Api api;
    public static  OkHttpClient       client;
    private static final String BASE_URL = "https://api.github.com";

    public DataLayerRetrofit1Server() {
        client = new OkHttpClient();
        api = genApi();
    }

    @Override
    public GithubRetrofit1Api genApi() {
        try {
            CLS_BUILDER = Class.forName("retrofit.RequestBuilder");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (null == api) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setClient(new Ok3Client(client))
                    .setEndpoint(BASE_URL)
                    .setLog(new Logger())
                    .setConverter(new JsonConverter(ObjectMapperUtils.getMapperInstance()))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setRequestInterceptor(interceptor)
                    .setErrorHandler(new BasicErrorHandler())
                    .build();

            api = adapter.create(GithubRetrofit1Api.class);
        }
        return api;
    }

    @Override
    public GithubRetrofit1Api getApi() {
        if (null == api) {
            genApi();
        }
        return api;
    }

    private static class Logger implements RestAdapter.Log {
        @Override
        public void log(String message) {
            Log.d("spYiApp", message);
        }
    }
}
