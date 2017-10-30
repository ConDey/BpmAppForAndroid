package com.eazytec.bpm.app.contact.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.usercontact.contactchoose.UserChooseConst;
import com.eazytec.bpm.app.contact.usercontact.contactchoose.UserChooseManager;
import com.eazytec.bpm.appstub.view.imageview.AvatarImageView;
import com.eazytec.bpm.lib.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 已经选择有最大值控制！
 * 用RecyclerView代替gridview，这样可以横向移动，界面布局好看一丢丢
 * @author Beckett_W
 * @version Id: ContactChooseHasChooseAdapter, v 0.1 2017/7/24 13:07 Administrator Exp $$
 */
public class ContactChooseHasChooseAdapter extends RecyclerView.Adapter<ContactChooseHasChooseAdapter.ViewHolder> {

    private List<UserDetailDataTObject> items;
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



    public ContactChooseHasChooseAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_haschoose_contactchoose, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final  ViewHolder viewHolder, int position) {

        int length = items.get(position).getFullName().length();
        if (position < items.size() && items.get(position) != null) {
            if(items.get(position).getFullName()!=null&& items.get(position).getFullName().length()>0){
            if(length<3){
                viewHolder.imageview.setText(items.get(position).getFullName());
            }else{
                viewHolder.imageview.setText(items.get(position).getFullName().substring(length-2));
            }
        }
        }
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(viewHolder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(viewHolder.itemView, position);
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
        public AvatarImageView imageview;

        public ViewHolder(View view) {
            super(view);

            imageview = (AvatarImageView) view.findViewById( R.id.item_haschoose_contactchoose_imageview);
        }
    }

    /**
     * 获取items
     *
     * @param items
     */
    public void setItems(List<UserDetailDataTObject> items) {
        this.items = items;
    }

}

