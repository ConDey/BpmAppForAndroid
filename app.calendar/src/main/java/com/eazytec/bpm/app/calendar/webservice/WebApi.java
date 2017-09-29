package com.eazytec.bpm.app.calendar.webservice;

import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventTypeObject;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Vivi on 2017/9/27.
 */

public interface WebApi {

    /**
     * 获得事件列表
     * @param date
     * @return
     */
    @GET("schedule/list")
    Observable<EventListDataObject> loadScheduleList(@Query("date") String date);

    /**
     * 获得事件详情
     */
    @GET("schedule/detail")
    Observable<EventDetailDataObject> getEventDetail(@Query("id") String id);

    /**
     * 事件更新/保存
     */
    @GET("schedule/save")
    Observable<WebDataTObject>  saveDetail(@Query("startTime") String startTime, @Query("startDate") String startData, @Query("endTime") String endTime,
                                           @Query("endDate") String endData, @Query("description") String description, @Query("location") String location,
                                           @Query("eventName") String eventName, @Query("eventType") String eventType, @Query("id") String id);

    @GET("schedule/delete")
    Observable<WebDataTObject> deleteDetail(@Query("id") String evenId);

    //下拉类型
    @GET("schedule/typeList")
    Observable<EventTypeObject> getEventType();
}
