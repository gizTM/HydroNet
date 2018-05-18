package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.senior.gizgiz.hydronet.ClassForList.PlantLocation;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 018 18/03/2018.
 */

public class PlantLocationAdapter extends BaseAdapter {
    private Context context;
    private List<PlantLocation> plantLocations = new ArrayList<>();

    public PlantLocationAdapter(Context context, List<PlantLocation> plantLocations) {
        this.context = context;
        this.plantLocations = plantLocations;
    }

    @Override public int getCount() { return plantLocations.size(); }
    @Override public Object getItem(int i) { return plantLocations.get(i); }
    @Override  public long getItemId(int i) { return 0; }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PlantLocationAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (PlantLocationAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_plant_location,null);
            viewHolder = new PlantLocationAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(i);
        return view;
    }
    private class ViewHolder {
        private CustomTextView location;
        private ImageView img;

        public ViewHolder(View view) {
            this.img = view.findViewById(R.id.location_plant_img);
            this.location = view.findViewById(R.id.location_plant_location);
        }

        public void bind(int position) {
            PlantLocation card = plantLocations.get(position);
            Glide.with(context)
                    .load(ResourceManager.getDrawableID(context,"ic_plant_"+card.getPlantName()))
                    .fitCenter()
                    .into(img);
            String locations = "";
            for(String location : card.getLocationList()) locations+=location+", ";
            location.setText(locations);
        }
    }
}
