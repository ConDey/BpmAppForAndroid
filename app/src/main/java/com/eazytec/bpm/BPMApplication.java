package com.eazytec.bpm;

import android.app.Application;
import android.content.Context;

import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.db.DBConstants;
import com.eazytec.bpm.appstub.db.DBHelper;
import com.eazytec.bpm.appstub.push.CurrentPushParams;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

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

        DBConstants.createBriteDatabase(new DBHelper(this)); // 初始化SQLLite

        //推送
        PushAgent mPushAgent = PushAgent.getInstance(this);

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context var1, UMessage var2) {

                //app的跳转，最后解决吧，先放着！

            }
    };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        // 注册推送服务，每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {

                // 注册成功会返回device token
                 CurrentPushParams.getCurrentPushParams().updateCurrentPushParams(s);
            }

            @Override
            public void onFailure(String s, String s1) {
                String a = s;
            }
        });
    }

    private void initGradleConstants() {
        Config.DEFAULT_TOKEN = BuildConfig.DEFAULT_TOKEN;
        Config.WEB_URL = BuildConfig.WEB_URL;
        Config.WEB_SERVICE_URL = Config.WEB_URL + "/external/";
        Config.UPDATE_URL = BuildConfig.UPDATE_URL;
        Config.UPDATE_APK_URL = BuildConfig.UPDATE_APK_URL;
        Config.APK_PROVIDER_ID = BuildConfig.APK_PROVIDER_ID;
        Config.DB_NAME = BuildConfig.DB_NAME;
    }
}
