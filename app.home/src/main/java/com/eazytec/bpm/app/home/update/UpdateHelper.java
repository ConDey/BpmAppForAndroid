package com.eazytec.bpm.app.home.update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.appstub.Config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 16735
 * @version Id: UpdateHelper, v 0.1 2017-7-20 10:27 16735 Exp $$
 */
public class UpdateHelper {

    private static String UpdateData;
    private static HashMap<String, String> hm;

    /**
     * 检查更新
     *
     * @param showToast 如果不需要更新 ，是否提示
     * @param context 上下文
     * @param activity 当前activity
     */
    public static void doUpdate(final boolean showToast, final Context context, final Activity activity){

        OkHttpClient httpCient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Config.UPDATE_URL)
                .build();
        Call call = httpCient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseMsg = response.body().string();

                ParseXmlService ps = new ParseXmlService();
                InputStream Is = null;
                hm = new HashMap<String, String>();
                String StartSign = "<version_info>";
                String EndSign = "</version_info>";
                UpdateData = responseMsg.substring(responseMsg.indexOf(StartSign),responseMsg.indexOf(EndSign)+EndSign.length());
                if (UpdateData != null && !UpdateData.equals("")){
                    Is = new ByteArrayInputStream(UpdateData.getBytes());
                    try{
                        hm = ps.parseXml(Is);
                        int NewVersionCode = Integer.parseInt(hm.get("VersionCode")
                                .toString());
                        int OldVersionCode = VersionUtil.getVersionCode(context);
                        if (NewVersionCode > OldVersionCode) {

                            // 跳转到更新页面
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    final String name = hm.get("VersionName").toString();
                                    final String msg = hm.get("VersionDescription")
                                            .toString();
                                    new MaterialDialog.Builder(context)
                                            .title("发现新版本!版本名称:" + name + ",是否需要更新 ?")
                                            .content(msg)
                                            .positiveText("立即更新")
                                            .negativeText("下次再说")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                                    Intent it = new Intent(context, UpdateActivity.class);
                                                    it.putExtra("VersionName", hm.get("VersionName").toString());
                                                    it.putExtra("VersionDescription", hm.get("VersionDescription")
                                                            .toString());
                                                    it.putExtra("VersionURL", hm.get("VersionURL").toString());
                                                    activity.startActivity(it);
                                                }
                                            })
                                            .show();
                                }
                            });

                        }else {
                            if (showToast) {
                                // 跳转到更新页面
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //   ToastUtil.showToast(context, "当前版本已是最新版本");
                                        Toasty.normal(context,"当前版本已是最新版本").show();
                                    }
                                });
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
