package com.eazytec.bpm.app.home.data;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * 附件
 * @author Administrator
 * @version Id: AttachmentsDataTObject, v 0.1 2017/7/10 9:41 Administrator Exp $$
 */
public class AttachmentsDataTObject extends WebDataTObject {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

