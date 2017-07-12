package com.eazytec.bpm.appstub.view.progressdialog;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.eazytec.bpm.appstub.R;

/**
 * @author Administrator
 * @version Id: AnimatedProgressDialog, v 0.1 2017/7/12 15:44 Administrator Exp $$
 */
public class AnimatedProgressDialog  extends ProgressDialog{

    private Context context = null;
    private ZYDownloading zyDownloading;

    public AnimatedProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AnimatedProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.view_progressdialog);

        zyDownloading = (ZYDownloading) findViewById(R.id.view_custom_progressdialog);


    }

    @Override
    public void setProgress(int value) {
        super.setProgress(value);
        zyDownloading.startDownload();
        zyDownloading.setProgress(value);
    }

    @Override
    public void show() {
        super.show();
    }
}
