package com.eazytec.bpm.app.contact.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.UserContactData;
import com.eazytec.bpm.app.contact.utils.CnToSpell;
import com.eazytec.bpm.appstub.view.imageview.LetterImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * 带有过滤器的adapter
 * @author Beckett_W
 * @version Id: UserLocalContactAdapter, v 0.1 2017/7/6 9:29 Administrator Exp $$
 */
public class UserLocalContactAdapter extends BaseAdapter{
    private MyFilter myFilter;
    private List<UserContactData> list = null;
    private ArrayList<UserContactData> mOriginalValues;


    private final Object mLock = new Object();

    private Context mContext;

    public UserLocalContactAdapter(Context mContext, List<UserContactData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final UserContactData userContactData = list.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_local_contact_listview,null);
            viewHolder.catalog = (TextView) convertView.findViewById(R.id.item_local_contact_alpha);
            viewHolder.letterImageView = (LetterImageView) convertView.findViewById(R.id.item_local_contact_imageview);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_local_contact_name);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.item_local_contact_phone);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(catalog)){
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(userContactData.getFirstLetter().toUpperCase());
        }else{
            viewHolder.catalog.setVisibility(View.GONE);
        }
        viewHolder.letterImageView.setLetter(this.list.get(position).getName().substring(0,1));
        viewHolder.letterImageView.setOval(true);
        viewHolder.name.setText(this.list.get(position).getName());
        viewHolder.phone.setText(this.list.get(position).getPhoneNum());
        return convertView;
    }

    final static class ViewHolder {
        TextView catalog; //字母目录
        LetterImageView letterImageView; //首字母，可以用另一个，能设置头像
        TextView name;
        TextView phone;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }


    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter();
        }
        return myFilter;
    }

    class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    // 将list的用户 集合转换给这个原始数据的ArrayList
                    mOriginalValues = new ArrayList<UserContactData>(list);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<UserContactData> mlist = new ArrayList<UserContactData>(
                            mOriginalValues);
                    results.values = mlist;
                    results.count = mlist.size();
                }
            } else {
                // 做正式的筛选
                String prefixString = prefix.toString().toLowerCase();

                // 声明一个临时的集合对象 将原始数据赋给这个临时变量
                final ArrayList<UserContactData> values = mOriginalValues;

                final int count = values.size();

                // 新的集合对象
                final ArrayList<UserContactData> newValues = new ArrayList<UserContactData>(
                        count);

                for (int i = 0; i < count; i++) {
                    // 如果姓名的前缀相符或者电话相符就添加到新的集合
                    final UserContactData value = (UserContactData) values.get(i);

                    if (CnToSpell.getPinYinFirstLetter(value.getName()).startsWith(
                            prefixString)
                            || value.getPhoneNum().contains(prefixString)||value.getName().startsWith(prefixString)) {
                        newValues.add(value);
                    }
                }
                // 然后将这个新的集合数据赋给FilterResults对象
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // 重新将与适配器相关联的List重赋值一下
            list = (List<UserContactData>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}
