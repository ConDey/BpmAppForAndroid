package com.eazytec.bpm.app.home.data.app;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import java.util.Map;

/**
 * @author Beckett_W
 * @version Id: AppIconDataTObject, v 0.1 2017/11/14 13:10 Beckett_W Exp $$
 */
public class AppIconDataTObject extends WebDataTObject{

    private Map<String,Object> datas;

    public Map<String, Object> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }
}
