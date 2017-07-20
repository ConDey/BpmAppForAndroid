package com.eazytec.bpm.app.filepicker.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

import com.eazytec.bpm.app.filepicker.models.FileType;

import java.util.ArrayList;

/**
 * 
 * @author Administrator
 * @version Id: FilePickerBuilder, v 0.1 2017/7/19 10:43 Administrator Exp $$
 */
public class FilePickerBuilder {
    
    
    private final Bundle mPickerOptionsBundle;

    public FilePickerBuilder()
    {
        mPickerOptionsBundle = new Bundle();
    }

    public static FilePickerBuilder getInstance()
    {
        return new FilePickerBuilder();
    }

    public FilePickerBuilder setMaxCount(int maxCount)
    {
        FilePickerManager.getInstance().setMaxCount(maxCount);
        return this;
    }

    public FilePickerBuilder setActivityTheme(int theme)
    {
        FilePickerManager.getInstance().setTheme(theme);
        return this;
    }

    public FilePickerBuilder setSelectedFiles(ArrayList<String> selectedFiles)
    {
        mPickerOptionsBundle.putStringArrayList(FilePickerConst.KEY_SELECTED_DOCS, selectedFiles);
        return this;
    }

    public FilePickerBuilder showFolderView(boolean status)
    {
        FilePickerManager.getInstance().setShowFolderView(status);
        return this;
    }

    public FilePickerBuilder enableDocSupport(boolean status)
    {
        FilePickerManager.getInstance().setDocSupport(status);
        return this;
    }

    public FilePickerBuilder enableOrientation(boolean status)
    {
        FilePickerManager.getInstance().setEnableOrientation(status);
        return this;
    }

    public FilePickerBuilder addFileSupport(String title, String[] extensions, @DrawableRes int drawable)
    {
        FilePickerManager.getInstance().addFileType(new FileType(title,extensions,drawable));
        return this;
    }

    public FilePickerBuilder addFileSupport(String title, String[] extensions)
    {
        FilePickerManager.getInstance().addFileType(new FileType(title,extensions,0));
        return this;
    }


    public void pickFile(Activity context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.DOC_PICKER);
        start(context,FilePickerConst.DOC_PICKER);
    }

    public void pickFile(Fragment context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.DOC_PICKER);
        start(context,FilePickerConst.DOC_PICKER);
    }

    private void start(Activity context, int pickerType)
    {
        FilePickerManager.getInstance().setProviderAuthorities(context.getApplicationContext().getPackageName() + ".filepicker.provider");

        Intent intent = new Intent(context, FilePickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);
        
        context.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_DOC);
    }

    private void start(Fragment fragment, int pickerType)
    {
        FilePickerManager.getInstance().setProviderAuthorities(fragment.getContext().getApplicationContext().getPackageName() + ".filepicker.provider");

        Intent intent = new Intent(fragment.getActivity(), FilePickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);
         fragment.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_DOC);
    }
}
