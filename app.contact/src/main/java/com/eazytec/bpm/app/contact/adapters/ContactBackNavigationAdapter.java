package com.eazytec.bpm.app.contact.adapters;

import android.content.Context;
import android.graphics.Color;
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
public class ContactBackNavigationAdapter extends BaseAdapter {

    private List<BackInfoDataTObject> items;
    private Context context;

    public ContactBackNavigationAdapter(Context context){
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contactchoose_backnavigation, parent, false);
        }

        if (position < items.size() && items.get(position) != null) {
            TextView textView = ViewHolder.get(convertView,R.id.contactchoose_backnavigation_textview);
            textView.setText(items.get(position).getName());
            ImageView imageView = ViewHolder.get(convertView,R.id.contactchoose_backnavigation_imageview);
            imageView.setVisibility(View.VISIBLE);
            if(position == (items.size()-1)) {
                textView.setTextColor(Color.GRAY);
                imageView.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        if(position == (items.size()-1)) {
            return false;
        }else
            return super.isEnabled(position);
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

