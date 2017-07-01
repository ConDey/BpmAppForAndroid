package com.eazytec.bpm.app.home.userhome.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.app.BPMApp;

import java.util.ArrayList;
import java.util.List;

/**
 * BPM APP所表现的Gridview的适配器
 *
 * @author ConDey
 * @version Id: HomeAppAdapter, v 0.1 2017/5/17 下午1:32 ConDey Exp $$
 */
public class HomeAppAdapter extends BaseAdapter {

    private List<BPMApp> items;
    private Context context;

    public HomeAppAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }

    public HomeAppAdapter(Context context, List<BPMApp> items) {
        this.context = context;
        this.items = items;
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

    @TargetApi(21)
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_homeapp_gridview, parent, false);
        }
        return convertView;
    }

    public void setItems(List<BPMApp> items) {
        this.items = items;
    }
}
