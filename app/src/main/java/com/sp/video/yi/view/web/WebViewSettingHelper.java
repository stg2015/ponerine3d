package com.sp.video.yi.view.web;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * WebView 工具类
 * Created by Weichao Wang on 2015/10/30.
 */
public class WebViewSettingHelper {
    private WebSettings mSettings;
    private WebView mWebView;

    public WebViewSettingHelper(WebView webview) {
        this.mWebView = webview;
        this.mSettings = webview != null ? webview.getSettings() : null;
    }

    public WebViewSettingHelper invokeSetting(WebViewSettingStrategy options) {
        if (mSettings == null) {
            return this;
        }
        switch (options) {
            case NO_ZOOM_USE_BROWSER:
                mSettings.setBuiltInZoomControls(false);//取消缩放
                mSettings.setUseWideViewPort(false);//Sets whether the WebView should enable support for the "viewport" HTML meta tag or should use a wide viewport.
                mSettings.setLoadWithOverviewMode(true);// Gets whether this WebView loads pages in overview mode.
                mSettings.setJavaScriptEnabled(true);

                break;
            case ZOOM_USE_BROWSER:
                mSettings.setBuiltInZoomControls(true);
                mSettings.setUseWideViewPort(true);
                mSettings.setJavaScriptEnabled(true);

                break;
            default:
                break;

        }
        return this;
    }

    public enum WebViewSettingStrategy {
        NO_ZOOM_USE_BROWSER,
        ZOOM_USE_BROWSER;
    }
    public static final String RAW_URI_TEST = "http://3g.163.com/touch/";
//    public final static String CSS_STYLE ="<style>* {font-size:16px;line-height:25px;color:#777;}</style>";
}