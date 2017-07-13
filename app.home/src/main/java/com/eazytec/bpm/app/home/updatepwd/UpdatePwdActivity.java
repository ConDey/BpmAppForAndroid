package com.eazytec.bpm.app.home.updatepwd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.appstub.view.edittext.ClearEditText;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * @author Administrator
 * @version Id: UpdatePwdActivity, v 0.1 2017/7/13 15:57 Administrator Exp $$
 */
public class UpdatePwdActivity extends ContractViewActivity<UpdatePwdPresenter> implements UpdatePwdContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private ClearEditText originPwd;
    private ClearEditText newPwd;
    private ClearEditText confirmPwd;

    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("修改密码");

        originPwd = (ClearEditText) findViewById(R.id.updatepwd_origin_edittext);
        newPwd = (ClearEditText) findViewById(R.id.updatepwd_new_edittext);
        confirmPwd = (ClearEditText) findViewById(R.id.updatepwd_confirm_edittext);

        submitButton = (Button)findViewById(R.id.updatepwd_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePwd();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePwdActivity.this.finish();
            }
        });

    }

    private void updatePwd() {

        String originpwd = this.originPwd.getText().toString();
        String newpwd = this.newPwd.getText().toString();
        String confirmpwd = this.confirmPwd.getText().toString();

        getPresenter().updatePwd(originpwd,newpwd,confirmpwd);

    }


    @Override
    public void updatePwdSuccess(WebDataTObject updateData) {

        new MaterialDialog.Builder(getContext())
                .content("密码修改成功，请点击关闭此页面")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .positiveText("关闭页面")
                .show();
    }

    @Override
    protected UpdatePwdPresenter createPresenter() {
        return new UpdatePwdPresenter();
    }
}
