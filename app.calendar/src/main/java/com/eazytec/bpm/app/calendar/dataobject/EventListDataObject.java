package com.eazytec.bpm.app.calendar.dataobject;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import java.util.List;

/**
 * Created by Vivi on 2017/9/27.
 */

public class EventListDataObject extends WebDataTObject{

    private String startTime;

    private String id;

    private String startDate;

    private String location;

    private String description;

    private String endDate;

    private String eventType;

    private String eventName;

    private String endTime;

    private String eventTypeName;

    private List<EventListDataObject> datas;

    public List<EventListDataObject> getDatas() {
        return datas;
    }

    public void setDatas(List<EventListDataObject> datas) {
        this.datas = datas;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }
}
