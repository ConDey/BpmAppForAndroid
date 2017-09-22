package com.eazytec.bpm.app.home.userhome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.eazytec.bpm.app.home.HomeApplicaton;
import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.app.BPMApp;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.view.checkbox.SmoothCheckBox;
import com.eazytec.bpm.appstub.view.gridview.draggridview.DragAdapterInterface;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beckett_W
 * @version Id: HomeAllAppAdapter, v 0.1 2017/9/20 15:15 Beckett_W Exp $$
 */
public class AllAppSettingAdapter extends BaseAdapter implements DragAdapterInterface {

    private List<BPMApp> items;
    private Context context;
    private List<String> haschooseitems;

    public AllAppSettingAdapter(Context context) {
        this.items = new ArrayList<>();
        this.haschooseitems = new ArrayList<>();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_draggridview, parent, false);
        }

        if (position < items.size() && items.get(position) != null) {
            BPMApp appitem = items.get(position);
            if (!StringUtils.isSpace(appitem.getId())) {

                // 设置显示图片
                String imageurl = appitem.getImageUrl();
                ImageView imageView = ViewHolder.get(convertView, R.id.iv_item_all_homeapp);
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
                TextView textView = ViewHolder.get(convertView, R.id.tv_item_all_homeapp);
                textView.setText(appitem.getDisplayName());
            }

            SmoothCheckBox checkBox = ViewHolder.get(convertView, R.id.iv_item_all_add_homeapp);
            if (haschooseitems.contains(items.get(position).getId())) {
                checkBox.setChecked(true);
                isEnabled(position);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View v, MotionEvent event) {
                    return true; // 禁止checkBox的点击事件交给item去处理
                }
            });
        }


        return convertView;
    }

    public void setItems(List<BPMApp> items) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
    }

    public void resetHasChooseList(List<BPMApp> list) {
        haschooseitems.clear();
        if (list != null && list.size() > 0) {
            for (BPMApp ob : list) {
                haschooseitems.add(ob.getId());
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
