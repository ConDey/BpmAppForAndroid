package com.eazytec.bpm.app.home.data.commonconfig;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * @author Beckett_W
 * @version Id: ImgDataTObject, v 0.1 2017/9/20 9:05 Beckett_W Exp $$
 */
public class ImgDataTObject extends WebDataTObject {

    private String loginBackgroundImg;

    private String appBackgroundImg;

    public String getLoginBackgroundImg() {
        return loginBackgroundImg;
    }

    public void setLoginBackgroundImg(String loginBackgroundImg) {
        this.loginBackgroundImg = loginBackgroundImg;
    }

    public String getAppBackgroundImg() {
        return appBackgroundImg;
    }

    public void setAppBackgroundImg(String appBackgroundImg) {
        this.appBackgroundImg = appBackgroundImg;
    }
}
