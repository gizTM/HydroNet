package com.senior.gizgiz.hydronet.Adapter.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 2018/02/06.
 */

public class PlantRecyclerViewAdapter extends RecyclerView.Adapter<PlantRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Plant> systemDefaultPlants;
    public static List<Plant> exampleSystemPlants = new ArrayList<>();

    static {
        exampleSystemPlants.add(new Plant("tomato"));
        exampleSystemPlants.add(new Plant("cucumber"));
        exampleSystemPlants.add(new Plant("spinach"));
        exampleSystemPlants.add(new Plant("tomato"));
        exampleSystemPlants.add(new Plant("cucumber"));
        exampleSystemPlants.add(new Plant("spinach"));
    }

    public PlantRecyclerViewAdapter(Context context, List<Plant> systemDefaultPlants) {
        this.context = context;
        this.systemDefaultPlants = systemDefaultPlants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_plant, parent, false);
        return new PlantRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Plant sysPlant = systemDefaultPlants.get(position);
        holder.img.setImageResource(ResourceManager.getDrawableID(context,"ic_plant_"+sysPlant.getName()));
        holder.name.setText(sysPlant.getName());
    }

    @Override
    public int getItemCount() {
        return systemDefaultPlants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private CustomTextView name;

        public ViewHolder(View view) {
            super(view);
            this.img = view.findViewById(R.id.plant_thumbnail);
            this.name = view.findViewById(R.id.plant_name);
        }
    }
}
