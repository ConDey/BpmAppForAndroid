package com.eazytec.bpm.app.webkit.data;

import com.eazytec.bpm.lib.common.authentication.UserDetails;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * @version Id: UserCallbackBean, v 0.1 2017-7-18 15:30 16735 Exp $$
 * @auther 16735
 */
public class UserCallbackBean extends BaseCallbackBean {

    public final String DATAS = "datas";

    private UserDetails user;

    public UserCallbackBean(boolean isSuccess, String errorMsg, UserDetails datas) {
        setSuccess(isSuccess);
        setErrorMsg(errorMsg);
        if (datas != null) {
            this.user = datas;
        }
    }

    public UserDetails getDatas() {
        return user;
    }

    public void setDatas(UserDetails user) {
        this.user = user;
    }

    public HashMap<String, String> toJson() {
        String userStr = "";
        HashMap<String, String> hashMap = new HashMap<>();
        if (user != null) {
            Gson gson = new Gson();
            userStr = gson.toJson(user);
            hashMap.put(DATAS, userStr);
        }
        hashMap.put(ERRORMSG, getErrorMsg());
        String isSuccess = getSuccess()?"true":"false";
        hashMap.put(SUCCESS, isSuccess );

        return hashMap;
    }
}
