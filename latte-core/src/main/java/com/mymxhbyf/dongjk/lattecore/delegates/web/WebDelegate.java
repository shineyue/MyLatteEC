package com.mymxhbyf.dongjk.lattecore.delegates.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.mymxhbyf.dongjk.lattecore.delegates.LatteDelegate;
import com.mymxhbyf.dongjk.lattecore.delegates.web.route.RoteKeys;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * Created by DongJK on 2018/2/5.
 */

public abstract class WebDelegate extends LatteDelegate implements IWebViewInitializer{

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String mUrl = null;
    private boolean mIsWebViewAvailable = false;
    private LatteDelegate mTopDelegate = null;

    public WebDelegate(){
    }

    public abstract IWebViewInitializer setInitializer();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        mUrl = args.getString(RoteKeys.URL.name());
        initWebView();
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView(){
        if (mWebView != null){
            mWebView.removeAllViews();
            mWebView.destroy();
        }else {
            final IWebViewInitializer initializer = setInitializer();
            if (initializer != null){
                final WeakReference<WebView> webViewWeakReference = new WeakReference<>(new WebView(getContext()),WEB_VIEW_QUEUE);
                mWebView = webViewWeakReference.get();
                mWebView = initializer.initWebView(mWebView);
                mWebView.setWebViewClient(initializer.initWebViewClient());
                mWebView.setWebChromeClient(initializer.initWebChromeClent());
                mWebView.addJavascriptInterface(LatteWebInterface.creat(this),"latte");
                mIsWebViewAvailable = true;
            }else {
                throw new NullPointerException("Initialize is null");
            }
        }
    }

    public void setTopDelegate(LatteDelegate delegate){
        mTopDelegate = delegate;
    }

    public LatteDelegate getTopDelegate(){
        if (mTopDelegate == null){
            mTopDelegate = this;
        }
        return mTopDelegate;
    }

    public WebView getWebView(){
        if (mWebView == null){
            throw new NullPointerException("WebView is null");
        }
        return mIsWebViewAvailable ? mWebView : null;
    }

    public String getUrl(){
        if (mUrl == null){
            throw new NullPointerException("WebView is null");
        }
        return mUrl;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null){
            mWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null){
            mWebView.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsWebViewAvailable = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null){
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }
}
