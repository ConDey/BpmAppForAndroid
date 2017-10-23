package com.eazytec.bpm.app.webkit.data;

import java.util.HashMap;
import java.util.List;

/**
 * @author Beckett_W
 * @version Id: LocationCallbackBean, v 0.1 2017/10/23 14:27 Beckett_W Exp $$
 */
public class LocationCallbackBean extends BaseCallbackBean {

    private String latitude;

    private String longitude;

    public LocationCallbackBean(boolean isSuccess, String errorMsg, String latitude,String longitude ) {
        setSuccess(isSuccess);
        setErrorMsg(errorMsg);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public HashMap<String, Object> toJson() {
        HashMap<String, Object> hashMap = new HashMap<>();
        boolean isSuccess = getSuccess()?true:false;
        hashMap.put(SUCCESS, isSuccess );
        hashMap.put(ERRORMSG, getErrorMsg());
        hashMap.put("latitude", latitude);
        hashMap.put("longitude", longitude);
        return hashMap;
    }
}
