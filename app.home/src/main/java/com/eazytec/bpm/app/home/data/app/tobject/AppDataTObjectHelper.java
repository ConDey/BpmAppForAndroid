package com.eazytec.bpm.app.home.data.app.tobject;

import com.eazytec.bpm.app.home.data.app.BPMApp;
import com.eazytec.bpm.app.home.data.authenication.AuthenticationDataTObject;
import com.eazytec.bpm.lib.common.authentication.Token;
import com.eazytec.bpm.lib.common.authentication.UserAuthority;
import com.eazytec.bpm.lib.common.authentication.UserDetails;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * APP帮助类
 *
 * @author ConDey
 * @version Id: AppDataTObjectHelper, v 0.1 2017/6/3 下午1:32 ConDey Exp $$
 */
public abstract class AppDataTObjectHelper {

    /**
     * @param tObjects
     * @return
     */
    public static List<BPMApp> createBpmAppsByTObjects(List<AppDataTObject> tObjects) {

        List<BPMApp> bpmApps = new ArrayList<>();
        if (tObjects != null && tObjects.size() > 0) {

            Collections.sort(tObjects);
            for (AppDataTObject object : tObjects) {
                BPMApp app = createBpmAppByTObject(object);
                if (app != null) {
                    bpmApps.add(app);
                }
            }
        }
        return bpmApps;
    }


    /**
     * 根据AppDataTObject创建BPMApp
     * <p>
     * 如果有数据错误，则跳过此bpmapp的创建
     *
     * @param object
     * @return
     */
    private static BPMApp createBpmAppByTObject(AppDataTObject object) {


        try {
            BPMApp bpmapp = new BPMApp();
            bpmapp.setId(object.getId());
            bpmapp.setPackageName(object.getPackageName());
            bpmapp.setName(object.getName());
            bpmapp.setDisplayName(object.getDiplayName());
            bpmapp.setImageUrl(object.getImageUrl());
            bpmapp.setBundleName(object.getBundleName());


            if (object.getImageUrlType().equals("1")) {
                bpmapp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_INNER);
            } else {
                bpmapp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_REMOTE);
            }

            if (object.getType().equals("1")) {
                bpmapp.setType(BPMApp.APP_TYPE_INNER);
            } else {
                bpmapp.setType(BPMApp.APP_TYPE_WEB);
            }
            return bpmapp;

        } catch (Exception e) {

        }
        return null;
    }
}
