package com.eazytec.bpm.app.contact.data;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class UsersDataTObject extends WebDataTObject {

    private List<UserDetailDataTObject> datas;

    public List<UserDetailDataTObject> getDatas() {
        return datas;
    }

    public void setDatas(List<UserDetailDataTObject> datas) {
        this.datas = datas;
    }
}