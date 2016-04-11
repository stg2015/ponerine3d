package com.sp.video.yi.view.web;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sp.video.yi.demo.R;


/**
 * Created by magicDance on 2015/12/15.
 */
public class BasicWebView extends LinearLayout {
    private View contentView;
    public MWebView mWebView;
    private int mHeight;
    private View lodingView;
    private boolean isNeedLoadview = true;

    public BasicWebView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public BasicWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    protected void initView() {
        contentView = createContentView();
        LayoutParams linerLayoutParams;
        if (mHeight > 0) {
            linerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, mHeight);
        } else {
            linerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        addView(contentView, linerLayoutParams);
    }

    /**
     * 初始化操作
     *
     * @return
     */
    protected View createContentView() {
        removeAllViews();
        mWebView = new MWebView(getContext());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        MWebViewClient webViewClient = new MWebViewClient(getContext());
        mWebView.setWebViewClient(webViewClient);

        // TODO: 2015/12/16 webView缓存设计

        return mWebView;
    }


    protected View getLoadingView() {
        lodingView = new CircleAnimsLoadingView(getContext());
        return lodingView;
    }


    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.sp_include_empty_view, null);
    }

    /**
     * 设置空页面文字
     *
     * @param tip
     */
    public void setTip(String tip) {
        if (!TextUtils.isEmpty(tip) && mWebView.emptyView != null) {
            mWebView.textView.setText(tip);
        }
    }

    /**
     * 设置空页面图片
     *
     * @param drawId
     */
    public void setDrawbleViewId(int drawId) {
        if (drawId > 0 && mWebView.emptyView != null) {
            mWebView.textView.setCompoundDrawablesWithIntrinsicBounds(0, drawId, 0, 0);
        }
    }

    /**
     * 真正加载webview(自定义高度)
     *
     * @param content 内容
     * @param Height  高度
     * @param flag    是否加载状态视图
     */
    public void loadWebView(String content, int Height, boolean flag) {
        this.mHeight = Height;
        this.isNeedLoadview = flag;
        if (mWebView == null) {
            initView();
        }
        if (!mWebView.isLoaded()) {
            if (!TextUtils.isEmpty(content)) {
                mWebView.loadData(content, "text/html; charset=UTF-8", "utf-8");

            } else if (flag) {
                mWebView.showEmptyView();
            }
        }
    }

    /**
     * 真正加载webview
     *
     * @param content 内容
     * @param flag    是否加载状态视图
     */
    public void loadWebView(String content, boolean flag) {
        this.isNeedLoadview = flag;
        if (mWebView == null) {
            initView();
        }
        if (!mWebView.isLoaded()) {
            if (!TextUtils.isEmpty(content)) {
                mWebView.loadData(content, "text/html; charset=UTF-8", "utf-8");

            } else if (flag) {
                mWebView.showEmptyView();
            }
        }

    }


    /**
     * 本工程WebView的基类
     */
    public class MWebView extends WebView {
        private View loadingView;
        public View emptyView;
        public boolean isLoaded = false;

        public TextView textView;

        public MWebView(Context context) {
            super(context);
        }

        private void initLoadingView() {
            loadingView = getLoadingView();
            loadingView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(loadingView, params);
        }

        private void initEmptyView() {
            emptyView = getEmptyView();
            emptyView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(emptyView, params);
            textView = (TextView) emptyView.findViewById(R.id.tv_empty);
        }

        public void showLoadingView() {
            if (loadingView == null && isNeedLoadview) {
                initLoadingView();
                loadingView.setVisibility(View.VISIBLE);
            }
        }

        public void removeLoadingView() {
            if (loadingView != null) {
                removeView(loadingView);
                loadingView = null;
            }
        }

        public void showEmptyView() {
            if (emptyView == null) {
                initEmptyView();
            }
            emptyView.setVisibility(View.VISIBLE);
        }


        public boolean isLoaded() {
            return isLoaded;
        }

        public void setLoaded(boolean isLoaded) {
            this.isLoaded = isLoaded;
        }

        public void removeEmptyView() {
            if (emptyView != null) {
                removeView(emptyView);
                emptyView = null;
            }
        }

        public void removeRefreshView() {

        }

        public void showRefreshView() {
            //TODO:加载失败的时候调用，考虑目前仅支持文本形式，遂先不实现
        }
    }
}