package com.eazytec.bpm.app.home.data.app.tobject;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import java.util.List;

/**
 * 应用数据
 *
 * @author ConDey
 * @version Id: AppsDataTObject, v 0.1 2017/6/2 下午1:53 ConDey Exp $$
 */
public class AppsDataTObject extends WebDataTObject {

    private List<AppDataTObject> apps;

    public List<AppDataTObject> getApps() {
        return apps;
    }

    public void setApps(List<AppDataTObject> apps) {
        this.apps = apps;
    }
}
