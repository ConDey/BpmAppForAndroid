package com.eazytec.bpm.app.webkit;

import com.eazytec.bpm.lib.common.activity.WebViewActivity;
import com.eazytec.bpm.lib.common.webkit.CommonjsApi;

/**
 * 通用的JsApi, 所有插件如果要扩展JsApi必须继承此类
 *
 * @author ConDey
 * @version Id: BPMJsApi, v 0.1 2017/7/3 下午1:40 ConDey Exp $$
 */
public class BPMJsApi extends CommonjsApi {


    /**
     * 必须注入Activity
     *
     * @param activity
     */
    public BPMJsApi(WebViewActivity activity) {
        super(activity);
    }


    /**
     * 获取BPM WebViewActivity
     *
     * @return
     */
    private BPMWebViewActivity getBpmWebViewActivity() {
        return (BPMWebViewActivity) activity;
    }

}
