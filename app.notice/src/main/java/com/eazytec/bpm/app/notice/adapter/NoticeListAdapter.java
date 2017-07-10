package com.eazytec.bpm.app.notice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eazytec.bpm.app.notice.R;
import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.appstub.view.recyclerview.OnRecyclerViewItemClickListener;
import com.eazytec.bpm.appstub.view.recyclerview.RefreshViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version Id: NoticeListAdapter, v 0.1 2017/7/10 9:57 Administrator Exp $$
 */
public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.ViewHolder> implements View.OnClickListener {

    private OnRecyclerViewItemClickListener listener;
    private List<NoticeDetailDataTObject> items;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public NoticeListAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_notice_list, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view, viewType);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        NoticeDetailDataTObject item = items.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.authorTextView.setText(item.getCreatedBy());
        holder.timeTextView.setText(item.getCreatedTime());
        holder.itemView.setTag(item);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, v.getTag());
        }
    }

    public void setListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RefreshViewHolder {

        public TextView titleTextView;
        public TextView authorTextView;
        public TextView timeTextView;

        public ViewHolder(View itemView, int type) {
            super(itemView, type);
            titleTextView = (TextView) itemView.findViewById(R.id.item_notice_title_textview);
            authorTextView = (TextView) itemView.findViewById(R.id.item_notice_author_textview);
            timeTextView = (TextView) itemView.findViewById(R.id.item_notice_time_textview);
        }
    }

    public void resetList(List<NoticeDetailDataTObject> list) {
        items.clear();
        items.addAll(list);
    }

    public void addList(List<NoticeDetailDataTObject> list) {
        items.addAll(list);
    }
}
