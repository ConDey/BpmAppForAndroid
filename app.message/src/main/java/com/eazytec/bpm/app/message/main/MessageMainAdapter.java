package com.eazytec.bpm.app.message.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eazytec.bpm.app.message.R;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.view.badgeview.BadgeView;
import com.eazytec.bpm.appstub.view.recyclerview.OnRecyclerViewItemClickListener;
import com.eazytec.bpm.appstub.view.recyclerview.RefreshViewHolder;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 16735
 * @version Id: MessageMainAdapter, v 0.1 2017-8-17 16:35 16735 Exp $$
 */
public class MessageMainAdapter extends RecyclerView.Adapter<MessageMainAdapter.ViewHolder> implements View.OnClickListener {

    private OnRecyclerViewItemClickListener listener;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<MessageTopicDataTObject> datas;

    public MessageMainAdapter(Context context) {
        super();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        datas = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_message_main_listview, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        // 数据量比较少，关闭复用
        viewHolder.setIsRecyclable(false);
        if (datas != null && datas.size() > 0) {
            if (!StringUtils.isEmpty(datas.get(position).getName())) {
                viewHolder.titleTv.setText(datas.get(position).getName());
            }
            viewHolder.contentTv.setText(datas.get(position).getLatestMessage());
            if (!StringUtils.isEmpty(datas.get(position).getLatestUpdateTime())) {
                String mDate = TimeUtils.showDate(new Date(Long.valueOf(datas.get(position).getLatestUpdateTime())), false);
                viewHolder.timeTv.setText(mDate);
            }
            // 加载图片
            String photoUrl = Config.WEB_URL + datas.get(position).getIcon();
            String photo = datas.get(position).getIcon();
            if (!StringUtils.isSpace(photo)) {
                Picasso.with(context).load(photoUrl).placeholder(R.mipmap.ic_message_default).into(viewHolder.messageLeftImageView);
            }

            // 添加角标
            if (viewHolder.messageBadgeView == null) {
                viewHolder.messageBadgeView = new BadgeView(context, viewHolder.messageLeftImageView);
            }
            if (datas.get(position).getUnReadMessages() > 0) {
                String unreads = String.valueOf(datas.get(position).getUnReadMessages());
                viewHolder.messageBadgeView.setText(unreads);
                viewHolder.messageBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                viewHolder.messageBadgeView.show();
            }else {
                viewHolder.messageBadgeView.hide();
            }

            viewHolder.itemView.setTag(datas.get(position));
        }
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, v.getTag());
        }
    }

    public void setListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RefreshViewHolder {
        ImageView messageLeftImageView;
        TextView titleTv;
        TextView contentTv;
        TextView timeTv;
        BadgeView messageBadgeView;

        public ViewHolder(View itemView, int type) {
            super(itemView, type);
            messageLeftImageView = (ImageView)itemView.findViewById(R.id.message_item_left_iv);
            titleTv = (TextView)itemView.findViewById(R.id.message_item_title_tv);
            contentTv = (TextView)itemView.findViewById(R.id.message_item_content_tv);
            timeTv = (TextView)itemView.findViewById(R.id.message_item_time_tv);
        }
    }

    public void resetList(List<MessageTopicDataTObject> list) {
        datas.clear();
        datas.addAll(list);
    }



}
