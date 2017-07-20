package com.eazytec.bpm.app.filepicker;

import android.app.Activity;

import com.eazytec.bpm.app.filepicker.filepicker.FilePickerBuilder;

import java.util.ArrayList;

/**
 * @author Administrator
 * @version Id: FilePicker, v 0.1 2017/7/20 10:48 Administrator Exp $$
 */
public abstract class FilePicker {

    /**
     * 固定选择pdf，doc，ppt，xls，txt，zip，apk等格式文件！
     * 返回：
     * requestCode：234
     * data： data.getStringArrayListExtra( "SELECTED_DOCS")   文件路径
     * @param activity
     * @param docPaths
     * @param maxSelectNum
     */
    public void FilePicker(Activity activity , ArrayList<String> docPaths ,  int maxSelectNum){
        FilePickerBuilder.getInstance().setMaxCount(maxSelectNum)
                .setSelectedFiles(docPaths)
                .pickFile(activity);
    }


    /**
     * 示例自定义压缩文件选择器，只要用addFileSupport就可以了，同时可以自定义图片，支持的自定义格式有 PDF,WORD,EXCEL,PPT,TXT,APK,CERTIFICATE,ZIP,UNKNOWN
     * @param activity
     * @param docPaths
     * @param maxSelectNum
     */
    public void zipFilePicker(Activity activity , ArrayList<String> docPaths ,  int maxSelectNum ){

        final String[] zips = {".zip",".rar"};
        FilePickerBuilder.getInstance().setMaxCount(maxSelectNum)
                .setSelectedFiles(docPaths)
                .addFileSupport("ZIP",zips) //or addFileSupport("ZIP",zips,R.mipmap.XXX)
                .pickFile(activity);
    }


}
