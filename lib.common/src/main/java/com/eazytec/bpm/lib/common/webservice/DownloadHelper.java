package com.eazytec.bpm.lib.common.webservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.progressdialog.DownloadProgressDialog;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.eazytec.bpm.lib.common.webkit.CompletionHandler;
import com.eazytec.bpm.lib.common.webservice.progress.DownloadProgressHandler;
import com.eazytec.bpm.lib.common.webservice.progress.ProgressHelper;
import com.eazytec.bpm.lib.utils.MIMETypeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * 通用的下载功能
 * @author Administrator
 * @version Id: DownloadHelper, v 0.1 2017/7/11 19:49 Administrator Exp $$
 */
public class DownloadHelper{

   private static CompletionHandler mHandler;

   public static void download(final CommonActivity activity, final String id, final String name, final boolean isAutoOpen, final CompletionHandler handler ) {

       mHandler = handler;

        final DownloadProgressDialog dialog = new DownloadProgressDialog(activity);
        dialog.setTitle("下载");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                dialog.setProgress((int) (bytesRead *100/ contentLength));
                if (done) {
                    dialog.dismiss();
                }
            }
        });


       new Thread(new Runnable() {
           @Override public void run() {
               BPMRetrofit.downloadRetrofit().create(WebApi.class).download(id)
                       .subscribeOn(Schedulers.io())
                       .observeOn(Schedulers.io())
                       .subscribe(new Observer<ResponseBody>() {
                           @Override public void onNext(ResponseBody response) {
                               try {
                                   InputStream is = response.byteStream();
                                   File file = new File(Environment.getExternalStorageDirectory(), name);
                                   FileOutputStream fos = new FileOutputStream(file);
                                   BufferedInputStream bis = new BufferedInputStream(is);
                                   byte[] buffer = new byte[1024];
                                   int len;
                                   while ((len = bis.read(buffer)) != -1) {
                                       fos.write(buffer, 0, len);
                                       fos.flush();
                                   }
                                   fos.close();
                                   bis.close();
                                   is.close();

                                   if (mHandler != null) {
                                       fileHandler(true, null);
                                   }

                                   if (isAutoOpen) {
                                       if (Build.VERSION.SDK_INT >= 24) {
                                           // Android 7.0 需要用FileProvider的方式来将uri给外部应用使用
                                           Uri uri = FileProvider.getUriForFile(activity.getContext(), "android.support.v4.content.FileProvider", file);
                                           Intent intent = new Intent("android.intent.action.VIEW");
                                           intent.addCategory("android.intent.category.DEFAULT");
                                           intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                           intent.setDataAndType(uri, MIMETypeUtil.getMIMEType(file));
                                           activity.startActivity(intent);
                                       } else {
                                           Intent intent = new Intent("android.intent.action.VIEW");
                                           intent.addCategory("android.intent.category.DEFAULT");
                                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                           Uri uri = Uri.fromFile(file);
                                           intent.setDataAndType(uri, MIMETypeUtil.getMIMEType(file));
                                           activity.startActivity(intent);
                                       }
                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }

                           @Override public void onCompleted() {
                           }

                           @Override public void onError(Throwable e) {
                               if (mHandler != null) {
                                   fileHandler(false, null);
                               }
                               ToastDelegate.error(activity.getContext(),"文件下载失败，请稍后再试");
                               dialog.dismiss();
                           }
                       });
           }
       }).start();
   }

    public static void fileHandler(boolean isSuccess, JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        if (isSuccess) {
                try {
                    jsonObject.put("success", true);
                    jsonObject.put("errorMsg", "");
                    mHandler.complete(jsonObject.toString());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
        }else {
            try {
                jsonObject.put("success", false);
                jsonObject.put("errorMsg", "");
                mHandler.complete(jsonObject.toString());
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
