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
import com.eazytec.bpm.lib.utils.StringUtils;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import rx.functions.Action1;

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

        // 初始化APP数据，在没有插件管理平台的情况下，这里先写死
        bpmApps = new ArrayList<>();

        // 通讯录模块
        BPMApp contactBpmApp = new BPMApp();
        contactBpmApp.setId("com.eazytec.bpm.app.contact");
        contactBpmApp.setPackageName("com.eazytec.bpm.app.contact");
        contactBpmApp.setDisplayName("通讯录");
        contactBpmApp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_INNER);
        contactBpmApp.setImageUrl("ic_homeapp_card");
        contactBpmApp.setType(BPMApp.APP_TYPE_INNER);
        contactBpmApp.setBundleName("app.contact");
        bpmApps.add(contactBpmApp);

        // 通知公告模块
        BPMApp noticeBpmApp = new BPMApp();
        noticeBpmApp.setId("com.eazytec.bpm.app.notice");
        noticeBpmApp.setPackageName("com.eazytec.bpm.app.notice");
        noticeBpmApp.setDisplayName("公告");
        noticeBpmApp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_INNER);
        noticeBpmApp.setImageUrl("ic_homeapp_notice");
        noticeBpmApp.setType(BPMApp.APP_TYPE_INNER);
        noticeBpmApp.setBundleName("app.notice");
        bpmApps.add(noticeBpmApp);


        // JS测试模块
        BPMApp jsTestBpmApp = new BPMApp();
        jsTestBpmApp.setId("com.eazytec.bpm.app.webkit");
        jsTestBpmApp.setPackageName("com.eazytec.bpm.app.webkit");
        jsTestBpmApp.setDisplayName("JSWEB");
        jsTestBpmApp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_INNER);
        jsTestBpmApp.setImageUrl("ic_homeapp_process");
        jsTestBpmApp.setType(BPMApp.APP_TYPE_WEB);
        jsTestBpmApp.setBundleName(WebViewUtil.getLocalHTMLUrl("jswebview.html"));
        bpmApps.add(jsTestBpmApp);

        homeAppAdapter.setItems(bpmApps);
        homeAppAdapter.notifyDataSetChanged();
    }


}
