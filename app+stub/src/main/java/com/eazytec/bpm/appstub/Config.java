package com.eazytec.bpm.appstub;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 系统级别的常量定义
 *
 * @author ConDey
 * @version Id: Config, v 0.1 2017/6/29 上午11:00 ConDey Exp $$
 */
public abstract class Config {

    /**
     * 默认的登录TOKEN
     * <p>
     * 当用户开始鉴权的时候，需要传入一个默认的Token，每个系统的可能都有特殊的默认Token
     */
    public static String DEFAULT_TOKEN;

    /**
     * 主机地址IP + PORT
     */
    public static String WEB_URL;

    /**
     * 服务地址
     */
    public static String WEB_SERVICE_URL;

    /**
     * 更新地址 IP + PORT
     */
    public static String UPDATE_URL;

    /**
     * 更新APK地址 IP + PORT
     */
    public static String UPDATE_APK_URL;

    /**
     * APK的ApplicationId
     */
    public static String APK_APPLICAITON_ID;

    /**
     * 是否是单例调试模式
     */
    public static boolean IS_DEBUG;
}
