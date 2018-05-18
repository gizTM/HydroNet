package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 018 18/02/2018.
 */

public class PlantOverviewAdapter extends BaseAdapter {
    private final Context context;
    private final List<UserPlant> plantOverviewCards;
    private int numGrowing,numHarvested,numFailed;

    public PlantOverviewAdapter(Context context, List<UserPlant> plantOverviewCards) {
        this.context = context;
        this.plantOverviewCards = plantOverviewCards;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PlantOverviewAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (PlantOverviewAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_plant_overview,null);
            viewHolder = new PlantOverviewAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }

    @Override public int getCount() {
        return plantOverviewCards.size();
    }
    @Override public long getItemId(int position) {
        return 0;
    }
    @Override public Object getItem(int position) {
        return null;
    }

    public List<Integer> getStat() {
        List<Integer> stat = new ArrayList<>();
        stat.add(numGrowing); stat.add(numHarvested); stat.add(numFailed);
        return stat;
    }

    private class ViewHolder {
        private CustomTextView id, name, growing, harvested, failed, total;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.plant_name);
            this.growing = view.findViewById(R.id.num_growing);
            this.harvested = view.findViewById(R.id.num_harvested);
            this.failed = view.findViewById(R.id.num_failed);
            this.total = view.findViewById(R.id.num_total);
        }

        public void bind(int position) {
            Plant card = plantOverviewCards.get(position);
            List<GrowHistory> histories = ((UserPlant) card).getGrowHistories();
            numGrowing=0; numHarvested=0; numFailed=0;
            for (GrowHistory history : histories) {
                if(history.getResult().equalsIgnoreCase("failed")) numFailed+=history.getCount();
                else if(history.isHarvested()) numHarvested+=history.getCount();
                else numGrowing+=history.getCount();
            }
//            id.setText(card.getId().substring(2));
            name.setText(card.getName());
            growing.setText(String.valueOf(numGrowing));
            harvested.setText(String.valueOf(numHarvested));
            failed.setText(String.valueOf(numFailed));
            total.setText(String.valueOf((numGrowing+numHarvested+numFailed)));
        }
    }
}
