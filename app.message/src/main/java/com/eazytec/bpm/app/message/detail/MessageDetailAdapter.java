package com.eazytec.bpm.app.message.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eazytec.bpm.app.message.R;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.view.recyclerview.RefreshViewHolder;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;


public class MessageDetailAdapter extends RecyclerView.Adapter<MessageDetailAdapter.ViewHolder>{

    public OnRecyclerViewItemClickListener mOnItemClickListener = null;//点击
    public OnRecyclerViewLongItemClickListener mOnLongItemClickListener = null;//长按

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setOnLongItemClickListener(OnRecyclerViewLongItemClickListener listener) {
        this.mOnLongItemClickListener = listener;
    }

    public interface OnRecyclerViewLongItemClickListener {
        void onLongItemClick(View view, int position,Object data);
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position,Object data);
    }

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<MessageDataTObject> datas;

    public MessageDetailAdapter(Context context,List<MessageDataTObject> datas) {
        super();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_message_detail_recycler_view, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (datas != null && datas.size() > 0) {
            if (!StringUtils.isEmpty(datas.get(position).getTitle())) {
                viewHolder.titleTv.setText(datas.get(position).getTitle());
            }
            viewHolder.contentTv.setText(datas.get(position).getContent());
            if (!StringUtils.isEmpty(datas.get(position).getCreatedTime())) {
                long date = TimeUtils.string2Millis(datas.get(position).getCreatedTime());
                String mDate = TimeUtils.showDate(new Date(date), false);
                viewHolder.timeTv.setText(mDate);
            }
            // 加载图片
            String photoUrl = Config.WEB_URL + datas.get(position).getTopicIcon();
            String photo = datas.get(position).getTopicIcon();
            if (!StringUtils.isSpace(photo)) {
                Picasso.with(context).load(photoUrl).placeholder(R.mipmap.ic_message_default).into(viewHolder.messageLeftImageView);
            }

            viewHolder.itemView.setTag(datas.get(position));


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position,datas.get(position));
                    }
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnLongItemClickListener != null) {
                        mOnLongItemClickListener.onLongItemClick(v, position,datas.get(position));
                    }
                    return true;
                }
            });
        }
    }

    @Override public int getItemCount() {
        return datas.size();
    }


    static class ViewHolder extends RefreshViewHolder {
        ImageView messageLeftImageView;
        TextView titleTv;
        TextView contentTv;
        TextView timeTv;

        public ViewHolder(View itemView, int type) {
            super(itemView, type);
            messageLeftImageView = (ImageView)itemView.findViewById(R.id.message_detail_mian_left_iv);
            titleTv = (TextView)itemView.findViewById(R.id.message_detail_main_title_tv);
            contentTv = (TextView)itemView.findViewById(R.id.message_detail_content_tv);
            timeTv = (TextView)itemView.findViewById(R.id.message_detail_time_tv);
        }
    }

    public void resetList(List<MessageDataTObject> list) {
        datas.clear();
        datas.addAll(list);
    }

    public void addList(List<MessageDataTObject> list) {
        datas.addAll(list);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position, Object data);
    }

}
