package com.eazytec.bpm.lib.common.bundle;

import android.app.Application;

import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.DebugMockConfig;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.authentication.Token;
import com.eazytec.bpm.lib.common.authentication.UserAuthority;
import com.eazytec.bpm.lib.common.authentication.UserDetails;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.Utils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Bundle所属的Application,所有的App.Bundle必须继承此类，如果没有的继承的没有办法调试
 *
 * @author ConDey
 * @version Id: BundleApplication, v 0.1 2017/6/29 下午3:04 ConDey Exp $$
 */
public class BundleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (isDebug()) {
            Utils.init(this);
            // mock 登陆过程
            new Thread(mockAuthenticationTask).start();

        }
    }

    /**
     * 登陆线程，因为网络请求不能在主线程上
     */
    Runnable mockAuthenticationTask = new Runnable() {
        @Override
        public void run() {
            mockAuthentication();
        }
    };

    /**
     * 模拟一下登录，构造Token
     * <p>
     * 在调试环境下运行，非调试环境下这段代码不会被执行
     */
    private void mockAuthentication() {

        // 模拟一下登录，构造Token
        CurrentUser currentUser = CurrentUser.getCurrentUser();

        RequestBody body = new FormBody.Builder()
                .add("username", DebugMockConfig.USERNAME)
                .add("password", DebugMockConfig.PASSWORD)
                .build();

        Request request = new Request.Builder()
                .url(Config.WEB_SERVICE_URL + "common/logon")
                .header("token", DebugMockConfig.DEFAULT_TOKEN)
                .post(body).build();

        Call call = BPMRetrofit.okHttpClient().newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();

            JSONObject authenticationMsg = new JSONObject(result);

            UserDetails userDetails = new UserDetails();
            userDetails.setUsername(authenticationMsg.getString("username"));
            userDetails.setPassword(authenticationMsg.getString("password"));
            userDetails.setFullName(authenticationMsg.getString("fullName"));
            userDetails.setDepartmentName(authenticationMsg.getString("departmentName"));
            userDetails.setEmail(authenticationMsg.getString("email"));
            userDetails.setMobile(authenticationMsg.getString("mobile"));
            userDetails.setPosition(authenticationMsg.getString("position"));

            UserAuthority userAuthority = new UserAuthority();
            if (!StringUtils.isSpace(authenticationMsg.getString("roles"))) {
                String[] roleArrays = authenticationMsg.getString("roles").split(",");
                userAuthority.setAuthorities(new HashSet(Arrays.asList(roleArrays)));
            }

            Token token = new Token(authenticationMsg.getString("token"));
            currentUser.updateCurrentUser(userDetails, userAuthority, token);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isDebug() {
        return Config.IS_DEBUG;
    }
}
