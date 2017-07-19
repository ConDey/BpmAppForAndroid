package com.eazytec.bpm.app.filepicker.utils;

import android.support.v4.app.FragmentActivity;

import com.eazytec.bpm.app.filepicker.cursors.DocScannerTask;
import com.eazytec.bpm.app.filepicker.cursors.loadercallbacks.FileResultCallback;
import com.eazytec.bpm.app.filepicker.models.Document;

/**
 * @author Administrator
 * @version Id: MediaStoreHelper, v 0.1 2017/7/19 10:17 Administrator Exp $$
 */
public class MediaStoreHelper {

    public static void getDocs(FragmentActivity activity, FileResultCallback<Document> fileResultCallback)
    {
        new DocScannerTask(activity,fileResultCallback).execute();
    }
}
