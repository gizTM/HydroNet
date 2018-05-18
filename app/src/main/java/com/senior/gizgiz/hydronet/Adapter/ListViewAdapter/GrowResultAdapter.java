package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrowResultAdapter extends BaseAdapter {
    private Context context;
    private List<GrowHistory> growHistories;
    private Map<String,Boolean> failCheck = new HashMap<>();

    public GrowResultAdapter(Context context, List<GrowHistory> growHistories) {
        this.context = context;
        this.growHistories = growHistories;
        for(GrowHistory growHistory : growHistories) {
            for(String location : growHistory.getLocationList()) {
                failCheck.put(location,false);
            }
        }
    }

    @Override
    public int getCount() {
        return growHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return growHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GrowResultAdapter.ViewHolder viewHolder;
        if(convertView != null) {
            viewHolder = (GrowResultAdapter.ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_result,null);
            viewHolder = new GrowResultAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return convertView;
    }

    private class ViewHolder {
        private ImageView img;
        private CustomTextView name,startDate,expectedHarvestDate,failReport;
        private CheckBox selectAllBTN;
        private FlexboxLayout locationLayout;
        private LinearLayout expandToggle,expandLayout;
        private boolean isSelectAll = false, isExpand = true;

        public ViewHolder(View view) {
            this.img = view.findViewById(R.id.plant_img);
            this.name = view.findViewById(R.id.plant_name);
            this.startDate = view.findViewById(R.id.start_date);
            this.selectAllBTN = view.findViewById(R.id.check_select_all);
            this.locationLayout = view.findViewById(R.id.location_layout);
            this.expandToggle = view.findViewById(R.id.expand_toggle);
            this.expandLayout = view.findViewById(R.id.expand_layout);
            this.expectedHarvestDate = view.findViewById(R.id.expected_date);
            this.failReport = view.findViewById(R.id.label_fail);
        }

        public void bind(int position) {
            final GrowHistory growHistory = growHistories.get(position);
            Glide.with(context)
                    .load(ResourceManager.getDrawableID(context,"ic_plant_".concat(growHistory.getPlantName())))
                    .fitCenter()
                    .into(img);
            name.setText(growHistory.getPlantName());
            startDate.setText(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(growHistory.getStartDate()))));
            expectedHarvestDate.setText(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(growHistory.getExpectedHarvestDate()))));
            failReport.setText(String.valueOf(growHistory.getFailedList().size()).concat(" marked as failed"));
            expandToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isExpand=!isExpand;
                    if(isExpand) {
                        NavigationManager.expand(expandLayout);
                        ((ImageView)expandToggle.findViewById(R.id.expand)).setImageResource(ResourceManager.getDrawableID(context,"ic_expand_less"));
                    } else {
                        NavigationManager.collapse(expandLayout);
                        ((ImageView)expandToggle.findViewById(R.id.expand)).setImageResource(ResourceManager.getDrawableID(context,"ic_expand_more"));
                    }
                }
            });
            locationLayout.removeAllViews();
            for(final String location : growHistory.getLocationList()) {
//                Log.e("location size",growHistory.getLocationList().size()+"");
                final CustomTextView textView = new CustomTextView(context);
                textView.setTextColor(ResourceManager.getColor(context,failCheck.get(location)?R.color.white:R.color.gray));
                textView.setBackground(ResourceManager.getDrawable(context,failCheck.get(location)?"btn_small_corner_red":"bg_transparent"));
                textView.setPadding(5,5,5,5);
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(15,15,15,15);
                textView.setLayoutParams(params);
                textView.setText(location);
                locationLayout.addView(textView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!failCheck.get(location)) {
                            textView.setTextColor(ResourceManager.getColor(context, R.color.white));
                            textView.setBackground(ResourceManager.getDrawable(context,"btn_small_corner_red"));
                            failCheck.put(location,true);
                            Log.e("selected location",location);
                        } else {
                            textView.setTextColor(ResourceManager.getColor(context, R.color.gray));
                            textView.setBackground(ResourceManager.getDrawable(context,"bg_transparent"));
                            failCheck.put(location,false);
                            Log.e("deselected location",location);
                        }
                        textView.setPadding(5,5,5,5);
                        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(15,15,15,15);
                        textView.setLayoutParams(params);
                        List<String> failed = new ArrayList<>();
                        for(Map.Entry fail : failCheck.entrySet())
                            if((Boolean) fail.getValue()) failed.add((String) fail.getKey());
                        growHistory.setFailedList(failed);
                        notifyDataSetChanged();
                        List<String> harvested = new ArrayList<>();
                        for(Map.Entry fail : failCheck.entrySet())
                            if(!(Boolean) fail.getValue()) harvested.add((String) fail.getKey());
                        growHistory.setHarvestList(harvested);
                        notifyDataSetChanged();
                        failReport.setText(String.valueOf(growHistory.getFailedList().size()).concat(" marked as failed"));
                    }
                });
            }
            selectAllBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelectAll=!isSelectAll;
                    for(int i=0; i<locationLayout.getChildCount(); i++) {
                        CustomTextView textView = (CustomTextView) locationLayout.getChildAt(i);
                        textView.setTextColor(ResourceManager.getColor(context,isSelectAll?R.color.white:R.color.gray));
                        textView.setBackground(ResourceManager.getDrawable(context,isSelectAll?"btn_small_corner_red":"bg_transparent"));
                        textView.setPadding(5,5,5,5);
                        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(15,15,15,15);
                        textView.setLayoutParams(params);
                        failCheck.put(textView.getText().toString(),isSelectAll);
                    }
                    Log.e("selected location","all");
                    List<String> failed = new ArrayList<>();
                    for(Map.Entry fail : failCheck.entrySet())
                        if((Boolean) fail.getValue()) failed.add((String) fail.getKey());
                    growHistory.setFailedList(failed);
                    notifyDataSetChanged();
                    List<String> harvested = new ArrayList<>();
                    for(Map.Entry fail : failCheck.entrySet())
                        if(!(Boolean) fail.getValue()) harvested.add((String) fail.getKey());
                    growHistory.setHarvestList(harvested);
                    notifyDataSetChanged();
                    failReport.setText(String.valueOf(growHistory.getFailedList().size()).concat(" marked as failed"));
                    Log.e("fail size",growHistory.getFailedList().size()+"");
                }
            });
        }
    }
}
