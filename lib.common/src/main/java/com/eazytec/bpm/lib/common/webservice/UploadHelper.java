package com.eazytec.bpm.lib.common.webservice;

import android.app.ProgressDialog;

import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.progressdialog.DownloadProgressDialog;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.eazytec.bpm.lib.common.webkit.CompletionHandler;
import com.eazytec.bpm.lib.common.webservice.progress.DownloadProgressHandler;
import com.eazytec.bpm.lib.common.webservice.progress.ProgressHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * 通用的上传功能
 *
 * @version Id: UploadHelper, v 0.1 2017-7-17 15:55 16735 Exp $$
 * @auther 16735
 */
public class UploadHelper {

    private static CompletionHandler mHandler;

    public static void upload(final CommonActivity activity, final File file, final CompletionHandler handler) {

        mHandler = handler;

        final DownloadProgressDialog dialog = new DownloadProgressDialog(activity);
        dialog.setTitle("上传");
        dialog.setMessage("正在上传，请稍后...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                dialog.setProgress((int) (bytesRead * 100 / contentLength));
                if (done) {
                    dialog.dismiss();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                String suffix = "";
                String filename = file.getName();
                BPMRetrofit.downloadRetrofit().create(WebApi.class).upload(filename, fileBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Observer<UploadFileDataTObject>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mHandler != null) {
                                    DownloadHelper.fileHandler(false, null, null,mHandler);
                                }
                                ToastDelegate.error(activity.getContext(), "上传文件失败，请稍后再试");
                                dialog.dismiss();
                            }

                            @Override
                            public void onNext(UploadFileDataTObject responseBody) {
                                try {
                                    dialog.dismiss();
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("path", responseBody.getPath());
                                    jsonObject.put("rename", responseBody.getRename());
                                    jsonObject.put("name", responseBody.getName());
                                    jsonObject.put("id", responseBody.getId());
                                    if (mHandler != null) {
                                        DownloadHelper.fileHandler(responseBody.isSuccess(), jsonObject,responseBody.getErrorMsg(), mHandler);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).start();
    }
}
