package com.sp.video.yi;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.sp.video.yi.inject.AppComponent;
import com.sp.video.yi.inject.DaggerYiAppComponent;

/**
 * Created by Administrator on 2016/5/8.
 */
public class YiApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//必须在后面
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //优先初始化,阻塞式
        AppComponent.Instance.init(
                DaggerYiAppComponent.builder()
                        .build()
        );
    }
}
