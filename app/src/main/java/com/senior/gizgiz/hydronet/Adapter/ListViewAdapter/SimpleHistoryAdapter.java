package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.github.kevinsawicki.timeago.TimeAgo;
import com.senior.gizgiz.hydronet.ClassForList.HomeCard;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class SimpleHistoryAdapter extends BaseAdapter {
    private final Context context;
    private final List<GrowHistory> simpleHistories;
    public static List<GrowHistory> histories = new ArrayList<>();

//    static {
//        histories.add(new HomeCard(1,"tomato",1,"A1",32,3));
//        histories.add(new HomeCard(2,"cucumber",2,"D2",2,2));
//        histories.add(new HomeCard(3,"spinach",3,"A3",21,(float) 2.5));
//        histories.add(new HomeCard(4,"cucumber",4,"D4",26,1));
//        histories.add(new HomeCard(5,"tomato",5,"A5",45,(float) 2.6));
//        histories.add(new HomeCard(6,"spinach",6,"B6",1,(float) 1.2));
//    }

    public SimpleHistoryAdapter(Context context, List<GrowHistory> homeCards) {
        this.context = context;
        this.simpleHistories = homeCards;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SimpleHistoryAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (SimpleHistoryAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.selection_history,null);
            viewHolder = new SimpleHistoryAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }

    @Override
    public int getCount() {
        return simpleHistories.size();
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
        private CustomTextView harvestDate,date,promptHistory;
        private LinearLayout historyLayout;

        public ViewHolder(View view) {
//            this.historyLayout = view.findViewById(R.id.history_layout);
//            this.promptHistory = view.findViewById(R.id.prompt_history);
//            this.location = view.findViewById(R.id.location);
//            this.harvestDate = view.findViewById(R.id.harvest_date);
//            this.date = view.findViewById(R.id.day_passed);
//            this.count = view.findViewById(R.id.count);
//            this.health = view.findViewById(R.id.health);
        }

        public void bind(int position) {
//            GrowHistory card = simpleHistories.get(position);
//            if(card.getPlantName().equalsIgnoreCase("")) {
//                historyLayout.setVisibility(View.GONE);
//                promptHistory.setVisibility(View.VISIBLE);
//            } else {
//                historyLayout.setVisibility(View.VISIBLE);
//                promptHistory.setVisibility(View.GONE);
//            location.setText(card.getLocationList().get(0));
//                harvestDate.setText(ResourceManager.shortDateFormat.format(card.getHarvestDate()));
//                date.setText(" (");
//                date.append(new TimeAgo().timeAgo(card.getHarvestDate().getTime()) + ")");
//            date.append(ResourceManager.shortDateFormat.format(Calendar.getInstance().getTime())+")");
//            count.setText("x"+card.getCount());
//            health.setText("fresh");
//            }
        }
    }
}