package com.eazytec.bpm.app.home.userhome.appsetting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.app.BPMApp;
import com.eazytec.bpm.app.home.data.app.tobject.AppDataTObjectHelper;
import com.eazytec.bpm.app.home.data.app.tobject.AppsDataTObject;
import com.eazytec.bpm.app.home.userhome.adapters.AllAppSettingAdapter;
import com.eazytec.bpm.app.home.userhome.adapters.CommonAppSettingAdapter;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.checkbox.SmoothCheckBox;
import com.eazytec.bpm.appstub.view.gridview.ScrollGridView;
import com.eazytec.bpm.appstub.view.gridview.draggridview.AddDragGridView;
import com.eazytec.bpm.appstub.view.gridview.draggridview.DragCallback;
import com.eazytec.bpm.appstub.view.gridview.draggridview.DragGridView;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;

import java.util.List;

/**
 * 菜单顺序和常用菜单设置
 * @author Beckett_W
 * @version Id: HomeAppSettingActivity, v 0.1 2017/9/21 9:59 Beckett_W Exp $$
 */
public class HomeAppSettingActivity extends ContractViewActivity<HomeAppSettingPresenter> implements HomeAppSettingContract.View{

    private Toolbar toolbar;
    private Button rightbutton;
    private TextView toolbarTitleTextView;

    //常用菜单
    private GridView commonGridView;
    private List<BPMApp> commonApps;
    private CommonAppSettingAdapter commonAdapter;

    //全部菜单
    private AddDragGridView allGridView;
    private List<BPMApp>  allApps;
    private AllAppSettingAdapter allAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeapp_setting);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        rightbutton = (Button) findViewById(R.id.contactchoose_submit);
        rightbutton.setText("完成");
        rightbutton.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("菜单设置");


        commonGridView = (GridView) findViewById(R.id.activity_common_homeapp_setting);
        commonAdapter = new CommonAppSettingAdapter(getContext());
        commonGridView.setAdapter(commonAdapter);

        allGridView = (AddDragGridView) findViewById(R.id.activity_all_homeapp_setting);
        allAdapter = new AllAppSettingAdapter(getContext());
        allGridView.setAdapter(allAdapter);

        setListener();
        //加载两个菜单数据
        getPresenter().loadApps();
        getPresenter().loadAllApps();
    }

    private void setListener(){

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAppSettingActivity.this.finish();
            }
        });

        //常用的，是点击删除！
        commonGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                commonApps.remove(position);
                commonAdapter.setItems(commonApps);
                commonAdapter.notifyDataSetChanged();
                allAdapter.resetHasChooseList(commonApps);
                allAdapter.notifyDataSetChanged();
            }
        });

        //全部菜单
        allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SmoothCheckBox checkBox = (SmoothCheckBox) view.findViewById(R.id.iv_item_all_add_homeapp);

                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    String needRemoteId = allApps.get(position).getId();
                    BPMApp needRemoteObject = null;

                    for (BPMApp obj : commonApps) {

                        if (obj.getId().equals(needRemoteId)) {
                            needRemoteObject = obj;
                            break;
                        }
                    }
                    if (needRemoteObject != null) {
                        commonApps.remove(needRemoteObject);
                    }

                } else {
                    if(commonApps.size() <= allApps.size()) {
                        checkBox.setChecked(true);
                        BPMApp needAddObject = allApps.get(position);
                        commonApps.add(needAddObject);
                    } else{
                        checkBox.setChecked(false);
                        return;
                    }
                }
                allAdapter.resetHasChooseList(commonApps);
                commonAdapter.setItems(commonApps);
                commonAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    protected HomeAppSettingPresenter createPresenter() {
        return new HomeAppSettingPresenter();
    }

    @Override
    public void loadAppsSuccess(AppsDataTObject appsDataTObject) {
        this.commonApps = AppDataTObjectHelper.createBpmAppsByTObjects(appsDataTObject.getApps());
        commonAdapter.setItems(this.commonApps);
        commonAdapter.notifyDataSetChanged();
        //要去掉选过的
        allAdapter.resetHasChooseList(commonApps);
        allAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadAllAppsSuccess(AppsDataTObject appsDataTObject) {
        this.allApps = AppDataTObjectHelper.createBpmAppsByTObjects(appsDataTObject.getApps());
        allAdapter.setItems(this.allApps);
        allAdapter.notifyDataSetChanged();
    }
}
