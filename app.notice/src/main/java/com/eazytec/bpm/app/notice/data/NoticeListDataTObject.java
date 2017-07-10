package com.eazytec.bpm.app.notice.data;

import com.eazytec.bpm.lib.common.webservice.PageDataTObject;

import java.util.List;

/**
 * @author Administrator
 * @version Id: NoticeListDataTObject, v 0.1 2017/7/10 9:39 Administrator Exp $$
 */
public class NoticeListDataTObject extends PageDataTObject {

    private List<NoticeDetailDataTObject> datas;

    public List<NoticeDetailDataTObject> getDatas() {
        return datas;
    }

    public void setDatas(List<NoticeDetailDataTObject> datas) {
        this.datas = datas;
    }
}

