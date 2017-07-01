package com.eazytec.bpm.lib.utils;

import net.wequick.small.Small;

/**
 * Util的Application
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(Small.getContext());
    }
}
