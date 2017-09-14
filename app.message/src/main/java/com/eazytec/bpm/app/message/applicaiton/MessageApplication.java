package com.eazytec.bpm.app.message.applicaiton;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;

/**
 * @author Beckett_W
 * @version Id: MessageApplication, v 0.1 2017/9/14 18:46 Beckett_W Exp $$
 */
public class MessageApplication extends BundleApplication {

    private static MessageApplication instance;

    public static MessageApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}