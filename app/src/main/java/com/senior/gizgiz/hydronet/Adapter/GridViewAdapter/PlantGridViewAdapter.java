package com.senior.gizgiz.hydronet.Adapter.GridViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class PlantGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Plant> plantList;
    public static List<Plant> exampleSystemPlants = new ArrayList<>();
    public static List<Plant> exampleUserPlants = new ArrayList<>();

    static {
        exampleSystemPlants.add(new Plant("tomato"));
        exampleSystemPlants.add(new Plant("tomato"));
        exampleSystemPlants.add(new Plant("tomato"));
        exampleSystemPlants.add(new Plant("cucumber"));
        exampleSystemPlants.add(new Plant("cucumber"));
        exampleSystemPlants.add(new Plant("cucumber"));

        exampleUserPlants.add(new Plant("salad"));
        exampleUserPlants.add(new Plant("salad"));
        exampleUserPlants.add(new Plant("salad"));
        exampleUserPlants.add(new Plant("tomato"));
        exampleUserPlants.add(new Plant("tomato"));
        exampleUserPlants.add(new Plant("tomato"));
    }

    public PlantGridViewAdapter(Context context, List<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    @Override
    public int getCount() { return plantList.size(); }

    @Override
    public Object getItem(int i) { return plantList.get(i); }

    @Override
    public long getItemId(int i) { return 0; }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        PlantGridViewAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (PlantGridViewAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_plant, null);
            viewHolder = new PlantGridViewAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }
    private class ViewHolder {
        private ImageView img;
        private CustomTextView name;

        public ViewHolder(View view) {
            this.img = view.findViewById(R.id.plant_thumbnail);
            this.name = view.findViewById(R.id.plant_name);
        }

        public void bind(int position) {
            Plant plant = plantList.get(position);
            name.setText(plant.getName());
            img.setImageResource(ResourceManager.getDrawableID(context,"ic_plant_"+plant.getName()));
        }
    }
}
