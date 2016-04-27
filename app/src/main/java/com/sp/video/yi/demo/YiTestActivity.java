package com.sp.video.yi.demo;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import com.sp.video.yi.view.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by Weichao Wang on 2016/4/27.
 */
public class YiTestActivity extends BaseActivity {
    public static String HOST = "192.168.31.11";
    public static int    PORT = 7878;
    @Bind(R.id.btn_retry)
    Button btnRetry;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yi_test;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        Log.d("wwc", "Thread: afterCreate id = " + Thread.currentThread().getId());
        sendMsg();

    }

    public void sendMsg(){
        bind(Observable.defer(new Func0<Observable<Object>>() {
            @Override
            public Observable<Object> call() {
                try {
                    Channel channel = getTelnetClient().getChannel(HOST, PORT);
                    getTelnetClient().sendMsg(channel, "{\"msg_id\":257,\"param\":0,\"token\":0,\"heartbeat\":1}", new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            Log.d("wwc", "Thread: operationComplete id = " + Thread.currentThread().getId());
                            Looper.prepare();
                            if (!future.isSuccess()) {
                                future.cause().printStackTrace();
                                Log.d("wwc", "发送不成功");
                            } else {
                                showMessage("发送成功");
                                Log.d("wwc", "发送成功");
                            }
                        }
                    });
                } catch (Exception e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
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

            }
        });
    }

    @OnClick(R.id.btn_retry)
    public void onClick() {
        sendMsg();
    }
}
