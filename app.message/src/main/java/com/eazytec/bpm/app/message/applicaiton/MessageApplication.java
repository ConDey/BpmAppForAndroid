package com.eazytec.bpm.app.message.applicaiton;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.eazytec.bpm.lib.common.bundle.BundleApplication;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;

/**
 * @author Beckett_W
 * @version Id: MessageApplication, v 0.1 2017/9/14 18:46 Beckett_W Exp $$
 */
public class MessageApplication extends BundleApplication {

    private static MessageApplication instance;

    public static MessageApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}