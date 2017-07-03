package com.eazytec.bpm.app.home;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;
import com.eazytec.bpm.lib.utils.Utils;

/**
 * app.home插件
 * <p>
 * 插件职责： 维护登录，首页等一般主体功能，和一些零散的小插件，一般是所有插件的入口
 *
 * @author ConDey
 * @version Id: HomeApplicaton, v 0.1 2017/6/29 下午3:06 ConDey Exp $$
 */
public class HomeApplicaton extends BundleApplication {

    private static HomeApplicaton instance;

    public static HomeApplicaton getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
