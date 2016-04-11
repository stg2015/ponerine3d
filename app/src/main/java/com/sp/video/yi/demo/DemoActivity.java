package com.sp.video.yi.demo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.video.yi.data.model.Contributor;
import com.sp.video.yi.data.model.User;
import com.sp.video.yi.view.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by Weichao Wang on 2016/4/11.
 */
public class DemoActivity extends BaseActivity {
    @Bind(R.id.tv)
    TextView     tv;
    @Bind(R.id.btn_call)
    Button       btnCall;
    @Bind(R.id.btn_obver)
    Button       btnObver;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }

    @OnClick(R.id.btn_obver)
    public void onClickObservable() {
       /* bind(getDataLayer().getApi().observableUserInfo("sp958857"))
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        tv.setText(user.getAvatarUrl() + "");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });*/
        bind(getDataLayer().getApi().observableContributors("square","retrofit"))
                .subscribe(new Action1<List<Contributor>>() {
                    @Override
                    public void call(List<Contributor> contributorList) {
                        tv.setText(contributorList.get(0).getContributions() + "");


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @OnClick(R.id.btn_call)
    public void onClickCall() {
        Call<User> call = getDataLayer().getApi().callUserInfo("sp958857");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // response.isSuccess() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    User user = response.body();
                    tv.setText(user.getAvatarUrl()+"");
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
}
