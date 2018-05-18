package com.senior.gizgiz.hydronet.Adapter.GridViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.Activity.AddPlantActivity;
import com.senior.gizgiz.hydronet.ClassForList.DropdownItem;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class LocationAdapter extends BaseAdapter {
    private Context context;
    private List<DropdownItem> itemList;
//    private List<String> currentlySelectedItems = new ArrayList<>();
    private List<String> previouslySelectedItems = new ArrayList<>();
    private List<String> currentlyViewItems = new ArrayList<>();

    private Map<String,List<String>> locationListForPlant;

    public LocationAdapter(Context context, List<DropdownItem> list) {
        this.context =context;
        itemList = list;
        locationListForPlant = new HashMap<>();
    }

    @Override public int getCount() {
        return itemList.size();
    }
    @Override public DropdownItem getItem(int position) {
        return itemList.get(position);
    }
    @Override public long getItemId(int position) {
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

    public List<String> selectItem(String name, final int position, int maxSelected) {
        if(locationListForPlant.get(name) == null) {
            itemList.get(position).setSelected(true);
//            currentlySelectedItems.add(itemList.get(position).getInfo());
            List<String> temp = new ArrayList<>();
            temp.add(itemList.get(position).getInfo());
            locationListForPlant.put(name,temp);
        } else if(locationListForPlant.get(name).size() < maxSelected) {
            itemList.get(position).setSelected(true);
//            currentlySelectedItems.add(itemList.get(position).getInfo());
            locationListForPlant.get(name).add(itemList.get(position).getInfo());
        } else AddPlantActivity.setLocationErrorMessage("Can choose only "+maxSelected+" items");
        notifyDataSetChanged();
        return locationListForPlant.get(name);
    }
    public List<String> selectAll(String name,int maxSelected) {
        for (DropdownItem item : itemList) {
            if(!previouslySelectedItems.contains(item.getInfo()) && !item.isSelected()) {
                if (locationListForPlant.get(name) == null) {
                    item.setSelected(true);
                    List<String> temp = new ArrayList<>();
                    temp.add(item.getInfo());
                    locationListForPlant.put(name, temp);
                } else if (locationListForPlant.get(name).size() < maxSelected) {
                    item.setSelected(true);
                    locationListForPlant.get(name).add(item.getInfo());
                }
            }
        }
        notifyDataSetChanged();
        return locationListForPlant.get(name);
    }
    public List<String> unSelectItem(String name,int position) {
        if(locationListForPlant.get(name).size() > 0) {
            itemList.get(position).setSelected(false);
//            currentlySelectedItems.remove(itemList.get(position).getInfo());
            locationListForPlant.get(name).remove(itemList.get(position).getInfo());
        }
        notifyDataSetChanged();
        return locationListForPlant.get(name);
    }
    public List<String> selectNone(String name) {
        for (DropdownItem item : itemList) {
            if(!previouslySelectedItems.contains(item.getInfo())) {
                if (locationListForPlant.get(name) != null) {
                    item.setSelected(false);
                    locationListForPlant.get(name).remove(item.getInfo());
                }
//                locationListForPlant.remove(name);
            }
        }
        notifyDataSetChanged();
        return locationListForPlant.get(name);
    }
    public void updatePreviousSelection(String selectedName) {
        previouslySelectedItems.clear();
        for(Map.Entry<String,List<String>> entry : locationListForPlant.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(selectedName)) {
                previouslySelectedItems.addAll(entry.getValue());
//                currentlySelectedItems.clear();
            }
        }
        notifyDataSetChanged();
    }
    public void addPreviouslySelection(String selectedName) {
        previouslySelectedItems.addAll(locationListForPlant.get(selectedName));
        notifyDataSetChanged();
    }

    public List<String> viewSelectedItem(String selectedName) {
//        previouslySelectedItems.removeAll(locationListForPlant.get(selectedName));
        currentlyViewItems.addAll(locationListForPlant.get(selectedName));
        notifyDataSetChanged();
        return currentlyViewItems;
    }
    public void resetViewSelectedItem(String selectedName) {
//        previouslySelectedItems.addAll(locationListForPlant.get(selectedName));
        currentlyViewItems.removeAll(locationListForPlant.get(selectedName));
        notifyDataSetChanged();
    }
    public void removeFromAdapter(String selectedName) {
        List<String> locations = locationListForPlant.get(selectedName);
        if(locations != null) {
            previouslySelectedItems.removeAll(locations);
            for(String location : locations)
                for (DropdownItem item : itemList)
                    if (location.equalsIgnoreCase(item.getInfo())) item.setSelected(false);
            locationListForPlant.remove(selectedName);
        }
    }
    public void resetLocationAdapter() {
        previouslySelectedItems.clear();
//        currentlySelectedItems.clear();
        locationListForPlant.clear();
        notifyDataSetChanged();
    }
    public int getRemainingSlot() {
        int remainingLocation = 0;
        for (DropdownItem item : itemList) remainingLocation += (!item.isSelected())?1:0;
        return remainingLocation;
    }

    public List<DropdownItem> getItemList() { return itemList; }
//    public List<String> getCurrentlySelectedItems() { return currentlySelectedItems; }
    public List<String> getPreviouslySelectedItems() { return previouslySelectedItems; }
    public Map<String, List<String>> getLocationListForPlant() { return locationListForPlant; }

    private class ViewHolder {
        private CustomTextView itemLabel;

        public ViewHolder(View view) {
            itemLabel = view.findViewById(R.id.custom_content_item);
        }

        public void bind(int position) {
            DropdownItem item = itemList.get(position);
            itemLabel.setText(item.getInfo());
            if (item.isSelected()) {
                if(currentlyViewItems.contains(item.getInfo())) {
                    itemLabel.setTextColor(ResourceManager.getColor(context,R.color.white));
                    itemLabel.setBackgroundResource(R.drawable.bg_view_dropdown_item);
                } else if(previouslySelectedItems.contains(item.getInfo())) {
                    itemLabel.setTextColor(ResourceManager.getColor(context,R.color.white));
                    itemLabel.setBackgroundResource(R.drawable.bg_disabled_dropdown_item);
                } else {
                    itemLabel.setTextColor(ResourceManager.getColor(context,R.color.gallery_gray));
                    itemLabel.setBackgroundResource(R.drawable.bg_selected_dropdown_item);
                }
            } else {
                itemLabel.setTextColor(ResourceManager.getColor(context,R.color.boulder_gray));
                itemLabel.setBackgroundResource(R.drawable.bg_stepper);
            }
        }
    }
}
