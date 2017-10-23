package com.eazytec.bpm.app.map;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;

/**
 * @author Beckett_W
 * @version Id: MapApplicaiton, v 0.1 2017/10/23 15:33 Beckett_W Exp $$
 */
public class MapApplicaiton extends BundleApplication {

    private static MapApplicaiton instance;

    public static MapApplicaiton getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
