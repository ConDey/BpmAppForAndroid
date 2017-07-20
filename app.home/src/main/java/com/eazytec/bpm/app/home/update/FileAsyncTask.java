package com.eazytec.bpm.app.home.update;

/**
 * @author 16735
 * @version Id: FileAsycTask, v 0.1 2017-7-20 10:19 16735 Exp $$
 */
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by yudongwan on 17/5/6.
 */
public class FileAsyncTask extends AsyncTask<String, Integer, Integer> {


    protected HashMap<String, String> mMapParams;
    protected UpdateActivity activity;
    protected Context mContext;
    protected String filePath;
    protected String ApkURL;
    protected TextView tv;
    protected ProgressBar pb;

    protected boolean success;

    public FileAsyncTask(Context context, UpdateActivity activity, String filePath, String ApkURL,
                         TextView tv, ProgressBar pb, HashMap<String, String> params) {
        this.activity = activity;
        this.mContext = context;
        this.filePath = filePath;
        this.ApkURL = ApkURL;
        this.tv = tv;
        this.pb = pb;
        this.mMapParams = params;
        success = true;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection conn = null;
        int progress = 0;

        File file = new File(filePath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        if (file.exists()) {
            file.delete();
            file = new File(filePath);
        }

        try {
            URL url = new URL(this.ApkURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            conn.setConnectTimeout(10000);

            int contentlength = conn.getContentLength();
            if (contentlength <= 0) {
                if (conn != null) {
                    conn.disconnect();
                    success = false;
                }
                return 0;
            }

            is = conn.getInputStream();
            int hasRead = 0;
            byte[] bs = new byte[1024 * 16];
            int len;
            os = new FileOutputStream(file);
            int progressBefore = 0;
            publishProgress(progress);
            while ((len = is.read(bs)) != -1) {
                if (isCancelled()) {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }

                    if (conn != null) {
                        conn.disconnect();
                    }
                    return 0;
                }

                os.write(bs, 0, len);
                hasRead += len;
                progress = (int) ((double) hasRead / (double) contentlength * 100);
                if (hasRead > progressBefore) {
                    progressBefore = hasRead;
                    publishProgress(progress);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }

        if (success) {
            if (progress == 100) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    protected void onCancelled() {
        FileAsyncTask.this.cancel(true);
        super.onCancelled();
    }

    protected void onPostExecute(Integer result) {
        if (result == 1) {
            activity.success();
        } else {
            activity.failure();
        }
        super.onPostExecute(result);
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onProgressUpdate(Integer... values) {
        this.tv.setText(values[0] + "%");
        this.pb.setProgress(values[0]);
        super.onProgressUpdate(values);
    }


}

