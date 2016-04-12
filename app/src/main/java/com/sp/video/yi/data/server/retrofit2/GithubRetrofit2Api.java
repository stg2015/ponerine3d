package com.sp.video.yi.data.server.retrofit2;

import com.sp.video.yi.data.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Weichao Wang on 2016/4/11.
 */
public interface GithubRetrofit2Api {
    @GET("users/{user}")
    Call<User> callUserInfo(@Path("user") String user);

    @GET("users/{user}")
    Observable<User> observableUserInfo(@Path("user") String user);


}
