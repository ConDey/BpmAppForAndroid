package com.eazytec.bpm.lib.style;

import android.app.Application;

import net.wequick.small.Small;

/**
 * @author ConDey
 * @version Id: StyleManagerApplication, v 0.1 2017/6/27 下午1:54 ConDey Exp $$
 */
public class StyleManagerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Small.setWebActivityTheme(R.style.AppTheme_NoActionBar);
    }
}
