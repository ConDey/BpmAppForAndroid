package com.eazytec.bpm.appstub.view.progressdialog;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eazytec.bpm.appstub.R;

import java.text.NumberFormat;

/**
 * @author Administrator
 * @version Id: CommonProgressDialog, v 0.1 2017/7/12 15:44 Administrator Exp $$
 */
public class CommonProgressDialog extends AlertDialog {


    private FlikerProgressBar mProgress;
    private TextView mProgressMessage;

    private Handler mViewUpdateHandler;
    private int mMax;
    private CharSequence mMessage;
    private boolean mHasStarted;
    private int mProgressVal;

    private String TAG="CommonProgressDialog";
    private String mProgressNumberFormat;
    private NumberFormat mProgressPercentFormat;
    public CommonProgressDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initFormats();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_progress_dialog);
        mProgress=(FlikerProgressBar) findViewById(R.id.progress);
        mProgressMessage=(TextView) findViewById(R.id.progress_message);
        mViewUpdateHandler = new Handler() {


            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                float progress = mProgress.getProgress();
                float max =100f;
                double dProgress = (double)progress/(double)(1024 * 1024);
                double dMax = (double)max/(double)(1024 * 1024);
            }

        };

        onProgressChanged();
        if (mMessage != null) {
            setMessage(mMessage);
        }
        if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }
    }
    private void initFormats() {
        mProgressNumberFormat = "%1.2fKB/%2.2fKB";
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }
    private void onProgressChanged() {
        mViewUpdateHandler.sendEmptyMessage(0);


    }
    public void setProgressStyle(int style) {
    }


    public void setProgress(int value) {
        if (mHasStarted) {
            mProgress.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }


    @Override
    public void setMessage(CharSequence message) {
        // TODO Auto-generated method stub
        //super.setMessage(message);
        if(mProgressMessage!=null){
            mProgressMessage.setText(message);
        }
        else{
            mMessage = message;
        }
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mHasStarted = true;
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mHasStarted = false;
    }

}
