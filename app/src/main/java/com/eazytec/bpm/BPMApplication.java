package com.eazytec.bpm;

import android.app.Application;
import com.eazytec.bpm.appstub.Config;

import net.wequick.small.Small;

/**
 * /**
 * 　　┏┓　　　┏┓+ +
 * 　┏┛┻━━━┛┻┓ + +
 * 　┃　　　　　　　┃
 * 　┃　　　━　　　┃ ++ + + +
 * ████━████ ┃+
 * 　┃　　　　　　　┃ +
 * 　┃　　　┻　　　┃
 * 　┃　　　　　　　┃ + +
 * 　┗━┓　　　┏━┛
 * 　　　┃　　　┃
 * 　　　┃　　　┃ + + + +
 * 　　　┃　　　┃
 * 　　　┃　　　┃ +  神兽保佑
 * 　　　┃　　　┃    代码无bug
 * 　　　┃　　　┃　　+
 * 　　　┃　 　　┗━━━┓ + +
 * 　　　┃ 　　　　　　　┣┓
 * 　　　┃ 　　　　　　　┏┛
 * 　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　┃┫┫　┃┫┫
 * 　　　　┗┻┛　┗┻┛+ + + +
 *
 * @author ConDey
 * @version Id: BPMApplication, v 0.1 2017/6/26 下午2:34 ConDey Exp $$
 */
public class BPMApplication extends Application {

    public BPMApplication() {
        // This should be the very first of the application lifecycle.
        // It's also ahead of the installing of content providers by what we can avoid
        // the ClassNotFound exception on if the provider is unimplemented in the host.
        Small.preSetUp(this);

        Config.IS_DEBUG = false; //不是单模块调试模式
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Small.setLoadFromAssets(BuildConfig.LOAD_FROM_ASSETS);

        initGradleConstants(); // 初始化Gradle常量
    }

    private void initGradleConstants() {
        Config.DEFAULT_TOKEN = BuildConfig.DEFAULT_TOKEN;
        Config.WEB_URL = BuildConfig.WEB_URL;
        Config.WEB_SERVICE_URL = Config.WEB_URL + "/external/";
        Config.UPDATE_URL = BuildConfig.UPDATE_URL;
        Config.UPDATE_APK_URL = BuildConfig.UPDATE_APK_URL;
        Config.APK_APPLICAITON_ID = BuildConfig.APK_APPLICAITON_ID;
    }
}
