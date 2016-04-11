package com.sp.video.yi.inject;



import com.sp.video.yi.data.server.DataLayer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangz
 * @version 2015/6/9.
 */
@Module
public class DataLayerModule {

    @Singleton
    @Provides
    public DataLayer provideDataLayer() {
        return new DataLayer();
    }


}