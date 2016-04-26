package com.sp.video.yi;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.nd.hy.android.hermes.frame.BaseHermesApp;
import com.sp.video.yi.data.tcp.TelnetClient;
import com.sp.video.yi.inject.AppComponent;
import com.sp.video.yi.inject.DaggerYiAppComponent;

/**
 * Created by Weichao Wang on 2016/4/11.
 */
public class YiApplication extends BaseHermesApp{

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //优先初始化,阻塞式
        AppComponent.Instance.init(
                DaggerYiAppComponent.builder()
                        .build()
        );
    }

    @Override
    protected void afterCreate() {
        TelnetClient.INSTANCE.init();

    }

    @Override
    protected void beforeExit() {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//必须在后面
    }
}
