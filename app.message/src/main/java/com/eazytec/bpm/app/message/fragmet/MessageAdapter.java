package com.eazytec.bpm.app.message.fragmet;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eazytec.bpm.app.message.R;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.view.badgeview.BadgeView;
import com.eazytec.bpm.appstub.view.recyclerview.OnRecyclerViewItemClickListener;
import com.eazytec.bpm.appstub.view.recyclerview.RefreshViewHolder;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Beckett_W
 * @version Id: MessageDetailAdapter, v 0.1 2017/9/18 15:00 Beckett_W Exp $$
 */
public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<MessageDataTObject> datas;

    public MessageAdapter(Context context, List<MessageDataTObject> datas) {
        super();
        this.context = context;
        this.datas = datas;
    }

    public MessageAdapter(Context context) {
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
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_message_detail_recycler_view_imageview);
            viewHolder.timeTv = (TextView) convertView.findViewById(R.id.item_message_detail_item_time_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtils.isEmpty(datas.get(position).getTitle())) {
            viewHolder.mainTitleTv.setText(datas.get(position).getTitle());
        }
        if (!StringUtils.isEmpty(datas.get(position).getContent())) {
            viewHolder.contentTv.setText(datas.get(position).getContent());
        }
        // 加载图片
        String photoUrl = Config.WEB_URL + datas.get(position).getTopicIcon();
        String photo = datas.get(position).getTopicIcon();
        if (!StringUtils.isSpace(photo)) {
            Picasso.with(context).load(photoUrl).placeholder(R.mipmap.ic_message_default).into(viewHolder.imageView);
        }
        if(!StringUtils.isEmpty(datas.get(position).getCreatedTime())){
            long date = TimeUtils.string2Millis(datas.get(position).getCreatedTime());
            String mDate = TimeUtils.showDate(new Date(date), false);
            viewHolder.timeTv.setText(mDate);
        }

        // 添加角标
        if (viewHolder.messageBadgeView == null) {
            viewHolder.messageBadgeView = new BadgeView(context, viewHolder.imageView);
        }
        //未读
        if (!datas.get(position).getIsRead()) {
            viewHolder.messageBadgeView.setText(""); //小圆点提示
            viewHolder.messageBadgeView.setHeight(20);
            viewHolder.messageBadgeView.setWidth(20);
            viewHolder.messageBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            viewHolder.messageBadgeView.show();
        }else {
            viewHolder.messageBadgeView.hide();
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

    public static class ViewHolder{
        private ImageView imageView;
        private TextView mainTitleTv;
        private TextView contentTv;
        private TextView timeTv;
        private BadgeView messageBadgeView;
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

