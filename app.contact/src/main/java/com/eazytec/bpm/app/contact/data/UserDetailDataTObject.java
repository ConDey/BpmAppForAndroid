package com.eazytec.bpm.app.contact.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * Created by Administrator on 2017/7/3.
 */

public class UserDetailDataTObject extends WebDataTObject implements Parcelable {

    private String id;

    private String username;

    private String email;

    private String mobile;

    private String fullName;

    private String departmentId;

    private String departmentName;

    private String position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    // Parcelable 实现接口

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(username);
        out.writeString(email);
        out.writeString(mobile);
        out.writeString(fullName);
        out.writeString(departmentId);
        out.writeString(departmentName);
        out.writeString(position);
    }

    public static final Parcelable.Creator<UserDetailDataTObject> CREATOR = new Parcelable.Creator<UserDetailDataTObject>() {
        public UserDetailDataTObject createFromParcel(Parcel in) {
            return new UserDetailDataTObject(in);
        }

        public UserDetailDataTObject[] newArray(int size) {
            return new UserDetailDataTObject[size];
        }
    };

    private UserDetailDataTObject(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        mobile = in.readString();
        fullName = in.readString();
        departmentId = in.readString();
        departmentName = in.readString();
        position = in.readString();
    }

    public UserDetailDataTObject() {
    }
}


