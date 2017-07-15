package com.eazytec.bpm.app.notice.application;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;

/**
 * @author Administrator
 * @version Id: NoticeApplication, v 0.1 2017/7/10 9:34 Administrator Exp $$
 */
public class NoticeApplication extends BundleApplication {
    private static NoticeApplication instance;

    public static NoticeApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

}
