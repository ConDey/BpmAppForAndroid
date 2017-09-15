package com.eazytec.bpm.lib.common.webservice.download;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;

import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.utils.AppUtils;
import com.eazytec.bpm.lib.utils.Application;
import com.eazytec.bpm.lib.utils.MIMETypeUtil;
import com.eazytec.bpm.lib.utils.ToastUtils;

import net.wequick.small.Small;

import java.io.File;

/**
 * @author 16735
 * @version Id: DownloadManagerHelper, v 0.1 2017-7-25 18:22 16735 Exp $$
 */
public class DownloadManagerHelper implements DownloadContract {

    private static int TYPE = 0x00;
    public static final int TYPE_DIALOG = 0x01;
    public static final int TYPE_NOTIFICATION = 0x02;

    // 要申请的权限
    public static String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // 下载链接
    private static String url = "";
    // 下载文件保存路径
    private static String filePath = "";
    private static Intent intent;

    // 下载管理器
    private DownloadManager manager;
    // 下载唯一ID
    private long downloadId;

    private final String apkName = AppUtils.getAppName() + AppUtils.getAppVersionName() + ".apk";

    // 上下文
    private Context mContext;

    public DownloadManagerHelper(Context context) {
        this.mContext = context;
    }

    /**
     * Notification 更新
     *
     * @param context  上下文
     * @param url      下载链接
     * @param filePath 文件保存路径
     */
    public void updateForNotification(Context context, String url, String filePath) {
        TYPE = TYPE_NOTIFICATION;
        DownloadManagerHelper.url = url;
        DownloadManagerHelper.filePath = filePath;
        startDownload(context);
    }

    /**
     * 开始下载
     */
    private void startDownload(Context context) {

        ToastUtils.showShort("系统正在后台下载新版本，完成之后将会自动打开！");

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(context, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission(context);
            } else {
                //已授权就开始下载
                DownloadBroadcastReceiver receiver = new DownloadBroadcastReceiver(DownloadManager.ACTION_DOWNLOAD_COMPLETE, DownloadManager.ACTION_NOTIFICATION_CLICKED, this);
                // 注册广播接收器
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                IntentFilter filter1 = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
                context.registerReceiver(receiver, filter);
                context.registerReceiver(receiver, filter1);


                File file = new File(Environment.getExternalStorageDirectory() + "/" + apkName);
                if (file.exists()) {
                    //删除已经存在的文件
                    file.delete();
                }
                manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDestinationInExternalPublicDir("", "/" + apkName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                downloadId = manager.enqueue(request);
            }
        } else {
//                intent = new Intent(context, DownloadService.class);
//                context.startService(intent);
//                context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    // 提示用户该请求权限的弹出框
    private static void showDialogTipUserRequestPermission(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("存储权限不可用")
                .setMessage("请先开启存储权限；\n否则，您将无法正常更新版本")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission(context);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private static void startRequestPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, permissions, 321);
    }

    @Override
    public void downloadComplete() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + apkName);
        if (file.exists()) {
            if (Build.VERSION.SDK_INT >= 24) {
                // Android 7.0 需要用FileProvider的方式来将uri给外部应用使用
                Uri uri = FileProvider.getUriForFile(mContext, Config.APK_PROVIDER_ID, file);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, MIMETypeUtil.getMIMEType(file));
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromFile(file);
                intent.setDataAndType(uri, MIMETypeUtil.getMIMEType(file));
                mContext.startActivity(intent);
            }
        } else {

        }
    }

    @Override
    public void downloadCancel() {
        //取消下载， 如果一个下载被取消了，所有相关联的文件，部分下载的文件和完全下载的文件都会被删除。
        manager.remove(downloadId);
        ToastDelegate.info(mContext, "下载已取消");
    }
}
