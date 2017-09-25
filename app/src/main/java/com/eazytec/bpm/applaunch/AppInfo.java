package com.eazytec.bpm.applaunch;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.compat.BuildConfig;

import java.util.List;

/**
 * 应用程序相关信息工具类
 *
 * @author ConDey
 * @version Id: AppInfoUtil, v 0.1 16/8/29 上午8:28 ConDey Exp $$
 */
public class AppInfo {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private AppInfo() {

    }

    /**
     * 获取应用程序信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取应用程序版本名称
     */
    public static String getVersionName(Context context) {
        return null == getPackageInfo(context) ? null : getPackageInfo(context).versionName;
    }


    /**
     * 获取应用程序版本号
     */
    public static int getVersionCode(Context context) {
        return null == getPackageInfo(context) ? null : getPackageInfo(context).versionCode;
    }


    /**
     * 获取应用程序包名
     */
    public static String getPackageName(Context context) {
        return null == getPackageInfo(context) ? null : getPackageInfo(context).packageName;
    }
    public static final String APK_AUTHORITY = BuildConfig.APPLICATION_ID+".provider";

    /**
     * 判断App是否处于前台
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground() {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = manager.getRunningAppProcesses();
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(getContext().getPackageName());
            }
        }
        return false;
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }
}
