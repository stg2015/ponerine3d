package com.sp.video.yi.demo;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    public static String HOST = "192.168.170.101";
    public static int    PORT = 7878;
    XiaoYiCameraConnection testCamera;
    Channel channel;

    @Bind(R.id.txt_ip_address)
    EditText txtIpAddress;
    @Bind(R.id.btn_retry)
    Button btnRetry;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yi_test;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wwc", "Thread: afterCreate id = " + Thread.currentThread().getId());
        testCamera = new XiaoYiCameraConnection(getHOST_ip_address(),PORT);
        sendMsg();
    }

    private String getHOST_ip_address() {
        String ip_address = txtIpAddress.getText().toString().trim();
        if (!ip_address.equals(""))
            return ip_address;
        else
            return HOST;
    }


    public void sendMsg() {
        bind(Observable.defer(new Func0<Observable<Object>>() {
            @Override
            public Observable<Object> call() {
                try {
                    channel  = getTelnetClient().connectChannel(testCamera,false);
                    getTelnetClient().sendMsg(channel, testCamera.getAccessTokenMsg());
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
                getTelnetClient().closeAllChannel();
                Log.d("wwc", "Thread: afterCreate id = " + Thread.currentThread().getId());
                testCamera = new XiaoYiCameraConnection(getHOST_ip_address(),PORT);
                sendMsg();
                break;
            case R.id.btn_close:
                getTelnetClient().closeAllChannel();
                break;
        }

    }
}
