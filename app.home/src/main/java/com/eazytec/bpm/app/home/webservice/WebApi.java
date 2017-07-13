package com.eazytec.bpm.app.home.webservice;

import com.eazytec.bpm.app.home.data.authenication.AuthenticationDataTObject;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author ConDey
 * @version Id: WebApi, v 0.1 2017/6/29 下午1:17 ConDey Exp $$
 */
public interface WebApi {

    /**
     * 用户登录Api
     *
     * @param username 用户名
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("common/logon")
    Observable<AuthenticationDataTObject> authentication(@Header("token") String token, @Field("username") String username, @Field("password") String password);


    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @FormUrlEncoded
    @POST("password/change")
    Observable<WebDataTObject> updatePwd(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword, @Field("confirmPassword") String confirmPassword);


}
