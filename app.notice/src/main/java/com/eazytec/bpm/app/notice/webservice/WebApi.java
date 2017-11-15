package com.eazytec.bpm.app.notice.webservice;

import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.app.notice.data.NoticeListDataTObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Beckett_W
 * @version Id: WebApi, v 0.1 2017/7/10 8:45 Administrator Exp $$
 */
public interface WebApi {

    @GET("notice/list")
    public Observable<NoticeListDataTObject> loadNoticeList(@Query("title") String title, @Query("pageNo") String pageNo, @Query("pageSize") String pageSize);

    @GET("notice/detail")
    public Observable<NoticeDetailDataTObject> loadNoticeDetail(@Query("noticeId") String noticeId);
}
