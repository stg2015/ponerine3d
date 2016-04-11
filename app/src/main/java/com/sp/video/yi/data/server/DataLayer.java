package com.sp.video.yi.data.server;


import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Weichao Wang on 2016/4/11.
 */
public class DataLayer {
    private static ServerApi api;
    public static OkHttpClient client;

    private static final String BASE_URL = "https://api.github.com/";

    public DataLayer(){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization", "auth-token")
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);

                // Customize or return the response
                return response;
            }
        };
        client = new OkHttpClient().newBuilder().addInterceptor(interceptor).build();
        api =  genApi();
    }

    private ServerApi genApi() {
        if (null == api) {
            Retrofit adapter = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            api = adapter.create(ServerApi.class);
        }
        return api;
    }

    public ServerApi getApi() {
        if (null == api) {
            genApi();
        }
        return api;
    }


}
