package com.sp.video.yi.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.video.yi.data.model.User;
import com.sp.video.yi.view.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by Weichao Wang on 2016/4/11.
 */
public class RetrofitTestActivity extends BaseActivity {

    private static final String NAME = "sp958857";

    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.btn_retrofit1_async)
    Button btnRetrofit1Async;
    @Bind(R.id.btn_retrofit1_obserable)
    Button btnRetrofit1Obserable;
    @Bind(R.id.btn_retrofit2_call)
    Button btnRetrofit2Call;
    @Bind(R.id.btn_retrofit2_obserable)
    Button btnRetrofit2Obserable;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_retrofit_test;
    }



    @Override
    protected void afterCreate(Bundle bundle) {

    }

    @OnClick({R.id.btn_retrofit1_async, R.id.btn_retrofit1_obserable, R.id.btn_retrofit2_call, R.id.btn_retrofit2_obserable})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_retrofit1_async:
                onClickRetrofit1Async();
                break;
            case R.id.btn_retrofit1_obserable:
                onClickRetrofit1Obser();
                break;
            case R.id.btn_retrofit2_call:
                onClickRetrofit2Call();
                break;
            case R.id.btn_retrofit2_obserable:
                onClickRetrofit2Obserable();
                break;
        }
    }

    private void onClickRetrofit2Obserable() {
        bind(getRetrofit2DataLayer().getApi().observableUserInfo(NAME))
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        tv.setText("User login: " + user.getLogin() + " onClickRetrofit2Obserable");

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void onClickRetrofit2Call() {
        getRetrofit2DataLayer().getApi().callUserInfo(NAME)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        // response.isSuccess() is true if the response code is 2xx
                        if (response.isSuccessful()) {
                            User user = response.body();
                            tv.setText("User login: " + user.getLogin() + " onClickRetrofit2Call");

                        } else {
                            int statusCode = response.code();
                            // handle request errors yourself
                            ResponseBody errorBody = response.errorBody();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable throwable) {
                        // handle execution failures like no internet connectivity
                        throwable.printStackTrace();
                    }
                });
    }

    private void onClickRetrofit1Obser() {
        bind(getRetrofit1DataLayer().getApi().observableUserInfo(NAME))
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        tv.setText("User login: " + user.getLogin() + " onClickRetrofit1Obser");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void onClickRetrofit1Async() {
        getRetrofit1DataLayer().getApi().asyncUserInfo(NAME, new retrofit.Callback<User>() {
            @Override
            public void success(User user, retrofit.client.Response response) {
                tv.setText("User login: " + user.getLogin() + " onClickRetrofit1Async");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(NAME, error.getMessage());
            }
        });
    }
}
