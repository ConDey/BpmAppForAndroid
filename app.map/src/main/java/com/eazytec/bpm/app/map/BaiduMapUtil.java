package com.eazytec.bpm.app.map;

import android.graphics.Point;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;

/**
 * @author Beckett_W
 * @version Id: BaiduMapUtil, v 0.1 2017/10/23 15:44 Beckett_W Exp $$
 */
public class BaiduMapUtil {

    public static float DEFAULT_ZOOM = 14.0f; // 地图默认的精度
    public static float HIGH_ZOOM = 16.0f; // 高精度
    public static float HIGHEST_ZOOM = 18.0f; // 最高精度

    public static Point getDefaultScaleControlPosition() {
        return new Point(15, 10);
    }

    public static Point getDefaultZoomControlPosition() {
        return new Point(10, 45);
    }

    public static MapStatusUpdate createMapStatusUpdate(LatLng latLng, float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(zoom);
        return MapStatusUpdateFactory.newMapStatus(builder.build());
    }
}
