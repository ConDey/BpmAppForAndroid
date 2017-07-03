package com.eazytec.bpm.app.webkit;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;

/**
 * app.webkit插件
 * <p>
 * 插件职责： 提供jswebview网页入口
 *
 * @author ConDey
 * @version Id: WebKitApplicaton, v 0.1 2017/6/29 下午3:06 ConDey Exp $$
 */
public class WebKitApplicaton extends BundleApplication {

    private static WebKitApplicaton instance;

    public static WebKitApplicaton getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
