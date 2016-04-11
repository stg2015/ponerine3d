package com.sp.video.yi.inject;

import android.app.Service;

import javax.inject.Singleton;

import dagger.Component;


/**
 * @author yangz
 * @version 2015/6/9.
 */
@Singleton
@Component(modules = {
        DataLayerModule.class
})


public interface YiAppComponent extends AppComponent {


}