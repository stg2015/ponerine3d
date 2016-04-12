package com.sp.video.yi.inject.provide;



import com.sp.video.yi.data.server.retrofit2.DataLayerRetrofit2Server;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangz
 * @version 2015/6/9.
 */
@Module
public class DataLayerRetrofit2Module {

    @Singleton
    @Provides
    public DataLayerRetrofit2Server provideDataLayer() {
        return new DataLayerRetrofit2Server();
    }


}