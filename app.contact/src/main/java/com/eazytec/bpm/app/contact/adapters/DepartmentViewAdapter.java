package com.eazytec.bpm.app.contact.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.appstub.view.imageview.AvatarImageView;
import com.eazytec.bpm.appstub.view.imageview.LetterImageView;
import com.eazytec.bpm.lib.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class DepartmentViewAdapter extends BaseAdapter {

    private List<DepartmentDataTObject> items;
    private Context context;

    public DepartmentViewAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dep_contact_listview, parent, false);
        }
        if (position < items.size() && items.get(position) != null) {
            ((AvatarImageView) ViewHolder.get(convertView, R.id.item_contact_imageview)).setText(items.get(position).getName().substring(0, 1));
            ((TextView) ViewHolder.get(convertView, R.id.item_contact_title)).setText(items.get(position).getName());
            ((TextView) ViewHolder.get(convertView, R.id.item_contact_num)).setText(items.get(position).getUserCount() + "人"); //员工人数
        }
        return convertView;
    }

    public void resetList(List<DepartmentDataTObject> list) {
        items.clear();
        items.addAll(list);
    }
}
