package com.eazytec.bpm.app.filepicker.filepicker;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.filter.CompositeFilter;
import com.eazytec.bpm.app.filepicker.filter.PatternFilter;
import com.eazytec.bpm.app.filepicker.utils.FileSelectUtils;
import com.eazytec.bpm.lib.common.activity.CommonActivity;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class FilePickerActivity extends CommonActivity implements DirectoryFragment.FileClickListener {

    public static final String ARG_START_PATH = "arg_start_path";
    public static final String ARG_CURRENT_PATH = "arg_current_path";

    public static final String ARG_FILTER = "arg_filter";
    public static final String ARG_CLOSEABLE = "arg_closeable";
    public static final String ARG_TITLE = "arg_title";

    public static final String STATE_START_PATH = "state_start_path";
    private static final String STATE_CURRENT_PATH = "state_current_path";

    public static final String RESULT_FILE_PATH = "result_file_path";
    private static final int HANDLE_CLICK_DELAY = 150;

    private String mStartPath = Environment.getExternalStorageDirectory().getAbsolutePath();//开始路径
    private String mCurrentPath = mStartPath; //当前路径

    private CharSequence mTitle;

    private Boolean mCloseable;

    private CompositeFilter mFilter;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        initArguments(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        initToolbar();
        initBackStackState();
        initFragment();



    }

    @SuppressWarnings("unchecked")
    private void initArguments(Bundle savedInstanceState) {
        if (getIntent().hasExtra(ARG_FILTER)) {
            Serializable filter = getIntent().getSerializableExtra(ARG_FILTER);

            if (filter instanceof Pattern) {
                ArrayList<FileFilter> filters = new ArrayList<>();
                filters.add(new PatternFilter((Pattern) filter, false));
                mFilter = new CompositeFilter(filters);
            } else {
                mFilter = (CompositeFilter) filter;
            }
        }

        if (savedInstanceState != null) {
            mStartPath = savedInstanceState.getString(STATE_START_PATH);
            mCurrentPath = savedInstanceState.getString(STATE_CURRENT_PATH);
            updateTitle();
        } else {
            if (getIntent().hasExtra(ARG_START_PATH)) {
                mStartPath = getIntent().getStringExtra(ARG_START_PATH);
                mCurrentPath = mStartPath;
            }

            if (getIntent().hasExtra(ARG_CURRENT_PATH)) {
                String currentPath = getIntent().getStringExtra(ARG_CURRENT_PATH);

                if (currentPath.startsWith(mStartPath)) {
                    mCurrentPath = currentPath;
                }
            }
        }

        if (getIntent().hasExtra(ARG_TITLE)) {
            mTitle = getIntent().getCharSequenceExtra(ARG_TITLE);
        }

        if (getIntent().hasExtra(ARG_CLOSEABLE)) {
            mCloseable = getIntent().getBooleanExtra(ARG_CLOSEABLE, true);
        }
    }

    /**
     * 更新状态栏上的标题
     */
    private void updateTitle() {
        if (getSupportActionBar() != null) {
            String titlePath = mCurrentPath.isEmpty() ? "/" : mCurrentPath;
            if (TextUtils.isEmpty(mTitle)) {
                getSupportActionBar().setTitle(titlePath);
            } else {
                getSupportActionBar().setSubtitle(titlePath);
            }
        }
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);

        // Show back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Truncate start of path
        try {
            Field f;
            if (TextUtils.isEmpty(mTitle)) {
                f = toolbar.getClass().getDeclaredField("mTitleTextView");
            } else {
                f = toolbar.getClass().getDeclaredField("mSubtitleTextView");
            }

            f.setAccessible(true);
            TextView textView = (TextView) f.get(toolbar);
            textView.setEllipsize(TextUtils.TruncateAt.START);
        } catch (Exception ignored) {
        }

        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
        updateTitle();
    }

    private void initBackStackState() {
        String pathToAdd = mCurrentPath;
        ArrayList<String> separatedPaths = new ArrayList<>();

        while (!pathToAdd.equals(mStartPath)) {
            pathToAdd = FileSelectUtils.cutLastSegmentOfPath(pathToAdd);
            separatedPaths.add(pathToAdd);
        }

        Collections.reverse(separatedPaths);

        for (String path : separatedPaths) {
            addFragmentToBackStack(path);
        }
    }

    private void initFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.file_picker_container, DirectoryFragment.getInstance(
                        mCurrentPath, mFilter))
                .addToBackStack(null)
                .commit();
    }

    private void addFragmentToBackStack(String path) {
        getFragmentManager().beginTransaction()
                .replace(R.id.file_picker_container, DirectoryFragment.getInstance(
                        path, mFilter))
                .addToBackStack(null)
                .commit();
    }

    /**
     * 回退处理
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();

        if (!mCurrentPath.equals(mStartPath)) {
            fm.popBackStack();
            mCurrentPath = FileSelectUtils.cutLastSegmentOfPath(mCurrentPath);
            updateTitle();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_CURRENT_PATH, mCurrentPath);
        outState.putString(STATE_START_PATH, mStartPath);
    }

    /**
     * 点击了文件，变成点击checkbox
     * @param clickedFile
     */
    @Override
    public void onFileClicked(final File clickedFile) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleFileClicked(clickedFile);
            }
        }, HANDLE_CLICK_DELAY);
    }

    private void handleFileClicked(final File clickedFile) {
        if (clickedFile.isDirectory()) {  //如果点击的是目录，进入
            mCurrentPath = clickedFile.getPath();
            // If the user wanna go to the emulated directory, he will be taken to the
            // corresponding user emulated folder.
            if (mCurrentPath.equals("/storage/emulated"))
                mCurrentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            addFragmentToBackStack(mCurrentPath);
            updateTitle();
        } else {

        //    setResultAndFinish(clickedFile.getPath());
        }
    }

    private void setResultAndFinish(String filePath) {
        Intent data = new Intent();
        data.putExtra(RESULT_FILE_PATH, filePath);
        setResult(RESULT_OK, data);
        finish();
    }
}
