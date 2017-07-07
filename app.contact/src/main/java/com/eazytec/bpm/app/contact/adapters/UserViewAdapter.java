package com.eazytec.bpm.app.contact.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.appstub.view.imageview.AvatarImageView;
import com.eazytec.bpm.appstub.view.imageview.LetterImageView;
import com.eazytec.bpm.lib.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class UserViewAdapter extends BaseAdapter {

    private List<UserDetailDataTObject> items;
    private Context context;

    public UserViewAdapter(Context context) {
        super();
        this.items = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_contact_listview, parent, false);
        }
        int length = items.get(position).getFullName().length();
        if (position < items.size() && items.get(position) != null) {
             if(length<3){
              ((AvatarImageView) ViewHolder.get(convertView, R.id.item_user_contact_imageview)).setText(items.get(position).getFullName());
             }else{
            ((AvatarImageView) ViewHolder.get(convertView, R.id.item_user_contact_imageview)).setText(items.get(position).getFullName().substring(length-2));
             }
            ((TextView) ViewHolder.get(convertView, R.id.item_user_contact_title)).setText(items.get(position).getFullName());
        }
        return convertView;
    }

    public void resetList(List<UserDetailDataTObject> list) {
        items.clear();
        items.addAll(list);
    }
}