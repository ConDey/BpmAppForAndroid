package com.eazytec.bpm.app.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eazytec.bpm.app.notice.R;
import com.eazytec.bpm.app.notice.data.AttachmentsDataTObject;
import com.eazytec.bpm.lib.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 附件下载列表的适配器
 * @author Beckett_W
 * @version Id: DownloadListAdapter, v 0.1 2017/7/12 13:05 Administrator Exp $$
 */
public class DownloadListAdapter extends BaseAdapter {

    private List<AttachmentsDataTObject> items;
    private Context context;

    public DownloadListAdapter(Context context){
        super();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notice_download, parent, false);
        }
        if (position < items.size() && items.get(position) != null) {

            if(items.get(position).getName().contains("png")){
            ((ImageView) ViewHolder.get(convertView, R.id.item_notice_download_head)).setImageResource(R.drawable.ic_download_type_img);
            }else{
                ((ImageView) ViewHolder.get(convertView, R.id.item_notice_download_head)).setImageResource(R.drawable.ic_download_type_file);
            }

            ((TextView) ViewHolder.get(convertView, R.id.item_notice_download_title)).setText(items.get(position).getName());

        }
        return convertView;
    }

    public void resetList(List<AttachmentsDataTObject> list) {
        items.clear();
        items.addAll(list);
    }
}
