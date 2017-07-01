package com.eazytec.bpm.applaunch;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 应用程序相关信息工具类
 *
 * @author ConDey
 * @version Id: AppInfoUtil, v 0.1 16/8/29 上午8:28 ConDey Exp $$
 */
public class AppInfo {

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

}
