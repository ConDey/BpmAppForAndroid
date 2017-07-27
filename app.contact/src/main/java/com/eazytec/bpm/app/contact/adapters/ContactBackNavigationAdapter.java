package com.eazytec.bpm.app.contact.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.BackInfoDataTObject;
import com.eazytec.bpm.lib.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beckett_W
 * @version Id: ContactBackNavigationAdapter, v 0.1 2017/7/24 13:04 Administrator Exp $$
 */
public class ContactBackNavigationAdapter extends RecyclerView.Adapter<ContactBackNavigationAdapter.ViewHolder> {

    private List<BackInfoDataTObject> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }



    public ContactBackNavigationAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contactchoose_backnavigation, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final  ViewHolder viewHolder, int position) {

        if (position < items.size() && items.get(position) != null) {

            viewHolder.textView.setText(items.get(position).getName());
            viewHolder.imageView.setVisibility(View.VISIBLE);
            if(position == (items.size()-1)) {
                viewHolder.textView.setTextColor(Color.GRAY);
                viewHolder.imageView.setVisibility(View.GONE);
            }
        }
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    if(position == (items.size()-1)) {

                    }else {
                        mOnItemClickListener.onItemClick(viewHolder.itemView, position); // 2
                    }
                }
            });
        }
        if(mOnItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    if(position == (items.size()-1)) {
                        return false;
                    }else{
                    mOnItemLongClickListener.onItemLongClick(viewHolder.itemView, position);
                    }
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return items.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView  textView;
        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById( R.id.contactchoose_backnavigation_imageview);
            textView = (TextView) view.findViewById( R.id.contactchoose_backnavigation_textview);
        }
    }

    /**
     * 获取items
     *
     * @param items
     */
    public void setItems(List<BackInfoDataTObject> items) {
        this.items = items;
    }

}


