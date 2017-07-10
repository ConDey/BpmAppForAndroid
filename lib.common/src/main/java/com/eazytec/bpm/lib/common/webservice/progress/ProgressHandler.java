package com.eazytec.bpm.lib.common.webservice.progress;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @author Administrator
 * @version Id: ProgressHandler, v 0.1 2017/7/10 16:48 Administrator Exp $$
 */
public abstract class ProgressHandler {
    protected abstract void sendMessage(ProgressBean progressBean);

    protected abstract void handleMessage(Message message);

    protected abstract void onProgress(long progress, long total, boolean done);

    protected static class ResponseHandler extends Handler {

        private ProgressHandler mProgressHandler;
        public ResponseHandler(ProgressHandler mProgressHandler, Looper looper) {
            super(looper);
            this.mProgressHandler = mProgressHandler;
        }

        @Override
        public void handleMessage(Message msg) {
            mProgressHandler.handleMessage(msg);
        }
    }
}
