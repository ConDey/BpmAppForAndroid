package com.eazytec.bpm.app.contact.usercontact.userdetail;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.imageview.LetterImageView;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.utils.IntentUtils;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/4.
 */

public class UserDetailActivity extends ContractViewActivity<UserDetailPresenter> implements  UserDetailContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private LetterImageView userDetailImageView;

    private ScrollView scrollView;

    private TextView usernameTextView;
    private TextView departmentTextView;
    private TextView positionTextView;
    private TextView telTextView;
    private TextView emailTextView;

    private RelativeLayout telLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText(name);

        scrollView = (ScrollView) findViewById(R.id.detail_contract_scrollview);

        userDetailImageView = (LetterImageView) findViewById(R.id.user_detail_imageview);
        userDetailImageView.setLetter(name.substring(0, 1));
        userDetailImageView.setOval(true);

        usernameTextView = (TextView) findViewById(R.id.user_detail_username);
        departmentTextView = (TextView) findViewById(R.id.user_detail_departmentname);
        positionTextView = (TextView) findViewById(R.id.user_detail_postition);
        telTextView = (TextView) findViewById(R.id.user_detail_tel);
        emailTextView = (TextView) findViewById(R.id.user_detail_email);

        telLayout = (RelativeLayout) findViewById(R.id.user_detail_tel_layout);


        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.requestFocus();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailActivity.this.finish();
            }
        });

        getPresenter().loadUserDetail(id);

        RxView.clicks(this.telLayout).throttleFirst(2, TimeUnit.MINUTES).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                final String tel = telTextView.getText().toString();

                if (!StringUtils.isEmpty(tel)) {
                    RxPermissions.getInstance(UserDetailActivity.this)
                            .request(Manifest.permission.CALL_PHONE)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onCompleted() {
                                    IntentUtils.getCallIntent(tel);
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    ToastDelegate.info(getContext(),"您没有授权拨打电话");
                                }

                                @Override
                                public void onNext(Boolean aBoolean) {

                                }
                            });
                } else {
                    ToastDelegate.info(getContext(),"该用户没有电话号码");
                }
            }
        });
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(receiver, filter);

    }

    @Override
    public void loadSuccess(UserDetailDataTObject userDetailDataTObject) {
        usernameTextView.setText(userDetailDataTObject.getFullName());
        departmentTextView.setText(userDetailDataTObject.getDepartmentName());
        positionTextView.setText(userDetailDataTObject.getPosition());
        telTextView.setText(userDetailDataTObject.getMobile());
        emailTextView.setText(userDetailDataTObject.getEmail());
    }

    @Override
    protected UserDetailPresenter createPresenter() {
        return new UserDetailPresenter();
    }

}
