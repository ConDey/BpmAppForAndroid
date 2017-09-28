package com.eazytec.bpm.app.calendar;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eazytec.bpm.app.calendar.adapters.ItemListAdapter;
import com.eazytec.bpm.app.calendar.contact.ItemListContract;
import com.eazytec.bpm.app.calendar.contact.ItemListPresenter;
import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.app.calendar.detailcontact.DetailActivity;
import com.eazytec.bpm.appstub.view.calendar.CaledarAdapter;
import com.eazytec.bpm.appstub.view.calendar.CalendarBean;
import com.eazytec.bpm.appstub.view.calendar.CalendarDateView;
import com.eazytec.bpm.appstub.view.calendar.CalendarView;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ContractViewActivity<ItemListPresenter> implements ItemListContract.View {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;
    private CalendarDateView calendarDateView;

    private TextView dateTv;

    private ListView todayListView;

    private TextView emptyTv;

    private List<EventListDataObject>  eventListDataObjectList;

    private ItemListAdapter itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        calendarDateView = (CalendarDateView) findViewById(R.id.schedule_list_calendarDateView);
        dateTv = (TextView) findViewById(R.id.schedule_list_date);
        todayListView  = (ListView) findViewById(R.id.schedule_list);
        emptyTv=(TextView)findViewById(R.id.schedule_list_empty);
        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("日程表");
        initDate();
        setListener();
    }

    @Override
    protected ItemListPresenter createPresenter() {
        return new ItemListPresenter();
    }

    private void initDate(){

        itemListAdapter=new ItemListAdapter(getContext());
        todayListView.setAdapter(itemListAdapter);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        calendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(48), px(48));
                    convertView.setLayoutParams(params);
                }
                view = (TextView) convertView.findViewById(R.id.item_calendar_text);

                view.setText(String.valueOf(bean.day));
                if (bean.mothFlag != 0) {
                    view.setTextColor(0xff9299a1);
                } else {
                    view.setTextColor(0xffffffff);
                }

                return convertView;
            }
        });
        String todaydate=dateTv.getText().toString();
        getPresenter().loadItemById(todaydate);



       /* //这样重写的啊，只是示例
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, null);
                }

                TextView textView = (TextView) convertView;
                textView.setText("position:" + position);

                return convertView;
            }
        });*/
    }


    private void setListener(){
        calendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                dateTv.setText(bean.year + "/" + getDisPlayNumber(bean.moth) + "/" + getDisPlayNumber(bean.day));
                String date = bean.year + "/" + getDisPlayNumber(bean.moth) + "/" + getDisPlayNumber(bean.day);
                getPresenter().loadItemById(date);
            }
        });

        todayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                String eventId = eventListDataObjectList.get(position).getId();
                intent.putExtra("eventId",eventId);
                intent.setClass(MainActivity.this, DetailActivity.class);
                startActivity(intent);

            }
        });
    }

    
    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }

    public static int px(float dipValue) {
        Resources r=Resources.getSystem();
        final float scale =r.getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void loadItemList(EventListDataObject eventListDataObject) {
        if (eventListDataObject.getDatas()!=null&& eventListDataObject.getDatas().size()>0){
            emptyTv.setVisibility(View.GONE);
            eventListDataObjectList=eventListDataObject.getDatas();
            itemListAdapter.resetList(eventListDataObjectList);
            itemListAdapter.notifyDataSetChanged();
        }else {
            emptyTv.setVisibility(View.VISIBLE);
        }
    }

}
