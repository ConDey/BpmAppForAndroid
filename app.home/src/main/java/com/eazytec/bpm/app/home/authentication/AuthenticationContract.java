package com.eazytec.bpm.app.home.authentication;

import android.content.Context;

import com.eazytec.bpm.app.home.data.authenication.AuthenticationDataTObject;
import com.eazytec.bpm.app.home.data.commonconfig.ImgDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author ConDey
 * @version Id: UserLoginContract, v 0.1 2017/6/2 下午3:44 ConDey Exp $$
 */
public interface AuthenticationContract {

    interface View extends CommonContract.CommonView {

        // 跳转页面成功后执行的方法
        void loginSuccess(AuthenticationDataTObject loginData);

        void getImgUrl(ImgDataTObject imgDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void userlogin(Context context, String username, String password);

        void commonconfig();
    }
}
