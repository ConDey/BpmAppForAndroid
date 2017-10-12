package com.eazytec.bpm.app.home.userhome.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eazytec.bpm.app.home.HomeApplicaton;
import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.app.BPMApp;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.view.gridview.draggridview.DragAdapterInterface;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.ViewHolder;
import com.squareup.picasso.Picasso;

import net.wequick.small.Small;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示常用APP的
 * @author Beckett_W
 * @version Id: HomeAllAppAdapter, v 0.1 2017/9/20 15:15 Beckett_W Exp $$
 */
public class CommonAppSettingAdapter extends BaseAdapter{


    private List<BPMApp> items;
    private Context context;

    public CommonAppSettingAdapter(Context context) {
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
    public boolean isEnabled(int position) {
        BPMApp appitem = items.get(position);
        if (!StringUtils.isSpace(appitem.getId())) {
            return true;
        }
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_delete_draggridview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_draggridview_tv_item_homeapp);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_draggridview_iv_item_homeapp);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position < items.size() && items.get(position) != null) {
            BPMApp appitem = items.get(position);
            if (!StringUtils.isSpace(appitem.getId())) {
                // 设置显示图片
                String imageurl = appitem.getImageUrl();
                if (StringUtils.equals(appitem.getImageUrlType(), BPMApp.IMAGE_URL_TYPE_INNER)) {
                    int imageRes = context.getResources().getIdentifier(imageurl, "mipmap", HomeApplicaton.getInstance().getPackageName());
                    if (imageRes != 0x0) {
                        viewHolder.imageView.setImageResource(imageRes);
                    }
                } else if (StringUtils.equals(appitem.getImageUrlType(), BPMApp.IMAGE_URL_TYPE_REMOTE)) {

                    imageurl = Config.WEB_URL + imageurl;
                    Picasso.with(context).load(imageurl).placeholder(R.mipmap.ic_homeapp_stub).into(viewHolder.imageView);
                }
                // 设置显示文字
                viewHolder.textView.setText(appitem.getDisplayName());
            }
        }
        return convertView;
    }

    final static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    public void setItems(List<BPMApp> items) {
        this.items.clear();
        this.items.addAll(items);
    }
}
