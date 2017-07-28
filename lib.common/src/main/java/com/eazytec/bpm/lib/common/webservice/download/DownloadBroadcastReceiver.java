package com.eazytec.bpm.lib.common.webservice.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Switch;

/**
 * 用于下载事件的广播
 *
 * @author 16735
 * @version Id: DownloadBroadcastReceiver, v 0.1 2017-7-26 16:31 16735 Exp $$
 */
public class DownloadBroadcastReceiver extends BroadcastReceiver {

    private String DOWNLOAD_COMPLETE;
    private String DOWNLOAD_NOTIFICATION_CLICKED;
    private DownloadContract contract;



    public DownloadBroadcastReceiver(String downloadState, String clickState, DownloadContract contract) {
        super();
        this.DOWNLOAD_COMPLETE = downloadState;
        this.DOWNLOAD_NOTIFICATION_CLICKED = clickState;
        this.contract = contract;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DOWNLOAD_COMPLETE)) {
            // 下载完成了
            contract.downloadComplete();
        }else if (action.equals(DOWNLOAD_NOTIFICATION_CLICKED)){
            // 点击代表取消下载
            contract.downloadCancel();
        }
    }

}
