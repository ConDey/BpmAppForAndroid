package com.eazytec.bpm.app.calendar.dataobject;

import java.util.List;

/**
 * Created by Vivi on 2017/9/29.
 */

public class EventTypeObject {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EventTypeObject> getDatas() {
        return datas;
    }

    public void setDatas(List<EventTypeObject> datas) {
        this.datas = datas;
    }

    private String code;
    private String name;
    private List<EventTypeObject> datas;
}
