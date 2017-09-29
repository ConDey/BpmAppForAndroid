package com.eazytec.bpm.app.calendar.detailcontact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eazytec.bpm.app.calendar.R;
import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.app.calendar.savecontact.SaveAvtivity;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.utils.StringUtils;

/**
 * Created by Vivi on 2017/9/28.
 */

public class DetailActivity extends ContractViewActivity<DetailPresenter> implements DetailContact.View {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private TextView shStartTime;
    private TextView shStartDate;
    private TextView shEndTime;
    private TextView shEndDate;
    private TextView shLocation;
    private TextView shDescription;
    private TextView shEventName;
    private TextView shEventType;
    private Button  shEdit;
    private Button shDelete;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_list);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("日程详情");


        shStartDate=(TextView)findViewById(R.id.sh_startDate);
        shStartTime=(TextView)findViewById(R.id.sh_startTime);
        shEndDate=(TextView)findViewById(R.id.sh_endDate);
        shEndTime=(TextView)findViewById(R.id.sh_endTime);
        shLocation=(TextView)findViewById(R.id.sh_location);
        shDescription=(TextView)findViewById(R.id.sh_description);
        shEventName=(TextView)findViewById(R.id.sh_eventName);
        shEventType=(TextView)findViewById(R.id.sh_eventType);
        shEdit=(Button)findViewById(R.id.sh_edit);
        shDelete=(Button)findViewById(R.id.sh_delete);
        if(getIntent()!=null){
            eventId = getIntent().getStringExtra("id");
            if (StringUtils.isEmpty(eventId)) {
                ToastDelegate.info(getContext(),"工作编号为空");
            }
        }
        initData();
        setListener();
    }

    private void setListener() {
        //监听事件
        shEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("id",eventId);
                intent.setClass(getContext(),SaveAvtivity.class);
                startActivity(intent);
            }
        });
        shDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().deleteDetail(eventId);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });
    }

    private void initData() {
        getPresenter().loadDetail(eventId);
    }

    @Override
    protected DetailPresenter createPresenter() {
        return new DetailPresenter();
    }

    @Override
    public void loadSuccess(EventDetailDataObject eventDetailDataObject) {
        shStartTime.setText(eventDetailDataObject.getStartTime());
        shStartDate.setText(eventDetailDataObject.getStartDate());
        shEventType.setText(eventDetailDataObject.getEventType());
        shEventName.setText(eventDetailDataObject.getEventName());
        shEndDate.setText(eventDetailDataObject.getEndDate());
        shEndTime.setText(eventDetailDataObject.getEndTime());
        shDescription.setText(eventDetailDataObject.getDescription());
        shLocation.setText(eventDetailDataObject.getLocation());
    }

    @Override
    public void deleteSuccess() {
        ToastDelegate.success(getContext(),"删除成功！");
        DetailActivity.this.finish();
    }
}
