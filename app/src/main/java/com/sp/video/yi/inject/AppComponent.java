package com.sp.video.yi.inject;


import android.app.Service;
import android.support.annotation.NonNull;

import com.sp.video.yi.view.base.BaseActivity;
import com.sp.video.yi.view.base.BaseDialogFragment;
import com.sp.video.yi.view.base.BaseFragment;


/**
 *
 * 通用app 注入
 */
public interface AppComponent {

    void inject(BaseActivity activity);

    void inject(BaseFragment fragment);

    void inject(BaseDialogFragment dialogFragment);

    void inject(Service service);


    class Instance {
        private static AppComponent sComponent;

        public static void init(@NonNull AppComponent component) {
            sComponent = component;
        }

        public static AppComponent get() {
            return sComponent;
        }
    }

}