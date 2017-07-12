package com.eazytec.bpm.lib.common.webservice;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Administrator
 * @version Id: WebApi, v 0.1 2017/7/12 8:07 Administrator Exp $$
 */
public interface WebApi {
    /**
     * 通用的文件下载器
     *
     * @param id
     * @return
     */

    @GET("attachment/down")
    Observable<ResponseBody> download(@Query("attachmentId") String id);

}
