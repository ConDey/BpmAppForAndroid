package com.eazytec.bpm.app.filepicker.applicaiton;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;

/**
 * @author Administrator
 * @version Id: FilePickerApplicaiton, v 0.1 2017/7/18 16:07 Administrator Exp $$
 */
public class FilePickerApplication extends BundleApplication {

    private static FilePickerApplication instance;

    public static FilePickerApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }



}
