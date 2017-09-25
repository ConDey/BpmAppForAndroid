package com.eazytec.bpm.app.calendar;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;

/**
 * @author Beckett_W
 * @version Id: CalendarApplicaiton, v 0.1 2017/9/25 8:53 Beckett_W Exp $$
 */
public class CalendarApplicaiton extends BundleApplication {

    private static CalendarApplicaiton instance;

    public static CalendarApplicaiton getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
