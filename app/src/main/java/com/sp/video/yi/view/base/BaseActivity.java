package com.sp.video.yi.view.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperToast;
import com.nd.hy.android.commons.util.Ln;
import com.nd.hy.android.hermes.frame.view.AbsRxCompatActivity;
import com.sp.video.yi.common.SchedulerFactory;
import com.sp.video.yi.data.server.retrofit1.DataLayerRetrofit1Server;
import com.sp.video.yi.data.server.retrofit2.DataLayerRetrofit2Server;
import com.sp.video.yi.data.tcp.TelnetClient;
import com.sp.video.yi.inject.AppComponent;


import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public abstract class BaseActivity extends AbsRxCompatActivity {


    protected View mRootView;

    public BaseActivity() {
        AppComponent.Instance.get().inject(this);
    }

    @Inject
    DataLayerRetrofit2Server mDataLayerRetrofit2;
    @Inject
    DataLayerRetrofit1Server mDataLayerRetrofit1;
    @Inject
    TelnetClient             mTelnetClient;


    public TelnetClient getTelnetClient() {
        return mTelnetClient;
    }

    public DataLayerRetrofit2Server getRetrofit2DataLayer() {
        return mDataLayerRetrofit2;
    }

    public DataLayerRetrofit1Server getRetrofit1DataLayer() {
        return mDataLayerRetrofit1;
    }

    @SuppressWarnings("unchecked")
    protected final <E extends View> E getViewWithoutButterKnife(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e("GET_VIEW", "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }

    protected final <E extends View> E getViewWithoutButterKnife(View convertView, int id) {
        try {
            return (E) convertView.findViewById(id);
        } catch (ClassCastException ex) {
            Log.e("GET_VIEW", "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }

    @Override
    protected void onBaseCreate(Bundle state) {
        setContentView(getLayoutId());
        mRootView = findViewById(android.R.id.content);
        ButterKnife.bind(this,mRootView);
        Ln.d("Activity create:[%s]", getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);//统计分析
        Ln.d("Activity resume:[%s]", getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);//统计分析
    }

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void showSnackbar(CharSequence message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
    }

    public Snackbar makeSnarkbar(CharSequence message, int duration) {
        return Snackbar.make(mRootView, message, duration);
    }

    protected void showMessage(int resMsg){
        showMessage(getString(resMsg));
    }

    protected void showMessage(CharSequence msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        SuperToast toast = SuperToast.create(this, msg, SuperToast.Duration.SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    @Override
    protected <T> Observable<T> bind(Observable<T> observable) {
        return super.bind(observable)
                .subscribeOn(SchedulerFactory.getIoScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }

}