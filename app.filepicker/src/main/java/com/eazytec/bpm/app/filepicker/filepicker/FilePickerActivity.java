package com.eazytec.bpm.app.filepicker.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.fragments.DocFragment;
import com.eazytec.bpm.app.filepicker.fragments.DocPickerFragment;
import com.eazytec.bpm.app.filepicker.utils.FragmentUtil;

import java.util.ArrayList;

/**
 * @author Administrator
 * @version Id: FilePickerActivity, v 0.1 2017/7/19 10:20 Administrator Exp $$
 */
public class FilePickerActivity extends AppCompatActivity implements
        DocFragment.DocFragmentListener,
        DocPickerFragment.DocPickerFragmentListener{

    private static final String TAG = FilePickerActivity.class.getSimpleName();
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(FilePickerManager.getInstance().getTheme());
        setContentView(R.layout.activity_file_picker);
        if(!FilePickerManager.getInstance().isEnableOrientation())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            if(getSupportActionBar()!=null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ArrayList<String> selectedPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            type = intent.getIntExtra(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER);

            if(selectedPaths!=null) {
                if (type == FilePickerConst.MEDIA_PICKER) {
                    FilePickerManager.getInstance().add(selectedPaths, FilePickerConst.FILE_TYPE_MEDIA);
                } else {
                    FilePickerManager.getInstance().add(selectedPaths, FilePickerConst.FILE_TYPE_DOCUMENT);
                }
            }

            setToolbarTitle(FilePickerManager.getInstance().getCurrentCount());
            openSpecificFragment(type, selectedPaths);
        }
    }

    private void openSpecificFragment(int type, @Nullable ArrayList<String> selectedPaths) {
        if (selectedPaths == null) {
            selectedPaths = new ArrayList<>();
        }

        if (FilePickerManager.getInstance().getMaxCount() == 1) {
            selectedPaths.clear();
        }

        if (type == FilePickerConst.MEDIA_PICKER) {

        } else {
            if(FilePickerManager.getInstance().isDocSupport())
                FilePickerManager.getInstance().addDocTypes();

            DocPickerFragment photoFragment = DocPickerFragment.newInstance(selectedPaths);
            FragmentUtil.addFragment(this, R.id.file_picker_container, photoFragment);
        }
    }

    private void setToolbarTitle(int count) {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            if (FilePickerManager.getInstance().getMaxCount() > 1)
                actionBar.setTitle(String.format(getString(R.string.attachments_title_text), count, FilePickerManager.getInstance().getMaxCount()));
            else {
                if (type == FilePickerConst.MEDIA_PICKER)
                    actionBar.setTitle(R.string.select_photo_text);
                else
                    actionBar.setTitle(R.string.select_doc_text);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_done) {

            returnData(FilePickerManager.getInstance().getSelectedFiles());

            return true;
        } else if (i == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_MEDIA_DETAIL:
                if(resultCode== Activity.RESULT_OK)
                {
                    returnData(FilePickerManager.getInstance().getSelectedFiles());
                }
                else
                {
                    setToolbarTitle(FilePickerManager.getInstance().getCurrentCount());
                }
                break;
        }
    }

    private void returnData(ArrayList<String> paths) {
        Intent intent = new Intent();
        if (type == FilePickerConst.MEDIA_PICKER)
            intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA, paths);
        else
            intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS, paths);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemSelected() {
        setToolbarTitle(FilePickerManager.getInstance().getCurrentCount());

        if(FilePickerManager.getInstance().getMaxCount()==1)
            returnData( FilePickerManager.getInstance().getSelectedFiles());
    }
}

