package com.sp.video.yi.view.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by magicDance on 2015/12/15.
 */
public class MWebViewClient extends WebViewClient {

    private Context context;

    public MWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (view instanceof BasicWebView.MWebView) {
            ((BasicWebView.MWebView) view).showLoadingView();
            ((BasicWebView.MWebView) view).setLoaded(true);
        }

        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (view instanceof BasicWebView.MWebView) {
            ((BasicWebView.MWebView) view).removeLoadingView();
            ((BasicWebView.MWebView) view).removeEmptyView();
        }
        super.onPageFinished(view, url);

    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (view instanceof BasicWebView.MWebView) {
//            view.loadUrl(WEB_ERROR_URL);
//            ((MWebPage.MWebView) view).showRefreshView();

        }
    }
}