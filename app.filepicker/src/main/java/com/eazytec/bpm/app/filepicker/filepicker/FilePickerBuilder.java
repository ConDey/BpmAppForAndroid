package com.eazytec.bpm.app.filepicker.filepicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.eazytec.bpm.app.filepicker.filter.CompositeFilter;
import com.eazytec.bpm.app.filepicker.filter.HiddenFilter;
import com.eazytec.bpm.app.filepicker.filter.PatternFilter;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * FilePickerBuilder
 * @author Administrator
 * @version Id: FilePickerBuilder, v 0.1 2017/7/18 18:02 Administrator Exp $$
 */
public class FilePickerBuilder {
    private Activity mActivity;
    private Fragment mFragment;
    private android.support.v4.app.Fragment mSupportFragment;

    private Class<? extends FilePickerActivity> mFilePickerClass = FilePickerActivity.class;

    private Integer mRequestCode;
    private Pattern mFileFilter;
    private Boolean mDirectoriesFilter = false;
    private String mRootPath;
    private String mCurrentPath;
    private Boolean mShowHidden = false;
    private Boolean mCloseable = true;
    private CharSequence mTitle;

    public FilePickerBuilder() {
    }


    /**
     */
    public FilePickerBuilder withActivity(Activity activity) {
        if (mSupportFragment != null || mFragment != null) {
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }

        mActivity = activity;
        return this;
    }

    /**
     */
    public FilePickerBuilder withFragment(Fragment fragment) {
        if (mSupportFragment != null || mActivity != null) {
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }

        mFragment = fragment;
        return this;
    }

    /**
     */
    public FilePickerBuilder withSupportFragment(android.support.v4.app.Fragment fragment) {
        if (mActivity != null || mFragment != null) {
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }

        mSupportFragment = fragment;
        return this;
    }

    /**
     */
    public FilePickerBuilder withRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }


    /**
     */
    public FilePickerBuilder withFilter(Pattern pattern) {
        mFileFilter = pattern;
        return this;
    }

    /**
     *
     * @see FilePickerBuilder#withFilter
     */
    public FilePickerBuilder withFilterDirectories(boolean directoriesFilter) {
        mDirectoriesFilter = directoriesFilter;
        return this;
    }

    /**
     */
    public FilePickerBuilder withRootPath(String rootPath) {
        mRootPath = rootPath;
        return this;
    }

    /**
     */
    public FilePickerBuilder withPath(String path) {
        mCurrentPath = path;
        return this;
    }

    /**
     * 是否要显示隐藏文件夹
     */
    public FilePickerBuilder withHiddenFiles(boolean show) {
        mShowHidden = show;
        return this;
    }

    /**
     * 完成按钮是否要显示
     */
    public FilePickerBuilder withCloseMenu(boolean closeable) {
        mCloseable = closeable;
        return this;
    }

    /**
     * 设置标题
     */
    public FilePickerBuilder withTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public FilePickerBuilder withCustomActivity(Class<? extends FilePickerActivity> customActivityClass) {
        mFilePickerClass = customActivityClass;
        return this;
    }

    public CompositeFilter getFilter() {
        ArrayList<FileFilter> filters = new ArrayList<>();

        if (!mShowHidden) {
            filters.add(new HiddenFilter());
        }

        if (mFileFilter != null) {
            filters.add(new PatternFilter(mFileFilter, mDirectoriesFilter));
        }

        return new CompositeFilter(filters);
    }


    /**
     * @return Intent that can be used to start Material File Picker
     */
    public Intent getIntent() {
        CompositeFilter filter = getFilter();

        Activity activity = null;
        if (mActivity != null) {
            activity = mActivity;
        } else if (mFragment != null) {
            activity = mFragment.getActivity();
        } else if (mSupportFragment != null) {
            activity = mSupportFragment.getActivity();
        }

        Intent intent = new Intent(activity, mFilePickerClass);
        intent.putExtra(FilePickerActivity.ARG_FILTER, filter);
        intent.putExtra(FilePickerActivity.ARG_CLOSEABLE, mCloseable);

        if (mRootPath != null) {
            intent.putExtra(FilePickerActivity.ARG_START_PATH, mRootPath);
        }

        if (mCurrentPath != null) {
            intent.putExtra(FilePickerActivity.ARG_CURRENT_PATH, mCurrentPath);
        }

        if (mTitle != null) {
            intent.putExtra(FilePickerActivity.ARG_TITLE, mTitle);
        }

        return intent;
    }


    public void start() {
        if (mActivity == null && mFragment == null && mSupportFragment == null) {
            throw new RuntimeException("You must pass Activity/Fragment by calling withActivity/withFragment/withSupportFragment method");
        }

        if (mRequestCode == null) {
            throw new RuntimeException("You must pass request code by calling withRequestCode method");
        }

        Intent intent = getIntent();

        if (mActivity != null) {
            mActivity.startActivityForResult(intent, mRequestCode);
        } else if (mFragment != null) {
            mFragment.startActivityForResult(intent, mRequestCode);
        } else {
            mSupportFragment.startActivityForResult(intent, mRequestCode);
        }
    }
}
