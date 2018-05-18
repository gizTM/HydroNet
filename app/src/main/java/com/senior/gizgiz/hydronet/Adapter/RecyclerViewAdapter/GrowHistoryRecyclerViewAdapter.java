package com.senior.gizgiz.hydronet.Adapter.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.List;

/**
 * Created by Admins on 2018/02/06.
 */

public class GrowHistoryRecyclerViewAdapter extends RecyclerView.Adapter<GrowHistoryRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<GrowHistory> growHistoryList;

    public GrowHistoryRecyclerViewAdapter(Context context, List<GrowHistory> systemDefaultPlants) {
        this.context = context;
        this.growHistoryList = systemDefaultPlants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_added_plant, parent, false);
        return new GrowHistoryRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GrowHistory growHistory = growHistoryList.get(position);
        holder.img.setImageResource(ResourceManager.getDrawableID(context,"ic_plant_"+growHistory.getPlantName()));
        holder.count.setText("x".concat(String.valueOf(growHistory.getCount())));
    }

    @Override
    public int getItemCount() {
        return growHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private CustomTextView count;

        public ViewHolder(View view) {
            super(view);
            this.img = view.findViewById(R.id.added_plant_img);
            this.count = view.findViewById(R.id.added_plant_count);
        }
    }
}
