package com.eazytec.bpm.app.contact.usercontact.localcontact;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.imageview.AvatarImageView;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.eazytec.bpm.lib.utils.IntentUtils;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * @author Administrator
 * @version Id: LocalContactDetailActivity, v 0.1 2017/7/6 18:51 Administrator Exp $$
 */
public class LocalContactDetailActivity extends CommonActivity {

    private Toolbar toolbar;

    private AvatarImageView avatarImageView;

    private TextView usernameTextView;
    private TextView telTextView;
    private TextView toolbarTitle;


    private RelativeLayout telLayout;

    //悬浮菜单按钮
    private LinearLayout SendMsgLayout;
    private LinearLayout TelphoneLayout;

    private NestedScrollView scrollView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_user_detail);

        toolbar = (Toolbar) findViewById(R.id.local_user_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitle = (TextView) findViewById(R.id.local_user_detail_toolbar_title);
        toolbarTitle.setText("人员信息详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phoneNum");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.local_user_detail_collapasingtoolbar);
        collapsingToolbarLayout.setTitleEnabled(true);

        avatarImageView = (AvatarImageView) findViewById(R.id.local_user_detail_avatarImageView);
        avatarImageView.setText(name);

        usernameTextView = (TextView) findViewById(R.id.local_user_detail_username);
        telTextView = (TextView) findViewById(R.id.local_user_detail_tel);

        usernameTextView.setText(name);
        telTextView.setText(phone);

        telLayout = (RelativeLayout) findViewById(R.id.local_user_detail_tel_layout);

        SendMsgLayout = (LinearLayout) findViewById(R.id.local_user_detail_contact_way_msg);
        TelphoneLayout = (LinearLayout) findViewById(R.id.local_user_detail_contact_way_tel);

        scrollView = (NestedScrollView) findViewById(R.id.local_user_detail_scrollview);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalContactDetailActivity.this.finish();
            }
        });

        RxView.clicks(this.telLayout).throttleFirst(2, TimeUnit.MINUTES).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                final String tel = telTextView.getText().toString();

                if (!StringUtils.isEmpty(tel)) {
                    RxPermissions rxPermissions = new RxPermissions(LocalContactDetailActivity.this);
                    rxPermissions.request(Manifest.permission.CALL_PHONE)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if(aBoolean){
                                        Intent intent = IntentUtils.getCallIntent(tel);
                                        startActivity(intent);
                                    }else{
                                        ToastDelegate.info(getContext(), "您没有授权拨打电话");
                                    }
                                }
                            });
                } else {
                    ToastDelegate.info(getContext(), "该用户没有电话号码");
                }
            }
        });

        TelphoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tel = telTextView.getText().toString();
                if (!StringUtils.isEmpty(tel)) {
                    RxPermissions rxPermissions = new RxPermissions(LocalContactDetailActivity.this);
                    rxPermissions.request(Manifest.permission.CALL_PHONE)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if(aBoolean){
                                        Intent intent = IntentUtils.getCallIntent(tel);
                                        startActivity(intent);
                                    }else{
                                        ToastDelegate.info(getContext(), "您没有授权拨打电话");
                                    }
                                }
                            });
                } else {
                    ToastDelegate.info(getContext(), "该用户没有电话号码");
                }
            }
        });

        SendMsgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tel = telTextView.getText().toString();
                if (!StringUtils.isEmpty(tel)) {
                    RxPermissions rxPermissions = new RxPermissions(LocalContactDetailActivity.this);
                    rxPermissions.request(Manifest.permission.SEND_SMS)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if(aBoolean){
                                        Intent intent = IntentUtils.getSendSmsIntent(tel,"");
                                        startActivity(intent);
                                    }else{
                                        ToastDelegate.info(getContext(), "您没有授权发送短信");
                                    }
                                }
                            });
                } else {
                    ToastDelegate.info(getContext(), "该用户没有手机号码");
                }
            }
        });

    }
}
