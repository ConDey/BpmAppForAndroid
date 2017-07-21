package com.eazytec.bpm.app.webkit.data;

import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.HashMap;
import java.util.List;

/**
 * @author 16735
 * @version Id: MediaCallbackBean, v 0.1 2017-7-20 14:11 16735 Exp $$
 */
public class MediaCallbackBean extends BaseCallbackBean {

    private final String LIST = "list";

    private List<LocalMedia> list;

    public MediaCallbackBean(boolean isSuccess, String errorMsg, List<LocalMedia> list) {
        setSuccess(isSuccess);
        setErrorMsg(errorMsg);
        this.list = list;
    }

    public List<LocalMedia> getList() {
        return list;
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public HashMap<String, Object> toJson() {
        StringBuffer lists = new StringBuffer("[");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(ERRORMSG, getErrorMsg());
        boolean isSuccess = getSuccess()?true:false;
        hashMap.put(SUCCESS, isSuccess );

        for (LocalMedia media : list) {
            lists.append(media.getPath());
            lists.append(",");
        }
        String mLists = lists.toString();
        if (mLists.equals("[")) {
            mLists = mLists+"]";
        }else {
            mLists = lists.substring(0, mLists.length()-1) + "]";
        }
        hashMap.put(LIST, mLists);

        return hashMap;
    }
}
