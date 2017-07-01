package com.eazytec.bpm.appstub.delegate;

import android.content.Context;
import android.support.annotation.IntDef;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * 使用Toasty作为系统默认Toast的替代框架
 * <p>
 * 创建一个代理类，禁止直接使用Toasty造成代码与框架耦合
 *
 * @author ConDey
 * @version Id: ToastDelegate, v 0.1 2017/6/29 上午10:21 ConDey Exp $$
 */
public abstract class ToastDelegate {

    public static final int TOAST_TYPE_ERROR = 0;
    public static final int TOAST_TYPE_SUCCESS = 1;
    public static final int TOAST_TYPE_INFO = 2;
    public static final int TOAST_TYPE_WARINGING = 3;
    public static final int TOAST_TYPE_NORMAL = 4;

    // 自定义一个注解MyState
    @IntDef({TOAST_TYPE_ERROR, TOAST_TYPE_SUCCESS, TOAST_TYPE_INFO, TOAST_TYPE_WARINGING, TOAST_TYPE_NORMAL})
    public @interface TOAST_TYPE {
    }

    /**
     * 显示一个带有错误提示的Toast
     *
     * @param context
     * @param info
     */
    public static void error(Context context, String info) {
        Toasty.error(context, info, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * 显示一个带有成功提示的Toast
     *
     * @param context
     * @param info
     */
    public static void success(Context context, String info) {
        Toasty.success(context, info, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * 显示一个带有信息提示的Toast
     *
     * @param context
     * @param info
     */
    public static void info(Context context, String info) {
        Toasty.info(context, info, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * 显示一个带有警告信息的Toast
     *
     * @param context
     * @param info
     */
    public static void warning(Context context, String info) {
        Toasty.warning(context, info, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * 显示系统默认的Toast
     *
     * @param context
     * @param info
     */
    public static void normal(Context context, String info) {
        Toasty.normal(context, info, Toast.LENGTH_SHORT).show();
    }

}
