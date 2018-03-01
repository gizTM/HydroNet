package com.senior.gizgiz.hydronet.Adapter.GridViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.List;

/**
 * Created by Admins on 021 21/02/2018.
 */

public class ChangePlantAdapter extends BaseAdapter {
    private Context context;
    private List<Plant> plantList;

    public ChangePlantAdapter(Context context, List<Plant> plantList) {
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
        ChangePlantAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (ChangePlantAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_change_plant, null);
            viewHolder = new ChangePlantAdapter.ViewHolder(view);
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
//            img.setImageResource(ResourceManager.getDrawableID(context,"ic_plant_"+plant.getName()));
            Glide.with(context)
                    .load(ResourceManager.getDrawableID(context,"ic_plant_"+plant.getName()))
                    .fitCenter()
                    .into(img);
        }
    }
}
