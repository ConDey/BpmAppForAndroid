package com.eazytec.bpm.app.contact.usercontact.localuser;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.lib.common.activity.CommonActivity;

/**
 * 本地通讯录
 * @author Administrator
 * @version Id: LocalcContactActivity, v 0.1 2017/7/5 16:34 Administrator Exp $$
 */
public class LocalcContactActivity extends CommonActivity{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_contact);
    }
}
