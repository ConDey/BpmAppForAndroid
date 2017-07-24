package com.eazytec.bpm.app.contact.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * @author Administrator
 * @version Id: BackInfoDataTObject, v 0.1 2017/7/24 13:05 Administrator Exp $$
 */
public class BackInfoDataTObject extends WebDataTObject implements Parcelable {

    private String id;
    private String name;

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

    protected BackInfoDataTObject(Parcel in) {
        id = in.readString();
        name = in.readString();
    }
    public  BackInfoDataTObject(){}


    public static final Creator<BackInfoDataTObject> CREATOR = new Creator<BackInfoDataTObject>() {
        @Override
        public BackInfoDataTObject createFromParcel(Parcel in) {
            return new BackInfoDataTObject(in);
        }

        @Override
        public BackInfoDataTObject[] newArray(int size) {
            return new BackInfoDataTObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(name);
    }
}

