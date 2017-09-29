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
    @GET("schedule/list")
    Observable<EventListDataObject> loadItInfo(@Query("datas") String datas);

    /*@GET("schedule/detail")
    Observable<EventListDataObject> loadItStartTime(@Query("startTime") String startTime);
    Observable<EventListDataObject> loadItStartData(@Query("startData") String startData);
    Observable<EventListDataObject> loadItEndTime(@Query("endTime") String endTime);
    Observable<EventListDataObject> loadItEndData(@Query("endData") String endData);
    Observable<EventListDataObject> loadItDescription(@Query("description") String description);
    Observable<EventListDataObject> loadItLocation(@Query("location") String location);
    Observable<EventListDataObject> loadItEventName(@Query("eventName") String eventName);
    Observable<EventListDataObject> loadItEventType(@Query("eventType") String eventType);
    Observable<EventListDataObject> loadItId(@Query("id") String id);
*/
    /**
     * 获得事件详情
     */
    @GET("schedule/detail")
    Observable<EventDetailDataObject> getEventDetail(@Query("id") String evenId);

    /**
     * 事件更新/保存
     */
    @GET("schedule/save")
    Observable<WebDataTObject>  saveDetail(@Query("startTime") String startTime, @Query("startDate") String startData, @Query("endTime") String endTime,
                                           @Query("endDate") String endData, @Query("description") String description, @Query("location") String location,
                                           @Query("eventName") String eventName, @Query("eventType") String eventType, @Query("id") String id);

    @GET("schedule/delete")
    Observable<WebDataTObject> deleteDetail(@Query("id") String evenId);

    @GET("schedule/typeList")
    Observable<EventTypeObject> getEventType();
}
