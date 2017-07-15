package com.eazytec.bpm.app.photo.application;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;

/**
 * @author Administrator
 * @version Id: PhotoApplication, v 0.1 2017/7/15 10:44 Administrator Exp $$
 */
public class PhotoApplication extends BundleApplication {
    private static PhotoApplication instance;

    public static PhotoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


    }

}
