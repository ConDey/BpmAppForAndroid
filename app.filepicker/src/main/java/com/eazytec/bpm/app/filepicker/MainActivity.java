package com.eazytec.bpm.app.filepicker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eazytec.bpm.app.filepicker.filepicker.FilePickerBuilder;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerActivity;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * @author Administrator
 * @version Id: MainActivity, v 0.1 2017/7/18 16:11 Administrator Exp $$
 */
public class MainActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private TextView pathTextView;
    private Button button;

    public static final int FILE_PICKER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        toolbarTitleTextView.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("文件选择器");

        pathTextView = (TextView) findViewById(R.id.demo_file_path);
        button = (Button)findViewById(R.id.demo_pick_button);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if(aBoolean){
                                  //  Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
                                  //  startActivityForResult(intent, 1);
                                    new FilePickerBuilder()
                                            .withActivity(MainActivity.this)
                                            .withRequestCode(FILE_PICKER_REQUEST_CODE)
                                            .withHiddenFiles(true)
                                            .withTitle("手机文件")
                                            .start();
                                }else{
                                    ToastDelegate.info(getContext(), "您没有授权查看本机文件");
                                }
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            if (path != null) {
                pathTextView.setText("所选文件路径："+path);
            }
        }
    }
}
