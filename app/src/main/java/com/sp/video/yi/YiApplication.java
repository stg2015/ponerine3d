package com.sp.video.yi;

import com.nd.hy.android.hermes.frame.BaseHermesApp;
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

    }

    @Override
    protected void beforeExit() {

    }
}