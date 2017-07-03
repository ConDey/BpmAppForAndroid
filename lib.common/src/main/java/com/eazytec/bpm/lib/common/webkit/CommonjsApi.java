package com.eazytec.bpm.lib.common.webkit;

import android.webkit.JavascriptInterface;

import com.eazytec.bpm.lib.common.activity.WebViewActivity;

import org.json.JSONObject;

/**
 * 通用的JsApi, 所有插件如果要扩展JsApi必须继承此类
 *
 * @author ConDey
 * @version Id: CommonjsApi, v 0.1 2017/7/3 下午1:40 ConDey Exp $$
 */
public class CommonjsApi {

    protected WebViewActivity activity;

    /**
     * 必须注入Activity
     *
     * @param activity
     */
    public CommonjsApi(WebViewActivity activity) {
        this.activity = activity;
    }


    /**
     * 关闭此页面
     */
    @JavascriptInterface
    public void close(JSONObject jsonObject) {
        if (activity != null) {
            activity.finish();
        }
    }

}
