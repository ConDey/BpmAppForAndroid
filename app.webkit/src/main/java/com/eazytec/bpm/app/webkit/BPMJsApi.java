package com.eazytec.bpm.app.webkit;

import android.webkit.JavascriptInterface;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * 通用的JsApi, 所有插件如果要扩展JsApi必须继承此类
 *
 * @author ConDey
 * @version Id: BPMJsApi, v 0.1 2017/7/3 下午1:40 ConDey Exp $$
 */
public class BPMJsApi {

    private BPMWebViewActivity activity;

    /**
     * 必须注入Activity
     *
     * @param activity
     */
    public BPMJsApi(BPMWebViewActivity activity) {
        this.activity = activity;
    }

    /**
     * 标题，jswebview参数，用于设置BPMWebViewActivity.toolbar的标题
     */
    public static final String API_PARAM_TITLE = "title";

    /**
     * 关闭此页面
     */
    @JavascriptInterface
    public void close(JSONObject jsonObject) {
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * html文件名，jswebview参数，用于设置新的BPMWebViewActivity
     */
    public static final String API_PARAM_NEW_HTML = "html";

    /**
     * 设置当前页面的Toobar标题
     */
    @JavascriptInterface
    public void setTitle(JSONObject jsonObject) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLE, jsonObject.toString()));
    }

    /**
     * 新建WebViewActivity
     */
    @JavascriptInterface
    public void newWebViewActivity(JSONObject jsonObject) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_NEW_WEBVIEW_ACTIVITY, jsonObject.toString()));
    }

}
