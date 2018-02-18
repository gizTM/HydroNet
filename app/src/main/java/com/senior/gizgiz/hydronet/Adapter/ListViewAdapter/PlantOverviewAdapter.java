package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.ClassForList.PlantOverviewCard;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;

/**
 * Created by Admins on 018 18/02/2018.
 */

public class PlantOverviewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<PlantOverviewCard> plantDetailCards;
    public static ArrayList<PlantOverviewCard> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new PlantOverviewCard(1,"tomato",1,5,2));
        exampleCards.add(new PlantOverviewCard(2,"cucumber",5,2,2));
        exampleCards.add(new PlantOverviewCard(3,"spinach",3,21,5));
        exampleCards.add(new PlantOverviewCard(4,"cucumber",4,1,0));
        exampleCards.add(new PlantOverviewCard(5,"tomato",5,45,6));
        exampleCards.add(new PlantOverviewCard(6,"spinach",6,1,2));
    }

    public PlantOverviewAdapter(Context context, ArrayList<PlantOverviewCard> homeCards) {
        this.context = context;
        this.plantDetailCards = homeCards;
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

    @Override
    public int getCount() {
        return plantDetailCards.size();
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
        private CustomTextView id, name, growing, harvested, failed, total;

        public ViewHolder(View view) {
            this.id = view.findViewById(R.id.plant_id);
            this.name = view.findViewById(R.id.plant_name);
            this.growing = view.findViewById(R.id.num_growing);
            this.harvested = view.findViewById(R.id.num_harvested);
            this.failed = view.findViewById(R.id.num_failed);
            this.total = view.findViewById(R.id.num_total);
        }

        public void bind(int position) {
            PlantOverviewCard card = plantDetailCards.get(position);
            id.setText(card.getId()+"");
            name.setText(card.getName());
            growing.setText("x"+card.getGrowing());
            harvested.setText("x"+card.getHarvested());
            failed.setText("x"+card.getFailed());
            total.setText("x"+(card.getGrowing()+card.getHarvested()+card.getFailed()));
        }
    }
}
