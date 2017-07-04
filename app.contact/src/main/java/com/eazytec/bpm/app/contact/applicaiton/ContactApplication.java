package com.eazytec.bpm.app.contact.applicaiton;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;

/**
 * Created by Administrator on 2017/7/3.
 */

public class ContactApplication extends BundleApplication {

    private static ContactApplication instance;

    public static ContactApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }



}
