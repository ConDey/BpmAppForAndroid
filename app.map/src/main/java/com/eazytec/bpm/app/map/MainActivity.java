package com.eazytec.bpm.app.map;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.eazytec.bpm.lib.common.activity.CommonActivity;

public class MainActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private MapView mMapView;

    private LocationClient mLocationClient;
    // 是否第一次定位
    private boolean isFirstLocation = true;
    private BaiduMap mBaiduMap;

    private BitmapDescriptor bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("我的位置");

        mMapView = (MapView) findViewById(R.id.activity_baidu_map_view);

        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_loc_mark);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        initMap();
        initLocation();
    }

    // 初始化地图
    private void initMap(){
        // 不显示缩放比例尺
        mMapView.showZoomControls(false);
        // 不显示百度地图logo
        mMapView.removeViewAt(1);
        // 百度地图
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMapView.showZoomControls(false);
            }
        });

        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setAllGesturesEnabled(true); // 允许所有手势交互

        mBaiduMap.setMyLocationConfigeration(LocationUtil.getDefaultConfiguration());
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);

    }

    private void initLocation(){
        // 定位客户端的设置
        mLocationClient = LocationUtil.initLocationClient(this, new MyLocationListener());
        mLocationClient.start();
    }

    /**
     *  定位监听程序
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (mBaiduMap == null) {
                return; // 防止出现内存泄露
            }
            LatLng nowLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder().accuracy(0).latitude(nowLatLng.latitude).longitude(nowLatLng.longitude).direction(location.getDirection()).build();
            mBaiduMap.setMyLocationData(locData);
            if(isFirstLocation) {
                mBaiduMap.setMapStatus(BaiduMapUtil.createMapStatusUpdate(nowLatLng, BaiduMapUtil.HIGH_ZOOM));

                // 修改定位点图案
                OverlayOptions option = new MarkerOptions().position(nowLatLng).icon(bitmap);
                mBaiduMap.addOverlay(option);

                isFirstLocation = false;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
    }
}
