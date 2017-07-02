package com.eazytec.bpm.lib.common.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.widget.ProgressBar;

import com.eazytec.bpm.lib.common.webkit.JsWebView;
import com.eazytec.bpm.lib.common.webkit.WebViewUtil;

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

    /**
     * 用于存放webview的回调函数，当存在回调函数的时候，activityresult才会去执行callback
     * <p>
     * 需要注意的是callback一定是js定义的函数，不然会报错。
     */
    protected Map<Integer, String> callbacks = new HashMap<>();

    @Override @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.jsWebView = getJsWebView();
        this.progressBar = getProgressBar();

        WebViewUtil.initWebSettings(jsWebView.getSettings());

        this.jsWebView.clearCache(true);
        this.jsWebView.loadUrl(getUrl());

    }


    /**
     * 获取webview对象
     *
     * @return
     */
    protected abstract JsWebView getJsWebView();


    /**
     * 获取进度条控件，如果没有可以设置为null
     *
     * @return
     */
    protected abstract ProgressBar getProgressBar();

    /**
     * 获取URL
     *
     * @return
     */
    protected abstract String getUrl();

}
