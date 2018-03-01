package com.senior.gizgiz.hydronet.Adapter.GridViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.SystemDefaultPlant;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Admins on 2018/02/06.
 */

public class PlantAdapter extends BaseAdapter {
    private Context context;
    private List<Plant> plantList;
    public static List<Plant> exampleSystemPlants = new ArrayList<>();
    public static List<Plant> exampleUserPlants = new ArrayList<>();

    static {
        exampleSystemPlants.add(new SystemDefaultPlant("sp1","tomato"));
        exampleSystemPlants.add(new SystemDefaultPlant("sp2","cucumber"));
        exampleSystemPlants.add(new SystemDefaultPlant("sp3","spinach"));
        exampleSystemPlants.add(new SystemDefaultPlant("sp4","salad"));
        exampleSystemPlants.add(new SystemDefaultPlant("sp5","strawberry"));
        exampleSystemPlants.add(new SystemDefaultPlant("sp6","celery"));
        exampleSystemPlants.add(new SystemDefaultPlant("sp7","carrot"));

        exampleUserPlants.add(new UserPlant("up1","cucumber",createMockGrowHistory(4)));
        exampleUserPlants.add(new UserPlant("up2","spinach",createMockGrowHistory(6)));
        exampleUserPlants.add(new UserPlant("up3","salad",createMockGrowHistory(2)));
        exampleUserPlants.add(new UserPlant("up4","celery",createMockGrowHistory(12)));
        exampleUserPlants.add(new UserPlant("up5","carrot",createMockGrowHistory(9)));
        exampleUserPlants.add(new UserPlant("up6","chili",createMockGrowHistory(5)));
    }

    public PlantAdapter(Context context, List<Plant> plantList) {
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
        PlantAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (PlantAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_plant, null);
            viewHolder = new PlantAdapter.ViewHolder(view);
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

    private static ArrayList<GrowHistory> createMockGrowHistory(int total) {
        ArrayList<GrowHistory> mockHistory = new ArrayList<>();
        Random rand = new Random();
        for (int i=0; i<total; i++) {
            int count = rand.nextInt(10)+1;
            boolean failed = rand.nextBoolean();
            boolean harvested = rand.nextBoolean();
            GrowHistory temp = new GrowHistory();
            temp.setCount(count);
            temp.setResult(failed?"success":"failed");
            temp.setHarvested(harvested);
            mockHistory.add(temp);
        }
        return mockHistory;
    }
}
