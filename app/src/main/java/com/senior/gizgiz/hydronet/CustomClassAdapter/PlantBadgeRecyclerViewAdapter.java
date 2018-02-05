package com.senior.gizgiz.hydronet.CustomClassAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.senior.gizgiz.hydronet.CustomClassForList.PlantBadge;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.CustomHelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.List;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class PlantBadgeRecyclerViewAdapter extends RecyclerView.Adapter<PlantBadgeRecyclerViewAdapter.PlantBadgeViewHolder> {
    private final Context context;
    private final List<PlantBadge> plantBadges;

    public PlantBadgeRecyclerViewAdapter(Context context, List<PlantBadge> plantBadge) {
        this.context = context;
        this.plantBadges = plantBadge;
    }

    @Override
    public PlantBadgeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.badge_added_plant, viewGroup, false);
        return new PlantBadgeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlantBadgeRecyclerViewAdapter.PlantBadgeViewHolder viewHolder, int position) {
        // set view holder for each card
        PlantBadge badge = plantBadges.get(position);
        viewHolder.img.setImageResource(ResourceManager.getDrawableID(context,"ic_plant_"+badge.getPlant().getName()));
        viewHolder.count.setText("x"+badge.getCount());
    }

    @Override
    public int getItemCount() {
        return plantBadges.size();
    }

    public static class PlantBadgeViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView name,count;
        public ImageView img;

        private PlantBadgeViewHolder(View view) {
            super(view);
            this.img = view.findViewById(R.id.added_plant_img);
            this.count = view.findViewById(R.id.added_plant_count);
        }
    }
}
