package com.sp.video.yi.view.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.hy.android.commons.util.Ln;
import com.sp.video.yi.constant.BundleKey;
import com.sp.video.yi.demo.R;


/**
 * Created by Administrator on 2015/9/10.
 */
public class BaseWebviewFragment extends BaseFragment implements View.OnClickListener {

    private final static int FILECHOOSER_RESULTCODE = 1;


    ImageButton mIvHeaderLeft;

    ProgressBar mLaodProgress;

    TextView mTvTittle;

    RelativeLayout mRlExamHeader;

    WebView mWebview;

    private ValueCallback<Uri> mUploadMessage;

    private String title = "";
    private String url   = "";

    private boolean isLoaded;

    public static BaseWebviewFragment newInstance() {
        BaseWebviewFragment fragment = new BaseWebviewFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sp_frg_base_webview;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        mIvHeaderLeft = getViewWithoutButterKnife(R.id.iv_header_left);
        mLaodProgress = getViewWithoutButterKnife(R.id.laod_progress);
        mTvTittle = getViewWithoutButterKnife(R.id.tv_tittle);
        mRlExamHeader = getViewWithoutButterKnife(R.id.rl_exam_header);
        mWebview = getViewWithoutButterKnife(R.id.fg_base_webview_webview);
        title = getActivity().getIntent().getExtras().getString(BundleKey.KEY_TITLE);
        url = getActivity().getIntent().getExtras().getString(BundleKey.KEY_URL);
        initHeader();
        initContent();
    }

    public void initHeader() {
        mTvTittle.setText(title);
        mIvHeaderLeft.setOnClickListener(this);
        mLaodProgress.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_header_left) {
            if (mWebview.canGoBack()) {
                mWebview.goBack();
            } else {
                getActivity().finish();
            }

        }

    }

    private void initContent() {

        WebSettings settings = mWebview.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受证书
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mLaodProgress != null) {
                    mLaodProgress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mLaodProgress != null) {
                    mLaodProgress.setVisibility(View.GONE);
                }
            }
        });
        mWebview.setWebChromeClient(new WebChromeClient() {


            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                BaseWebviewFragment.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                BaseWebviewFragment.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                BaseWebviewFragment.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoaded) {
            Ln.d("loadWeb:[%s]", url.toLowerCase());
            mWebview.loadUrl(url.toLowerCase());
            isLoaded = true;
        }
    }
}