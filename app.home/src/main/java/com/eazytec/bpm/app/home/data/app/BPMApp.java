package com.eazytec.bpm.app.home.data.app;

import android.content.Context;
import android.support.annotation.StringDef;

import com.eazytec.bpm.lib.utils.EncodeUtils;
import com.eazytec.bpm.lib.utils.StringUtils;

import net.wequick.small.Small;

/**
 * BPM框架下的App应用
 * <p>
 * <p>
 * <ul>
 * <li>1.一个App只能对应唯一Bundle,但是一个Bundle能对应多个App</li>
 * <li>2.App应用分为内置，远程和Web</li>
 * </ul>
 *
 * @author ConDey
 * @version Id: BPMApp, v 0.1 2017/7/1 上午10:04 ConDey Exp $$
 */
public class BPMApp implements Installable {

    /**
     * 应用唯一标识，在BPMApp系统中，一个应用需要有唯一的标识
     */
    private String id;

    /**
     * 应用名称，用于描述一个应用
     */
    private String name;


    /**
     * 应用的显示名称
     */
    private String displayName;

    /**
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 图片路径
     */
    private String imageUrl;

    /**
     * 获取显示图片路径
     *
     * @return
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置显示图片路径
     *
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 图片本地路径类型，如果是INNER代表此图片是系统预设的图片,只需要通过FILE-URL来访问就可以了
     */
    public static final String IMAGE_URL_TYPE_INNER = "IMAGE_URL_TYPE_INNER";

    /**
     * 图片远程路径类型，如果是REMOTE代表此图片需要先下载后显示
     */
    public static final String IMAGE_URL_TYPE_REMOTE = "IMAGE_URL_TYPE_REMOTE";

    // 自定义一个注解MyState
    @StringDef({IMAGE_URL_TYPE_INNER, IMAGE_URL_TYPE_REMOTE})
    public @interface IMAGE_URL_TYPE {
    }

    /**
     * 图片路径类型
     */
    @IMAGE_URL_TYPE
    private String imageUrlType;

    /**
     * 获取图片路径类型
     *
     * @return
     */
    @IMAGE_URL_TYPE
    public String getImageUrlType() {
        return imageUrlType;
    }

    /**
     * 设置图片路径类型
     *
     * @param imageUrlType
     */
    public void setImageUrlType(@IMAGE_URL_TYPE String imageUrlType) {
        this.imageUrlType = imageUrlType;
    }

    /**
     * App类型代表内置应用，如果此应用是内置应用，被预装在整体宿主应用之中
     * <p>
     * 不需要预安装就可以使用
     * 一般来说这样的应用都是此业务环境下的专用应用
     */
    public static final String APP_TYPE_INNER = "APP_TYPE_INNER";


    /**
     * App类型代表远程应用，如果此应用是远程应用，则代表应用需要被远程安装才可以使用
     */
    public static final String APP_TYPE_REMOTE = "APP_TYPE_REMOTE";

    /**
     * WEB应用，WEB应用特指可以需要远程加载HTML的应用，此类应用不需要被插件化，由small.webview渲染并显示
     */
    public static final String APP_TYPE_WEB = "APP_TYPE_WEB";

    // 自定义一个注解MyState
    @StringDef({APP_TYPE_INNER, APP_TYPE_REMOTE, APP_TYPE_WEB})
    public @interface BPM_APP_TYPE {
    }

    /**
     * 应用类型，使用BPM_APP_TYPE枚举
     */
    @BPM_APP_TYPE
    private String type;

    /**
     * 获取应用类型
     *
     * @return
     */
    @BPM_APP_TYPE
    public String getType() {
        return type;
    }

    /**
     * 设置应用类型
     *
     * @param type 应用类型
     */
    public void setType(@BPM_APP_TYPE String type) {
        this.type = type;
    }

    /**
     * 获取应用标识符
     *
     * @return 应用标识符
     */
    public String getId() {
        return id;
    }

    /**
     * 设置应用标识符
     *
     * @param id 应用标识符
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取应用显示名称
     *
     * @return 应用名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置应用显示名称
     *
     * @param name 应用名称
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * BPMAPP 应用跳转的Bundle信息, 这个BundleName可以是一个插件也可以是
     * 一个路由
     */
    private String bundleName;


    /**
     * 获取插件所在Bundle的ID
     *
     * @return
     */
    public String getBundleName() {
        return bundleName;
    }

    /**
     * 设置Bundle的名称
     *
     * @param bundleName
     */
    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }


    /**
     * Bundle的packageName, Small用于判断Bundle的唯一性
     */
    private String packageName;


    /**
     * 获取packageName
     *
     * @return
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置packageName
     *
     * @param packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 返回插件或应用是否被安装
     *
     * @return
     */
    @Override public boolean installed() {
        if (StringUtils.equals(getType(), APP_TYPE_WEB)) {
            return true; // 这两个应用不需要安装
        }

        // 主要还是看没有这个Bundle，如果有则代表已经安装
        // 如果没有的话需要从插件库中下载和安装
        if (Small.getBundle(getPackageName()) != null) {
            return true;
        }
        return false;
    }

    /**
     * 安装应用或插件
     *
     * @param context
     */
    @Override public void install(Context context) {

    }

    /**
     * 是否允许安装此插件
     *
     * @return
     */
    @Override public boolean canInstall() {
        if (StringUtils.equals(getType(), APP_TYPE_REMOTE)) {
            // 只有远程应用才需要安装
            return true;
        }
        return false;
    }

    /**
     * 通过Small进入App应用
     *
     * @param context
     */
    public void getIntoApp(Context context) {
        // 进入应用
        // 如果应用没有安装，则方法无效
        if (!installed()) {
            return;
        }

        if (StringUtils.equals(getType(), APP_TYPE_WEB)) {

            String url = EncodeUtils.urlEncode(bundleName).toString();
            Small.openUri("app.webkit?url=" + url + "&title=" + displayName, context);
            return;
        }
        Small.openUri(getBundleName(), context);
    }
}
