package com.eazytec.bpm.lib.common.webservice;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscription;

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

    /**
     * 通用的文件上传器
     *
     * @param file
     * @param body
     * @return
     */

    @POST("attachment/upload")
    Observable<UploadFileDataTObject> upload(@Header("fileName") String file, @Body RequestBody body);
}
