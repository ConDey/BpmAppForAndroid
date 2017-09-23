package com.eazytec.bpm.app.home.authentication;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.eazytec.bpm.app.home.HomeApplicaton;
import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.authenication.AuthenticationDataTObject;
import com.eazytec.bpm.app.home.data.commonconfig.ImgDataTObject;
import com.eazytec.bpm.app.home.userhome.UserHomeActivity;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.appstub.view.edittext.ClearEditText;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

    private Bitmap picLoginBitmap;

    private RelativeLayout bgRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        usernameEditText = (ClearEditText) findViewById(R.id.et_authentication_username);
        usernameEditText.setText(CurrentUser.getCurrentUser().getLastLoginUsername());

        passwordEditText = (ClearEditText) findViewById(R.id.et_authentication_password);
        passwordEditText.setText(CurrentUser.getCurrentUser().getLastLoginPassword());

        loginButton = (Button) findViewById(R.id.bt_authentication);

        bgRelativeLayout = (RelativeLayout) findViewById(R.id.activity_authentication_relativelayout);

        // 绑定用户登录点击事件
        RxView.clicks(this.loginButton).throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getPresenter().userlogin(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    }
                });

        getPresenter().commonconfig();
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

    /**
     * 图片有url
     * @param imgDataTObject
     */
    @Override
    public void getImgUrl(ImgDataTObject imgDataTObject) {
        if(!StringUtils.isEmpty(imgDataTObject.getLoginBackgroundImg())){
            picLoginBitmap = HomeApplicaton.picLoginBgBitmap;

            String url = Config.WEB_URL+imgDataTObject.getLoginBackgroundImg();

            Picasso.with(this).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (android.os.Build.VERSION.SDK_INT > 15) {
                    bgRelativeLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                } else {
                    bgRelativeLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                }
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                //失败用本地的图片
                if (android.os.Build.VERSION.SDK_INT > 15) {
                    bgRelativeLayout.setBackground(new BitmapDrawable(getResources(), picLoginBitmap));
                } else {
                    bgRelativeLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), picLoginBitmap));
                }
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                if (android.os.Build.VERSION.SDK_INT > 15) {
                    bgRelativeLayout.setBackground(new BitmapDrawable(getResources(), picLoginBitmap));
                } else {
                    bgRelativeLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), picLoginBitmap));
                }
            }
        });
        }
    }
}
