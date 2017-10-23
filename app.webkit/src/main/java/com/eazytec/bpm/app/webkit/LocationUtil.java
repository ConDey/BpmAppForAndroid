package com.eazytec.bpm.app.webkit;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;

/**
 * @author Beckett_W
 * @version Id: LocationUtil, v 0.1 2017/10/23 13:58 Beckett_W Exp $$
 */
public class LocationUtil {

    public static String COOR_TYPE = "bd09ll"; // 设置坐标类型
    public static int SCAN_SPAN = 10000; // 定位间隔
    public static float DEFAULT_ACCURACY = 500; // 默认精度范围(显示用)

    /**
     * 初始化定位客户端
     *
     * @param context
     * @param listener
     * @return
     */
    public static LocationClient initLocationClient(Context context, BDLocationListener listener) {
        return initLocationClient(context, listener, SCAN_SPAN);
    }

    public static LocationClient initLocationClient(Context context, BDLocationListener listener, int scanSpan) {

        LocationClient locationClient = new LocationClient(context);
        locationClient.registerLocationListener(listener);
        locationClient.setLocOption(LocationUtil.getDefaultLocationClientOption(scanSpan));
        locationClient.requestLocation();
        return locationClient;
    }

    /**
     * 生成默认的LocationClientOption
     */
    public static LocationClientOption getDefaultLocationClientOption(int scanSpan) {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType(COOR_TYPE);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(scanSpan);
        option.disableCache(true);   //禁止启用缓存定位
        option.setLocationNotify(false);
        return option;
    }


    /**
     * 获取默认的定位配置
     *
     * @return
     */
    public static MyLocationConfiguration getDefaultConfiguration() {
        BitmapDescriptor mCurrentMarker = null; // 自定义图标,这里没有自定义图标则写入null
        return new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
    }


    /**
     * 判断两个坐标是否相等
     *
     * @param latLng1
     * @param latLng2
     * @return
     */
    public static boolean isLatLngEqual(LatLng latLng1, LatLng latLng2) {
        if (latLng1 == null && latLng2 == null) {
            return true;
        } else if (latLng1 == null || latLng2 == null) {
            return false;
        } else {
            return latLng1.latitude == latLng2.latitude && latLng1.longitude == latLng2.longitude ? true : false;
        }
    }
}
