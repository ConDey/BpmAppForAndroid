package com.eazytec.bpm.lib.common.webkit;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;

/**
 * webview的通用方法，定义了webview的相关属性
 *
 * @author ConDey
 * @version Id: WebViewUtil, v 0.1 2017/5/22 下午7:39 ConDey Exp $$
 */
public class WebViewUtil {

    /**
     * 初始化WebSettings
     *
     * @param webSettings
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebSettings(WebSettings webSettings) {
        webSettings.setJavaScriptEnabled(true); // 支持JavaScript
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);   // 支持文件访问
        webSettings.setSupportZoom(true);       // 支持尺寸缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setDefaultTextEncodingName("utf-8"); //设置文本编码

        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
    }


    /**
     * 获得本地url地址全称
     *
     * @param url
     * @return
     */
    public static String getLocalHTMLUrl(String url) {
        return "file:///android_asset/html/" + url;
    }
}
