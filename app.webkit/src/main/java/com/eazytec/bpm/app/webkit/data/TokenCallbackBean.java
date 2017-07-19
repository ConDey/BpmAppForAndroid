package com.eazytec.bpm.app.webkit.data;

import com.eazytec.bpm.lib.common.authentication.UserDetails;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * @version Id: UserCallbackBean, v 0.1 2017-7-18 15:30 16735 Exp $$
 * @auther 16735
 */
public class TokenCallbackBean extends BaseCallbackBean {

    public final String TOKEN = "token";

    private String token;

    public TokenCallbackBean(boolean isSuccess, String errorMsg, String token) {
        setSuccess(isSuccess);
        setErrorMsg(errorMsg);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HashMap<String, Object> toJson() {
        HashMap<String, Object> hashMap = new HashMap<>();
        boolean isSuccess = getSuccess()?true:false;
        hashMap.put(SUCCESS, isSuccess );
        hashMap.put(ERRORMSG, getErrorMsg());
        hashMap.put(TOKEN, token);

        return hashMap;
    }
}
