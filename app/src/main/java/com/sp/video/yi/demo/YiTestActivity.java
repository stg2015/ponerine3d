package com.sp.video.yi.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sp.video.yi.data.model.connection.XiaoYiCameraConnection;
import com.sp.video.yi.view.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;
import io.netty.channel.Channel;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by Weichao Wang on 2016/4/27.
 */
public class YiTestActivity extends BaseActivity {
    public static String HOST = "192.168.31.15";
    public static int    PORT = 7878;
    XiaoYiCameraConnection testCameta;
    Channel channel;


    @Bind(R.id.btn_retry)
    Button btnRetry;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yi_test;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        Log.d("wwc", "Thread: afterCreate id = " + Thread.currentThread().getId());

        testCameta = new XiaoYiCameraConnection(HOST,PORT);
        sendMsg();

    }
    public void sendMsg() {
        bind(Observable.defer(new Func0<Observable<Object>>() {
            @Override
            public Observable<Object> call() {
                try {
                    channel  = getTelnetClient().connectChannel(testCameta);
                    getTelnetClient().sendMsg(channel, testCameta.getAccessTokenMsg());
                } catch (Exception e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                return null;
            }
        })).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                showMessage(throwable.getMessage());
            }
        });
    }

    @OnClick({R.id.btn_retry, R.id.btn_close})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_retry:
                sendMsg();
                break;
            case R.id.btn_close:
                getTelnetClient().closeAllChannel();
                break;
        }

    }
}
