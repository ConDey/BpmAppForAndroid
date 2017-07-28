package com.eazytec.bpm.app.webkit.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 16735
 * @version Id: UserChooseCallbackBean, v 0.1 2017-7-20 19:43 16735 Exp $$
 */
public class UserChooseCallbackBean extends BaseCallbackBean {

    private List<HashMap<String, String>> datas;

    public UserChooseCallbackBean(boolean isSuccess, String errorMsg, List<HashMap<String, String>> datas) {
        setSuccess(isSuccess);
        setErrorMsg(errorMsg);
        this.datas = datas;
    }

    public HashMap<String, Object> toJson() {
        StringBuffer lists = new StringBuffer("[");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(ERRORMSG, getErrorMsg());
        boolean isSuccess = getSuccess() ? true : false;
        hashMap.put(SUCCESS, isSuccess);
        hashMap.put("users", datas);
        return hashMap;
    }
}
