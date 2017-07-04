package com.eazytec.bpm.app.contact.webservice;

import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.data.UsersDataTObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/3.
 */

public interface WebApi {

    /**
     * 加载部门和用户列表
     *
     */
    @GET("department/listByParentId")
    Observable<DepartmentDataTObject> loadDepartmentsAndUsers(@Query("parentId") String parentId);

    /**
     * 搜索用户
     *
     * @param name
     * @return
     */
    @GET("user/list")
    Observable<UsersDataTObject> loadUserSearch(@Query("name") String name);

    /**
     * 根据用户ID加载用户详情
     *
     * @param userId
     * @return
     */
    @GET("user/detail")
    Observable<UserDetailDataTObject> loadUserDetail(@Query("userId") String userId);




}
