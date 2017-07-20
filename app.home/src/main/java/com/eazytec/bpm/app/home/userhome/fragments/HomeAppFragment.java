package com.eazytec.bpm.app.home.userhome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.app.BPMApp;
import com.eazytec.bpm.app.home.userhome.adapters.HomeAppAdapter;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.gridview.SingleDividerGridView;
import com.eazytec.bpm.lib.common.fragment.CommonFragment;
import com.eazytec.bpm.lib.common.webkit.WebViewUtil;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.eazytec.bpm.app.home.data.app.BPMApp.APP_TYPE_REMOTE;

/**
 * APP列表页的Fragment
 *
 * @author ConDey
 * @version Id: HomeAppFragment, v 0.1 2017/6/30 上午9:06 ConDey Exp $$
 */
public class HomeAppFragment extends CommonFragment {

    private SingleDividerGridView appGridView;
    private HomeAppAdapter homeAppAdapter;

    private List<BPMApp> bpmApps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_homeapp, container, false);

        appGridView = (SingleDividerGridView) parentView.findViewById(R.id.gv_homeapp);

        // 设置AppGridView的适配器
        homeAppAdapter = new HomeAppAdapter(getContext());
        appGridView.setAdapter(homeAppAdapter);

        RxAdapterView.itemClicks(appGridView).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                BPMApp bpmApp = bpmApps.get(integer);
                // 在Debug模式下应用肯定都是没有安装的
                if (bpmApp.installed()) {
                    bpmApp.getIntoApp(getContext());
                } else {
                    ToastDelegate.warning(getContext(), getString(R.string.userhome_app_is_not_installed));
                }
            }
        });


        return parentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeAppAdapter.setItems(getBpmApps());
        homeAppAdapter.notifyDataSetChanged();
    }

    private List<BPMApp> getBpmApps() {
        List<BPMApp> bpmApps = new ArrayList<>();

        try {
            InputStreamReader isr = new InputStreamReader(getCommonActivity().getAssets().open("app.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
            JSONArray array = testjson.getJSONArray("apps");         //从JSONObject中取出数组对象
            for (int i = 0; i < array.length(); i++) {
                JSONObject appobject = array.getJSONObject(i);    //取出数组中的对象

                BPMApp bpmApp = new BPMApp();
                bpmApp.setId(appobject.getString("id"));
                bpmApp.setPackageName(appobject.getString("packagename"));
                bpmApp.setName(appobject.getString("name"));
                bpmApp.setDisplayName(appobject.getString("diplayname"));

                String imgUrlType = appobject.getString("imageurltype");

                if (imgUrlType.equals("IMAGE_URL_TYPE_INNER")) {
                    bpmApp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_INNER);
                } else if (imgUrlType.equals("IMAGE_URL_TYPE_REMOTE")) {
                    bpmApp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_REMOTE);
                }

                bpmApp.setImageUrl(appobject.getString("imageurl"));

                String type = appobject.getString("type");

                if (type.equals("APP_TYPE_INNER")) {
                    bpmApp.setType(BPMApp.APP_TYPE_INNER);
                } else if (type.equals("APP_TYPE_REMOTE")) {
                    bpmApp.setType(BPMApp.APP_TYPE_REMOTE);
                } else {
                    bpmApp.setType(BPMApp.APP_TYPE_WEB);
                }
                bpmApp.setBundleName(appobject.getString("bundlename"));
                bpmApps.add(bpmApp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bpmApps;
    }


}
