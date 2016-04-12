package com.sp.video.yi.inject.provide;



import com.sp.video.yi.data.server.retrofit1.DataLayerRetrofit1Server;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangz
 * @version 2015/6/9.
 */
@Module
public class DataLayerRetrofit1Module {

    @Singleton
    @Provides
    public DataLayerRetrofit1Server provideDataLayer() {
        return new DataLayerRetrofit1Server();
    }


}