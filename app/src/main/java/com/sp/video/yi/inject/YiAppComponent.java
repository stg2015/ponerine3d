package com.sp.video.yi.inject;

import com.sp.video.yi.inject.provide.DataLayerRetrofit1Module;
import com.sp.video.yi.inject.provide.DataLayerRetrofit2Module;
import com.sp.video.yi.inject.provide.DataLayerTcpModule;

import javax.inject.Singleton;

import dagger.Component;


/**
 * @author yangz
 * @version 2015/6/9.
 */
@Singleton
@Component(modules = {
        DataLayerRetrofit1Module.class,
        DataLayerRetrofit2Module.class,
        DataLayerTcpModule.class
})

public interface YiAppComponent extends AppComponent {

}