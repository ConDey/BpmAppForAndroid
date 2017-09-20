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
 * BPM APP所表现的Gridview的适配器
 *
 * @author ConDey
 * @version Id: HomeAppAdapter, v 0.1 2017/5/17 下午1:32 ConDey Exp $$
 */
public class HomeAppAdapter extends BaseAdapter implements DragAdapterInterface {

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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_delete_draggridview, parent, false);
        }

        if (position < items.size() && items.get(position) != null) {
            BPMApp appitem = items.get(position);
            if (!StringUtils.isSpace(appitem.getId())) {

                // 设置显示图片
                String imageurl = appitem.getImageUrl();
                ImageView imageView = ViewHolder.get(convertView, R.id.iv_item_homeapp);
                if (StringUtils.equals(appitem.getImageUrlType(), BPMApp.IMAGE_URL_TYPE_INNER)) {
                    int imageRes = context.getResources().getIdentifier(imageurl, "mipmap", HomeApplicaton.getInstance().getPackageName());
                    if (imageRes != 0x0) {
                        imageView.setImageResource(imageRes);
                    }
                } else if (StringUtils.equals(appitem.getImageUrlType(), BPMApp.IMAGE_URL_TYPE_REMOTE)) {

                    imageurl = Config.WEB_URL + imageurl;
                    Picasso.with(context).load(imageurl).placeholder(R.mipmap.ic_homeapp_stub).into(imageView);
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

    @Override
    public void reOrder(int startPosition, int endPosition) {
        if(endPosition<items.size())
        {
            BPMApp object=items.remove(startPosition);
            items.add(endPosition, (BPMApp) object);
            notifyDataSetChanged();
        }
    }
}
