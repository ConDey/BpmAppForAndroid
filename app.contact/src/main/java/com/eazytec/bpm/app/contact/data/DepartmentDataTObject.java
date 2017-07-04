package com.eazytec.bpm.app.contact.data;

import android.support.annotation.NonNull;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class DepartmentDataTObject extends WebDataTObject implements Comparable<DepartmentDataTObject> {

    private String id;

    private String name;

    private String order;

    private int childCount;

    private int userCount;

    private List<DepartmentDataTObject> childs;

    private List<UserDetailDataTObject> users;

    @Override public int compareTo(@NonNull DepartmentDataTObject o) {
        if (StringUtils.isEmpty(this.order)) {
            return -1;
        }

        if (StringUtils.isEmpty(o.getOrder())) {
            return 1;
        }

        if (Integer.valueOf(this.order) < Integer.valueOf(o.getOrder())) {
            return -1;
        } else if (Integer.valueOf(this.order) > Integer.valueOf(o.getOrder())) {
            return 1;
        }
        return 0;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<DepartmentDataTObject> getChilds() {
        return childs;
    }

    public void setChilds(List<DepartmentDataTObject> childs) {
        this.childs = childs;
    }

    public List<UserDetailDataTObject> getUsers() {
        return users;
    }

    public void setUsers(List<UserDetailDataTObject> users) {
        this.users = users;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}


