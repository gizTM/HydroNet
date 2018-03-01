package com.senior.gizgiz.hydronet.Adapter.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Activity.AddPlantActivity;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.LocationAdapter;
import com.senior.gizgiz.hydronet.ClassForList.DropdownItem;
import com.senior.gizgiz.hydronet.ClassForList.ToGrowPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Listener.RecyclerTouchListener;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class PlantBadgeRecyclerViewAdapter extends RecyclerView.Adapter<PlantBadgeRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final List<ToGrowPlant> plantBadges;

    public PlantBadgeRecyclerViewAdapter(Context context, List<ToGrowPlant> plantBadge) {
        this.context = context;
        this.plantBadges = plantBadge;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.badge_added_plant, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // set view holder for each card
        ToGrowPlant badge = plantBadges.get(position);
        viewHolder.img.setImageResource(ResourceManager.getDrawableID(context,"ic_plant_"+badge.getPlant().getName()));
        viewHolder.count.setText("x"+badge.getCount());
    }

    @Override
    public int getItemCount() {
        return plantBadges.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView count;
        private ImageView img;

        private ViewHolder(View view) {
            super(view);
            this.img = view.findViewById(R.id.added_plant_img);
            this.count = view.findViewById(R.id.added_plant_count);
            view.setTag(view);
        }

    }
}
