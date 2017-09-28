package com.eazytec.bpm.app.calendar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eazytec.bpm.app.calendar.R;
import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivi on 2017/9/27.
 */

public class ItemListAdapter extends BaseAdapter {

    private List<EventListDataObject> items;
    private Context context;

    public ItemListAdapter(Context context){
        super();
        this.context=context;
        this.items=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return items!=null?items.size():0;
    }

    @Override
    public Object getItem(int position) {
        if(position<items.size()){
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount(){
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_list,null);
            viewHolder.startDate=(TextView) convertView.findViewById(R.id.it_startDate);
            viewHolder.startTime=(TextView) convertView.findViewById(R.id.it_startTime);
            viewHolder.endDate=(TextView) convertView.findViewById(R.id.it_endDate);
            viewHolder.endTime=(TextView) convertView.findViewById(R.id.it_endTime);
            viewHolder.location=(TextView) convertView.findViewById(R.id.it_location);
            viewHolder.eventName=(TextView) convertView.findViewById(R.id.it_eventName);
            viewHolder.eventType=(TextView) convertView.findViewById(R.id.it_eventType);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        EventListDataObject item=items.get(position);
        //开始日期
        String startDate=item.getStartDate();
        if (!StringUtils.isEmpty(startDate)){
            viewHolder.startDate.setText(startDate);
        }
        //开始时间
        String startTime=item.getStartTime();
            if (!StringUtils.isEmpty(startTime)){
            viewHolder.startTime.setText(startTime);
        }
        //结束日期
        String endDate=item.getEndDate();
        if (!StringUtils.isEmpty(endDate)){
            viewHolder.endDate.setText(endDate);
        }
        //结束时间
        String endTime=item.getEndTime();
        if (!StringUtils.isEmpty(endTime)){
            viewHolder.endTime.setText(endTime);
        }
        //事件地点
        String location=item.getLocation();
        if (!StringUtils.isEmpty(location)){
            viewHolder.location.setText(location);
        }
        //事件名称
        String eventName=item.getEventName();
        if (!StringUtils.isEmpty(eventName)){
            viewHolder.eventName.setText(eventName);
        }
        //事件类型
        String eventType=item.getEventType();
        if (!StringUtils.isEmpty(eventType)){
            viewHolder.eventType.setText(eventType);
        }
        return convertView;
    }

    static class ViewHolder{
        TextView startTime;
        TextView startDate;
        TextView endTime;
        TextView endDate;
        TextView location;
        TextView eventName;
        TextView eventType;

    }
    public void resetList(List<EventListDataObject> list){
        items.clear();
        items.addAll(list);
    }
}
