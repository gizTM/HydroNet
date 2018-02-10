package com.senior.gizgiz.hydronet.Adapter.GridViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.ClassForList.DropdownItem;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class LocationGridViewAdapter extends BaseAdapter {
    private Context context;
    public static List<DropdownItem> itemList;
    public static List<DropdownItem> selectedItem = new ArrayList<>();

    public LocationGridViewAdapter(Context context, List<DropdownItem> list) {
        this.context =context;
        itemList = list;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public DropdownItem getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_custom_content_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return convertView;
    }

    public List<DropdownItem> selectItem(int position,int maxSelected) {
        if (selectedItem.size() < maxSelected) {
            itemList.get(position).setSelected(true);
            selectedItem.add(itemList.get(position));
        }
        notifyDataSetChanged();
        return selectedItem;
    }

    public List<DropdownItem> unselectItem(int position) {
        if (selectedItem.size() > 0) {
            itemList.get(position).setSelected(false);
            selectedItem.remove(itemList.get(position));
        }
        notifyDataSetChanged();
        return selectedItem;
    }

    private class ViewHolder {
        private CustomTextView textView;

        public ViewHolder(View view) {
            textView = view.findViewById(R.id.custom_content_item);
        }

        public void bind(int position) {
            DropdownItem item = itemList.get(position);
            textView.setText(item.getInfo());
            if (item.isSelected()) {
                textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                textView.setBackgroundResource(R.drawable.bg_disabled_stepper);
            } else {
                textView.setTextColor(context.getResources().getColor(R.color.black));
                textView.setBackgroundResource(R.drawable.bg_stepper);
            }
        }
    }
}
