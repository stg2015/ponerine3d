package com.sp.video.yi.inject.provide;



import com.sp.video.yi.data.server.retrofit1.DataLayerRetrofit1Server;
import com.sp.video.yi.data.tcp.TelnetClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangz
 * @version 2015/6/9.
 */
@Module
public class DataLayerTcpModule {

    @Singleton
    @Provides
    public TelnetClient provideTelnetClient() {
        return new TelnetClient();
    }


}