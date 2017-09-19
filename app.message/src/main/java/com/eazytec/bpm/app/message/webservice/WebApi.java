package com.eazytec.bpm.app.message.webservice;

import com.eazytec.bpm.lib.common.message.dataobject.MessageListDataTObject;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Beckett_W
 * @version Id: WebApi, v 0.1 2017/9/18 13:05 Beckett_W Exp $$
 */
public interface WebApi {

    /**
     * 消息主题详细
     *
     * @param id
     * @return
     */
    @GET("msg/topic/detail")
    Observable<MessageTopicDataTObject> loadTopicById(@Query("id") String id);

    /**
     * 获取消息列表
     *
     * @param date
     * @return
     */
    @GET("msg/list")
    Observable<MessageListDataTObject> loadMessages(@Query("date") String date);

}
