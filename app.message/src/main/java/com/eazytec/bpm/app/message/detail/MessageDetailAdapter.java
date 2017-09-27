package com.eazytec.bpm.app.message.detail;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.eazytec.bpm.app.message.R;
import com.eazytec.bpm.appstub.view.textview.BorderTextView;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageDetailAdapter extends BaseAdapter{

    private Context context;
    private List<MessageDataTObject> datas;

    public MessageDetailAdapter(Context context, List<MessageDataTObject> datas) {
        super();
        this.context = context;
        this.datas = datas;
    }

    public MessageDetailAdapter(Context context) {
        super();
        this.context = context;
        this.datas = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_message_detail_recycler_view, null);
            viewHolder.mainTitleTv = (TextView)convertView.findViewById(R.id.message_detail_main_title_tv);
            viewHolder.contentTv = (TextView)convertView.findViewById(R.id.message_detail_content_tv);
            viewHolder.timeTv = (TextView)convertView.findViewById(R.id.message_detail_time_tv);
            viewHolder.canClickll = (LinearLayout)convertView.findViewById(R.id.message_detail_can_click_ll);
            viewHolder.stateTv = (BorderTextView) convertView.findViewById(R.id.message_detail_main_state_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.canClickll.setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(datas.get(position).getTitle())) {
            viewHolder.mainTitleTv.setText(datas.get(position).getTitle());
        }
        if (!StringUtils.isEmpty(datas.get(position).getContent())) {
            viewHolder.contentTv.setText(datas.get(position).getContent());
        }
        if (!StringUtils.isEmpty(datas.get(position).getCreatedTime())) {
            long date = TimeUtils.string2Millis(datas.get(position).getCreatedTime());
            viewHolder.timeTv.setText(TimeUtils.showDate(new Date(date), true));
        }
        if (!datas.get(position).isCanClick()) {
            viewHolder.canClickll.setVisibility(View.GONE);
        }else {
            viewHolder.canClickll.setVisibility(View.VISIBLE);
        }

        if(!datas.get(position).getIsRead()){
            viewHolder.stateTv.setText("未读");
            viewHolder.stateTv.setTextColor(context.getResources().getColor(R.color.color_primary));
        }else{
            viewHolder.stateTv.setText("已读");
            viewHolder.stateTv.setTextColor(context.getResources().getColor(R.color.red_400));
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        if (position < datas.size()) {
            return datas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        private TextView mainTitleTv;
        private TextView contentTv;
        private TextView timeTv;
        private LinearLayout canClickll;
        private BorderTextView stateTv;

    }

    public void resetList(List<MessageDataTObject> messages) {
        datas.clear();
        datas.addAll(convertTheListOrder(messages));
    }

    public void addList(List<MessageDataTObject> messages) {
        datas.addAll(0,convertTheListOrder(messages));
    }

    public List<MessageDataTObject> getDatas() {
        return this.datas;
    }

    /**
     * 倒叙方法
     *
     * @param messages
     * @return
     */
    private List<MessageDataTObject> convertTheListOrder(List<MessageDataTObject> messages) {
        List<MessageDataTObject> newOrderMessages = new ArrayList<>();
        if (messages != null && messages.size() > 0) {
            for (int i = (messages.size()-1); i>=0 ;i--) {
                newOrderMessages.add(messages.get(i));
            }
        }
        return newOrderMessages;
    }

}
