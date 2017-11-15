package com.eazytec.bpm.app.home.userhome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eazytec.bpm.app.home.HomeApplicaton;
import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.app.BPMApp;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.view.badgeview.BadgeView;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 最基本展示app的gridview
 *
 * @author Beckett_W
 * @version Id: CommonAppAdapter, v 0.1 2017/9/21 7:55 Beckett_W Exp $$
 */
public class CommonAppAdapter extends BaseAdapter {

    private List<BPMApp> items;
    private List<HashMap<String ,Object>> itemsIcon;
    private Context context;

    public CommonAppAdapter(Context context) {
        this.items = new ArrayList<>();
        this.itemsIcon = new ArrayList<>();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_homeapp_gridview, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_item_homeapp);
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_item_homeapp);
        convertView.setTag(viewHolder);
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


                for(int i =0 ;i<itemsIcon.size();i++){
                 HashMap<String,Object> map = itemsIcon.get(i);
                if (map.get("id").equals(items.get(position).getId())) {
                   //如果相同，就有角标
                    // 添加角标
                    if (viewHolder.messageBadgeView == null) {
                        viewHolder.messageBadgeView = new BadgeView(context, viewHolder.imageView);
                    }
                    try{
                    int num = (int)Double.parseDouble(map.get("num").toString());
                    if (num>0) {
                        viewHolder.messageBadgeView.setText(String.valueOf(num));
                        viewHolder.messageBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                        viewHolder.messageBadgeView.setBadgeMargin(5);
                        viewHolder.messageBadgeView.show();
                    }else {
                        viewHolder.messageBadgeView.hide();
                    }
                    }catch (Exception e) {
                    }
                }
                }
            }
        }
        return convertView;
    }

    final static class ViewHolder {
        TextView textView;
        ImageView imageView;
        BadgeView messageBadgeView; //应用APP的角标
    }

    public void setItems(List<BPMApp> items) {
        this.items.clear();
        this.items.addAll(items);

        if (items != null && items.size() % 4 != 0) {
            // 补全 items.size() % 4
            for (int index = 0; ; index++) {
                this.items.add(new BPMApp()); // 占位用
                if (this.items.size() % 4 == 0) {
                    break;
                }
            }
        }
    }

    public void resetIcon(List<HashMap<String ,Object>>items) {
        this.itemsIcon = items;
    }


}

