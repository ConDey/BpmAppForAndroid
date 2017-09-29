package com.eazytec.bpm.app.calendar.savecontact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eazytec.bpm.app.calendar.MainActivity;
import com.eazytec.bpm.app.calendar.R;
import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.timepick.DatePickDialog;
import com.eazytec.bpm.appstub.view.timepick.OnSureLisener;
import com.eazytec.bpm.appstub.view.timepick.bean.DateType;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vivi on 2017/9/28.
 */

public class SaveAvtivity  extends ContractViewActivity<SavePresenter> implements SaveContact.View{
    private TextView edStartDateANDTime;
    private TextView edEndDateANDTime;
    private EditText edEventName;
    private EditText edLocation;
    private EditText edDescription;
    private Spinner edEventType;
    private String saveEventId;
    private String editStartTime;
    private String editStartDate;
    private String editEndTime;
    private String editEndDate;
    private LinearLayout edStartDateANDTimeLayout;
    private LinearLayout edEndDateANDTimeLayout;
    Date curDate=new Date(System.currentTimeMillis());//获取当前时间
    //开始日期，结束日期
    Date startDate =new Date();
    Date endDate=new Date();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_edit_list);
        edStartDateANDTime=(TextView)findViewById(R.id.ed_startDateANDTime);
        edEndDateANDTime=(TextView)findViewById(R.id.ed_endDateANDTime);
        edEventName=(EditText)findViewById(R.id.ed_eventName);
        edEventType=(Spinner)findViewById(R.id.ed_eventType);
        edLocation=(EditText)findViewById(R.id.ed_location);
        edDescription=(EditText)findViewById(R.id.ed_description);
        edStartDateANDTimeLayout=(LinearLayout)findViewById(R.id.ed_startDateANDTimeLayout);
        edEndDateANDTimeLayout=(LinearLayout)findViewById(R.id.ed_endDateANDTimeLayout);
        if(getIntent()!=null){
            saveEventId = getIntent().getStringExtra("id");
            if (StringUtils.isEmpty(saveEventId)) {
                ToastDelegate.info(getContext(),"工作编号为空");
            }
        }
        getPresenter().loadSaveDetails(saveEventId);
        setListener();

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
    }


    @Override
    public void loadSuccess(EventDetailDataObject eventDetailDataObject) {
        //开始时间
        editStartDate=eventDetailDataObject.getStartData();
        editStartTime=eventDetailDataObject.getStartTime();
        edStartDateANDTime.setText(editStartDate+" "+editStartTime);
        //结束时间
        editEndDate=eventDetailDataObject.getEndDate();
        editEndTime=eventDetailDataObject.getEndDate();
        edEndDateANDTime.setText(editEndDate+" "+editEndTime);

        edEventName.setText(eventDetailDataObject.getEventName());
        edLocation.setText(eventDetailDataObject.getLoaction());
        edDescription.setText(eventDetailDataObject.getDescription());
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
    protected SavePresenter createPresenter() {
        return new SavePresenter();
    }
}
