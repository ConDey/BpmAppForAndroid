package com.eazytec.bpm.app.calendar.savecontact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.eazytec.bpm.app.calendar.MainActivity;
import com.eazytec.bpm.app.calendar.R;
import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventTypeObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.timepick.DatePickDialog;
import com.eazytec.bpm.appstub.view.timepick.OnSureLisener;
import com.eazytec.bpm.appstub.view.timepick.bean.DateType;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Vivi on 2017/9/28.
 */

public class SaveAvtivity  extends ContractViewActivity<SavePresenter> implements SaveContact.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private TextView edStartDateANDTime;
    private TextView edEndDateANDTime;
    private EditText edEventName;
    private EditText edLocation;
    private EditText edDescription;
    //类型
    private Spinner edEventType;
    private String  edEventTypeInfo;
    private List<EventTypeObject>  eventTypeObjectList;
    private ArrayAdapter spinnerAdapter;
    private int position;
    private String strEventType;
    private int typePosition;


    private String saveEventId;
    private String editStartTime;
    private String editStartDate;
    private String editEndTime;
    private String editEndDate;
    private Button edSave;
    private LinearLayout edStartDateANDTimeLayout;
    private LinearLayout edEndDateANDTimeLayout;

    Date curDate=new Date(System.currentTimeMillis());//获取当前时间
    //开始日期，结束日期
    Date startDate =new Date();
    Date endDate=new Date();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_edit_list);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("修改日程");


        edStartDateANDTime=(TextView)findViewById(R.id.ed_startDateANDTime);
        edEndDateANDTime=(TextView)findViewById(R.id.ed_endDateANDTime);
        edEventName=(EditText)findViewById(R.id.ed_eventName);
        edEventType=(Spinner)findViewById(R.id.ed_eventType);
        edLocation=(EditText)findViewById(R.id.ed_location);
        edDescription=(EditText)findViewById(R.id.ed_description);
        edSave=(Button)findViewById(R.id.ed_save);
        edStartDateANDTimeLayout=(LinearLayout)findViewById(R.id.ed_startDateANDTimeLayout);
        edEndDateANDTimeLayout=(LinearLayout)findViewById(R.id.ed_endDateANDTimeLayout);
        if(getIntent()!=null){
            saveEventId = getIntent().getStringExtra("id");
            if (StringUtils.isEmpty(saveEventId)) {
                ToastDelegate.info(getContext(),"工作编号为空");
            }
        }
        getPresenter().loadSaveDetails(saveEventId);
        //时间控件选择，spinner选择
        setListener();
        //页面取值，提交
        setSave();

    }

    private void setSave() {
        //从页面获取值
        String startTimeANDDate=edStartDateANDTime.getText().toString();
        String[] tempStart=startTimeANDDate.split(" ");
        editStartDate=tempStart[0];
        editStartTime=tempStart[1];
        String endTimeANDDate=edEndDateANDTime.getText().toString();
        String[] tempEnd=endTimeANDDate.split(" ");
        editEndDate=tempEnd[0];
        editEndTime=tempEnd[1];
        final String location=edLocation.getText().toString();
        final String description=edDescription.getText().toString();
        final String EventName=edEventName.getText().toString();
        final String eventid=saveEventId;
        typePosition=eventTypeObjectList.indexOf(strEventType);
        final String eventTypeSelection=eventTypeObjectList.get(typePosition).getCode();


        edSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().editDetail(editStartTime,editStartDate,editEndTime,editEndDate,description,location,EventName,eventTypeSelection,eventid);
            }
        });

    }

    private void setListener() {
        edStartDateANDTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickDialog dialog = new DatePickDialog(getContext());
                //设置上下年分限制
                dialog.setYearLimt(6);
                //设置标题
                dialog.setTitle("选择计划开始时间");
                dialog.setStartDate(curDate);
                dialog.setType(DateType.TYPE_YMDHM);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm:ss");
                //设置选择回调
                dialog.setOnChangeLisener(null);
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String pushTime = sdf.format(date);
                        //选择时间，不得小于当前日期，要判断
                        if((curDate.getTime()-date.getTime())<=0) {
                            edStartDateANDTime.setText(pushTime);
                            startDate = date;
                        } else{
                            ToastDelegate.info(getContext(),"所选时间不得小于当前时间");
                            return;
                        }
                    }
                });
                dialog.show();
            }
        });
        edEndDateANDTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickDialog dialog = new DatePickDialog(getContext());
                //设置上下年分限制
                dialog.setYearLimt(6);
                //设置标题
                dialog.setTitle("选择计划结束时间");
                dialog.setStartDate(curDate);
                dialog.setType(DateType.TYPE_YMDHM);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm:ss");
                //设置选择回调
                dialog.setOnChangeLisener(null);
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String pushTime = sdf.format(date);
                        //选择时间，不得小于当前日期，要判断
                        if((curDate.getTime()-date.getTime())<=0) {
                            edEndDateANDTime.setText(pushTime);
                            endDate = date;
                        } else{
                            ToastDelegate.info(getContext(),"所选时间不得小于当前时间");
                            return;
                        }
                    }
                });
                dialog.show();
            }
        });

        //下拉框选择
        edEventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strEventType=(String)edEventType.getItemAtPosition(position);

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAvtivity.this.finish();
            }
        });

    }


    @Override
    public void loadSuccess(EventDetailDataObject eventDetailDataObject) {
        //开始时间
        editStartDate=eventDetailDataObject.getStartDate();
        editStartTime=eventDetailDataObject.getStartTime();
        edStartDateANDTime.setText(editStartDate+" "+editStartTime);
        //结束时间
        editEndDate=eventDetailDataObject.getEndDate();
        editEndTime=eventDetailDataObject.getEndTime();
        edEndDateANDTime.setText(editEndDate+" "+editEndTime);

        edEventName.setText(eventDetailDataObject.getEventName());
        edLocation.setText(eventDetailDataObject.getLocation());
        edDescription.setText(eventDetailDataObject.getDescription());
        edEventTypeInfo=eventDetailDataObject.getEventType();
        position=eventTypeObjectList.indexOf(edEventTypeInfo);
        edEventType.setSelection(position);
    }

    @Override
    public void postSuccess() {
        ToastDelegate.info(getContext(),"保存成功");
        Intent intent=new Intent();
        intent.setClass(getContext(),MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void loadEventType(EventTypeObject eventTypeObject) {
        eventTypeObjectList=eventTypeObject.getDatas();
        spinnerAdapter =new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,eventTypeObjectList);
        edEventType.setAdapter(spinnerAdapter);
    }

    @Override
    protected SavePresenter createPresenter() {
        return new SavePresenter();
    }
}
