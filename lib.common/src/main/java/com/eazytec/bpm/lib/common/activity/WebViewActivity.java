package com.eazytec.bpm.lib.common.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.eazytec.bpm.lib.common.webkit.JsWebView;
import com.eazytec.bpm.lib.common.webkit.WebViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * BPMAPP自带的web插件载体，因为small的webview插件不好扩展不满足需求，所以另外定义了一个webviewActivity
 * <p>
 * 使用了jswebview替代了webkit.webview
 *
 * @author ConDey
 * @version Id: WebViewActivity, v 0.1 2017/7/2 下午9:33 ConDey Exp $$
 */
public abstract class WebViewActivity extends CommonActivity {

    /**
     * jswebview, jswebview支持native和js的互相调用
     */
    protected JsWebView jsWebView;

    /**
     * 进度条提示控件，可以为空
     */
    protected ProgressBar progressBar;


    @Override @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化webview
     * <p>
     * 需要显式的被调用，由子类决定何时初始化webview
     */
    protected void initWebView() {
        jsWebView = jsWebView();
        progressBar = progressBar();

        WebViewUtil.initWebSettings(jsWebView.getSettings());
        initWebViewClient();
        initWebChromeClient();

        jsWebView.setJavascriptInterface(jsApi()); // 设置jsApi
        jsWebView.clearCache(true);

        CookieManager cookieManager = CookieManager.getInstance();
        syncCookie(cookieManager);  // 写入自己的Cookie
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
            cookieSyncManager.sync();
        }


        jsWebView.loadUrl(url(), headers());
    }


    /**
     * 获取webview对象
     *
     * @return
     */
    protected abstract JsWebView jsWebView();


    /**
     * 获取进度条控件，如果没有可以设置为null
     *
     * @return
     */
    protected abstract ProgressBar progressBar();

    /**
     * 获取URL
     *
     * @return
     */
    protected abstract String url();

    /**
     * 请求头内容
     *
     * @return
     */
    protected HashMap<String, String> headers() {
        return new HashMap<>();
    }


    /**
     * 同步cookie
     */
    public void syncCookie(CookieManager cookieManager) {

    }


    /**
     * 获取jsApi
     *
     * @return
     */
    protected abstract Object jsApi();

    /**
     * 绑定后退键
     *
     * @param keyCode 键盘keyCode
     * @param event   键盘事件
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (this.jsWebView != null && this.jsWebView.canGoBack()) {
                this.jsWebView.goBack();
            } else {
                finish();
            }
        }
        return false;
    }


    protected void initWebViewClient() {
        jsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                jsWebView.loadUrl(request.toString());
                return true;
            }

            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (progressBar != null)
                    progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
                // 避免在此执行业务逻辑,因为可能会执行多次

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });
    }

    protected void initWebChromeClient() {
        jsWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, final JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (progressBar != null)
                    progressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

}
