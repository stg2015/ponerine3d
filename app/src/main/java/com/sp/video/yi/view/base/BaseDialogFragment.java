package com.sp.video.yi.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.SuperToast;
import com.nd.hy.android.commons.util.Ln;
import com.nd.hy.android.hermes.frame.base.AppContextUtil;
import com.nd.hy.android.hermes.frame.view.AbsRxHermesDialogFragment;
import com.sp.video.yi.common.SchedulerFactory;
import com.sp.video.yi.data.server.DataLayer;
import com.sp.video.yi.demo.R;
import com.sp.video.yi.inject.AppComponent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


/**
 * 默认全屏     setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Transparent_full_screen);
 *
 * 需要使用对话框请在onCreate中  setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDlg);
 */

public abstract class BaseDialogFragment extends AbsRxHermesDialogFragment {

    @Inject
    DataLayer mDataLayer;

    protected boolean mTablet;

    protected View mRootView;

    public BaseDialogFragment() {
        AppComponent.Instance.get().inject(this);
    }

    public DataLayer getDataLayer() {
        return mDataLayer;
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

        setStyle(DialogFragment.STYLE_NORMAL, R.style.SpDialogTransparentFullScreen);
        super.onCreate(savedInstanceState);
        Ln.d("DialogFragment create:[%s]", getClass().getSimpleName());

    }

    @Override
    public void onResume() {
        super.onResume();
        Ln.d("DialogFragment resume:[%s]", getClass().getSimpleName());
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
        Ln.d("DialogFragment attach:[%s] onAttach by [activity:%s]", getClass().getSimpleName(), getActivity());
    }

    @Override
    protected int getAnimStyle() {
        return R.style.SpDialogAnimFromBottom;
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        mRootView = layoutInflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this,mRootView);
        return mRootView;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    protected abstract int getLayoutId();

    protected void showMessage(CharSequence msg) {
        Context context = getActivity() == null ? AppContextUtil.getContext() : getActivity();
        SuperToast toast = SuperToast.create(context, msg, SuperToast.Duration.SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected <T> Observable<T> bindLifecycle(Observable<T> observable) {
        return super.bindLifecycle(observable)
                .subscribeOn(SchedulerFactory.getIoScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected OnDialogDismissListener onDialogDismissListener;
    public void setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDialogDismissListener != null) {
            onDialogDismissListener.onDismiss(dialog);
        }
    }

    public interface OnDialogDismissListener {
        void onDismiss(DialogInterface dialog);
    }
}