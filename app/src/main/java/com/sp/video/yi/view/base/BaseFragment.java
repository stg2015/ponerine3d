package com.sp.video.yi.view.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.SuperToast;
import com.nd.hy.android.commons.data.RestoreUtil;
import com.nd.hy.android.commons.util.Ln;
import com.nd.hy.android.commons.util.device.ResourceUtils;
import com.nd.hy.android.hermes.frame.base.AppContextUtil;
import com.nd.hy.android.hermes.frame.view.AbsRxHermesFragment;
import com.sp.video.yi.common.SchedulerFactory;
import com.sp.video.yi.data.server.retrofit1.DataLayerRetrofit1Server;
import com.sp.video.yi.data.server.retrofit2.DataLayerRetrofit2Server;
import com.sp.video.yi.inject.AppComponent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


public abstract class BaseFragment extends AbsRxHermesFragment {

    @Inject
    DataLayerRetrofit2Server mDataLayerRetrofit2;
    @Inject
    DataLayerRetrofit1Server mDataLayerRetrofit1;

    public DataLayerRetrofit2Server getRetrofit2DataLayer() {
        return mDataLayerRetrofit2;
    }

    public DataLayerRetrofit1Server getRetrofit1DataLayer() {
        return mDataLayerRetrofit1;
    }

    protected View mRootView;
    protected static final int PAGE_SIZE = 20;

    public BaseFragment() {
        AppComponent.Instance.get().inject(this);
    }


    @SuppressWarnings("unchecked")
    protected final <E extends View> E getViewWithoutButterKnife(int id) {
        try {
            return (E) mRootView.findViewById(id);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ln.d("Fragment create:[%s]", getClass().getSimpleName());
        RestoreUtil.loadState(getBundle(savedInstanceState), this);
    }

    private Bundle getBundle(Bundle savedInstanceState) {
        Bundle extras = savedInstanceState;
        if (extras == null) {
            extras = new Bundle();
            Bundle base = getActivity().getIntent().getExtras();

            if (base != null) {
                extras.putAll(base);
            }
            if (getArguments() != null) {
                extras.putAll(getArguments());
            }
        }
        return extras;
    }

    @Override
    public void onResume() {
        super.onResume();
        Ln.d("Fragment resume:[%s]", getClass().getSimpleName());
        //MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd(getClass().getSimpleName()); //统计页面
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Ln.d("Fragment attach:[%s] onAttach by [activity:%s]", getClass().getSimpleName(), getActivity());
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        mRootView = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    protected abstract int getLayoutId();

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    public void showSnackbar(CharSequence message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
    }

    public Snackbar makeSnarkbar(CharSequence message, int duration) {
        return Snackbar.make(mRootView, message, duration);
    }

    protected ActionBar getActionBar() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof AppCompatActivity) {
            return ((AppCompatActivity) activity).getSupportActionBar();
        }
        return null;
    }
    protected void showMessage(CharSequence msg, int time) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Context context = getActivity() == null ? AppContextUtil.getContext() : getActivity();
        SuperToast toast = SuperToast.create(context, msg, time);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    protected void showMessageIcon(int resId, CharSequence msg) {
        Context context = getActivity() == null ? AppContextUtil.getContext() : getActivity();
        SuperToast toast = SuperToast.create(context, msg, SuperToast.Duration.SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.getTextView().setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        toast.getTextView().setCompoundDrawablePadding(ResourceUtils.dpToPx(getActivity(), 8));
        toast.show();
    }
    protected void showMessage(CharSequence msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Context context = getActivity() == null ? AppContextUtil.getContext() : getActivity();
        SuperToast toast = SuperToast.create(context, msg, SuperToast.Duration.SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void showMessage(int stringResId){
        showMessage(getString(stringResId));
    }

    @Override
    protected <T> Observable<T> bindLifecycle(Observable<T> observable) {
        return super.bindLifecycle(observable)
                .subscribeOn(SchedulerFactory.getIoScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }
}