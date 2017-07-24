package com.eazytec.bpm.app.contact.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.appstub.view.imageview.AvatarImageView;
import com.eazytec.bpm.lib.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beckett_W
 * @version Id: ContactChooseHasChooseAdapter, v 0.1 2017/7/24 13:07 Administrator Exp $$
 */
public class ContactChooseHasChooseAdapter extends BaseAdapter {

    private List<UserDetailDataTObject> items;
    private Context context;

    public ContactChooseHasChooseAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_haschoose_contactchoose, parent, false);
        }

        int length = items.get(position).getFullName().length();
        if (position < items.size() && items.get(position) != null) {
            AvatarImageView imageView = ViewHolder.get(convertView, R.id.item_haschoose_contactchoose_imageview);
            if(length<3){
                imageView.setText(items.get(position).getFullName());
            }else{
                imageView.setText(items.get(position).getFullName().substring(length-2));
            }
        }
        return convertView;
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


