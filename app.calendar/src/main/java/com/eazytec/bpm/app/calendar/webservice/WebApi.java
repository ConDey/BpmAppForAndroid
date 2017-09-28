package com.eazytec.bpm.app.calendar.webservice;

import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;

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

}
