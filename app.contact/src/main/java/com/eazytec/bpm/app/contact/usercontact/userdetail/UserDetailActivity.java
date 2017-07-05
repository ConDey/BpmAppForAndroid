package com.eazytec.bpm.app.contact.usercontact.userdetail;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.imageview.AvatarImageView;
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

public class UserDetailActivity extends ContractViewActivity<UserDetailPresenter> implements UserDetailContract.View {

    private Toolbar toolbar;

    private AvatarImageView avatarImageView;

    private TextView usernameTextView;
    private TextView departmentTextView;
    private TextView positionTextView;
    private TextView telTextView;
    private TextView emailTextView;

    private RelativeLayout telLayout;

    //悬浮菜单按钮
    private LinearLayout SendMsgLayout;
    private LinearLayout TelphoneLayout;

    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

        // todo common_left_back的命名方式不对，以及下面所有的图标 应该以ic开头而不是icon
        toolbar = (Toolbar) findViewById(R.id.user_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.common_left_back);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("人员信息详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.user_detail_collapasingtoolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        avatarImageView = (AvatarImageView) findViewById(R.id.user_detail_avatarImageView);
        avatarImageView.setTextAndColor(name,0xffff6655);

        usernameTextView = (TextView) findViewById(R.id.user_detail_username);
        departmentTextView = (TextView) findViewById(R.id.user_detail_departmentname);
        positionTextView = (TextView) findViewById(R.id.user_detail_postition);
        telTextView = (TextView) findViewById(R.id.user_detail_tel);
        emailTextView = (TextView) findViewById(R.id.user_detail_email);

        telLayout = (RelativeLayout) findViewById(R.id.user_detail_tel_layout);

        SendMsgLayout = (LinearLayout) findViewById(R.id.user_detail_contact_way_msg);
        TelphoneLayout = (LinearLayout) findViewById(R.id.user_detail_contact_way_tel);

        scrollView = (NestedScrollView) findViewById(R.id.user_detail_scrollview);

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
                    RxPermissions rxPermissions = new RxPermissions(UserDetailActivity.this);
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
                    RxPermissions rxPermissions = new RxPermissions(UserDetailActivity.this);
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
                    RxPermissions rxPermissions = new RxPermissions(UserDetailActivity.this);
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
