package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.github.kevinsawicki.timeago.TimeAgo;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.senior.gizgiz.hydronet.ClassForList.HistoryCard;
import com.senior.gizgiz.hydronet.ClassForList.HomeCard;
import com.senior.gizgiz.hydronet.ClassForList.PlantLocation;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class HistoryAdapter extends BaseAdapter {
    private final Context context;
//    private final List<HomeCard> homeCards;
//    public static List<HomeCard> histories = new ArrayList<>();
    private List<HistoryCard> histories = new ArrayList<>();

    public HistoryAdapter(Context context, List<HistoryCard> historyCards) {
        this.context = context;
        this.histories = historyCards;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        HistoryAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (HistoryAdapter.ViewHolder) view.getTag();
        } else {
//            view = LayoutInflater.from(context).inflate(R.layout.card_home,null);
            view = LayoutInflater.from(context).inflate(R.layout.card_history,null);
            viewHolder = new HistoryAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }

    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    private class ViewHolder {
        private CustomTextView title,startDate,plantNum;
        private RelativeLayout toggle;
        private LinearLayout detail;
//        private ListView plantList;
        private boolean isExpand = false;

        public ViewHolder(View view) {
            this.title = view.findViewById(R.id.history_title);
            this.startDate = view.findViewById(R.id.history_startDate);
            this.plantNum = view.findViewById(R.id.history_num_plant);
            this.toggle = view.findViewById(R.id.toggle_detail_layout);
            this.detail = view.findViewById(R.id.card_detail_layout);
//            this.plantList = view.findViewById(R.id.plant_location_list);
        }

        public void bind(int position) {
            HistoryCard card = histories.get(position);
            title.setText(card.getTitle());
            startDate.setText("Planted on ".concat(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(card.getHistories().get(0).getStartDate()))))
                    .concat(" (").concat(new TimeAgo().timeAgo(Long.valueOf(card.getHistories().get(0).getStartDate()))).concat(")"));
            plantNum.setText(String.valueOf(card.getHistories().size()).concat(" plants"));
            detail.removeAllViews();
            for(GrowHistory growHistory : card.getHistories()) {
                Log.e("history",growHistory.getPlantName()+" "+growHistory.getCount());
                View child = LayoutInflater.from(context).inflate(R.layout.card_plant_location,null);
                ImageView img = child.findViewById(R.id.location_plant_img);
                Glide.with(context)
                        .load(ResourceManager.getDrawableID(context,"ic_plant_".concat(growHistory.getPlantName())))
                        .fitCenter()
                        .into(img);
                CustomTextView location = child.findViewById(R.id.location_plant_location);
                String locations = "";
                for(String unit : growHistory.getLocationList()) locations+=unit+", ";
                location.setText(locations);
                detail.addView(child);
            }
            toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isExpand) {
                        NavigationManager.expand(detail);
                        isExpand = true;
                    } else {
                        NavigationManager.collapse(detail);
                        isExpand = false;
                    }
                }
            });
        }
    }
}