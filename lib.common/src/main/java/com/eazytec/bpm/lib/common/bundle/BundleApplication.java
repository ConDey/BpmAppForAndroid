package com.eazytec.bpm.lib.common.bundle;

import android.app.Application;

import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.lib.utils.Utils;

/**
 * Bundle所属的Application,所有的App.Bundle必须继承此类，如果没有的继承的没有办法调试
 *
 * @author ConDey
 * @version Id: BundleApplication, v 0.1 2017/6/29 下午3:04 ConDey Exp $$
 */
public class BundleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (isDebug()) {
            Utils.init(this);

            // 模拟一下登录，构造Token
        }
    }

    public static boolean isDebug() {
        return Config.IS_DEBUG;
    }
}
