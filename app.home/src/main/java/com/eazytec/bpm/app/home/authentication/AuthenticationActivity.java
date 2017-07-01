package com.eazytec.bpm.app.home.authentication;

import android.os.Bundle;
import android.widget.Button;

import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.authenication.AuthenticationDataTObject;
import com.eazytec.bpm.app.home.userhome.UserHomeActivity;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.appstub.view.edittext.ClearEditText;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


/**
 * 用户身份认证Activity
 * <p>
 * <p>
 * 用于用户身份认证，权限鉴权
 *
 * @author ConDey
 * @version Id: AuthenticationActivity, v 0.1 2017/6/27 下午7:22 ConDey Exp $$
 */
public class AuthenticationActivity extends ContractViewActivity<AuthenticationPresenter> implements AuthenticationContract.View {

    private ClearEditText usernameEditText;
    private ClearEditText passwordEditText;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        usernameEditText = (ClearEditText) findViewById(R.id.et_authentication_username);
        usernameEditText.setText(CurrentUser.getCurrentUser().getLastLoginUsername());

        passwordEditText = (ClearEditText) findViewById(R.id.et_authentication_password);
        passwordEditText.setText(CurrentUser.getCurrentUser().getLastLoginPassword());

        loginButton = (Button) findViewById(R.id.bt_authentication);

        // 绑定用户登录点击事件
        RxView.clicks(this.loginButton).throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getPresenter().userlogin(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    }
                });
    }

    /**
     * 创建用户鉴权Presenter
     *
     * @return
     */
    @Override protected AuthenticationPresenter createPresenter() {
        return new AuthenticationPresenter();
    }

    /**
     * 登录成功后的页面处理
     *
     * @param loginData
     */
    @Override public void loginSuccess(AuthenticationDataTObject loginData) {
        skipActivity(AuthenticationActivity.this, UserHomeActivity.class);
    }
}
