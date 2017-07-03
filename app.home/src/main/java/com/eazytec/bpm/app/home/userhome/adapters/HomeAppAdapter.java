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
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.ViewHolder;

import net.wequick.small.Small;

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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_homeapp_gridview, parent, false);
        }

        if (position < items.size() && items.get(position) != null) {
            BPMApp appitem = items.get(position);
            if (!StringUtils.isSpace(appitem.getId())) {
                // 设置显示图片
                String imageurl = appitem.getImageUrl();
                if (StringUtils.equals(appitem.getImageUrlType(), BPMApp.IMAGE_URL_TYPE_INNER)) {
                    int imageRes = context.getResources().getIdentifier(imageurl, "mipmap", HomeApplicaton.getInstance().getPackageName());
                    ImageView imageView = ViewHolder.get(convertView, R.id.iv_item_homeapp);
                    if (imageRes != 0x0) {
                        imageView.setImageResource(imageRes);
                    }
                } else if (StringUtils.equals(appitem.getImageUrlType(), BPMApp.IMAGE_URL_TYPE_REMOTE)) {
                }
                // 设置显示文字
                TextView textView = ViewHolder.get(convertView, R.id.tv_item_homeapp);
                textView.setText(appitem.getDisplayName());
            }
        }
        return convertView;
    }

    public void setItems(List<BPMApp> items) {
        this.items = new ArrayList<>();
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
}
