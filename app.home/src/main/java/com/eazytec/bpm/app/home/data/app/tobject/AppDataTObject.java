package com.eazytec.bpm.app.home.data.app.tobject;

import android.support.annotation.NonNull;

/**
 * 远程APP推送数据
 *
 * @author ConDey
 * @version Id: AppDataTObject, v 0.1 2017/6/2 下午1:53 ConDey Exp $$
 */
public class AppDataTObject implements Comparable<AppDataTObject> {

    private String id;

    private String orderNo;

    private String diplayName;

    private String packageName;

    private String imageUrl;

    private String description;

    private String name;

    private String helpText;

    private String imageUrlType;

    private String bundleName;

    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDiplayName() {
        return diplayName;
    }

    public void setDiplayName(String diplayName) {
        this.diplayName = diplayName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getImageUrlType() {
        return imageUrlType;
    }

    public void setImageUrlType(String imageUrlType) {
        this.imageUrlType = imageUrlType;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override public int compareTo(@NonNull AppDataTObject o) {
        int originOrder = Integer.valueOf(this.getOrderNo());
        int appOrder = Integer.valueOf(o.getOrderNo());

        if (originOrder < appOrder)
            return -1;
        if (originOrder > appOrder)
            return 1;
        return 0;
    }
}
