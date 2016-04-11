package com.sp.video.yi.data.server;

import com.sp.video.yi.data.model.Contributor;
import com.sp.video.yi.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Weichao Wang on 2016/4/11.
 */
public interface ServerApi {
    @GET("users/{user}")
    Call<User> callUserInfo(@Path("user") String user);

    @GET("users/{user}")
    Observable<User> observableUserInfo(@Path("user") String user);

    @GET("repos/{owner}/{repo}/contributors}")
    Observable<List<Contributor>> observableContributors(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}
