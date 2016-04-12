package com.sp.video.yi.data.server.retrofit1;

import com.sp.video.yi.data.model.User;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Weichao Wang on 2016/4/11.
 */
public interface GithubRetrofit1Api{
    @GET("/users/{user}")
    void asyncUserInfo(@Path("user") String user, Callback<User> cb);

    @GET("/users/{user}")
    Observable<User> observableUserInfo(@Path("user") String user);


}
