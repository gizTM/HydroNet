package com.senior.gizgiz.hydronet.Adapter.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.senior.gizgiz.hydronet.ClassForList.HomeCard;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class HomeCardRecyclerViewAdapter extends RecyclerView.Adapter<HomeCardRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final List<HomeCard> homeCards;
    public static List<HomeCard> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new HomeCard(1,"tomato",1,"A1",32,3));
        exampleCards.add(new HomeCard(2,"cucumber",2,"D2",2,2));
        exampleCards.add(new HomeCard(3,"spinach",3,"A3",21,(float) 2.5));
        exampleCards.add(new HomeCard(4,"cucumber",4,"D4",26,1));
        exampleCards.add(new HomeCard(5,"tomato",5,"A5",45,(float) 2.6));
        exampleCards.add(new HomeCard(6,"spinach",6,"B6",1,(float) 1.2));
    }

    public HomeCardRecyclerViewAdapter(Context context, List<HomeCard> homeCards) {
        this.context = context;
        this.homeCards = homeCards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_home, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // set view holder for each card
        HomeCard card = homeCards.get(position);
        viewHolder.name.setText(card.getName());
        viewHolder.img.setImageResource(ResourceManager.getDrawableID(context,"ic_plant_"+card.getName()));
        viewHolder.location.setText(card.getLocation());
        viewHolder.date.setText(card.getDate()+"");
        viewHolder.date.append(card.getDate()%10==1?"st":card.getDate()%10==2?"nd":card.getDate()%10==3?"rd":"th");
        viewHolder.date.append(" day");
        viewHolder.count.setText("x"+card.getCount());
        viewHolder.health.setRating(card.getHealth());
    }

    @Override
    public int getItemCount() {
        return homeCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView name,location,date,count;
        public ImageView img;
        public RatingBar health;

        private ViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.plant_name);
            this.img = view.findViewById(R.id.plant_img);
            this.location = view.findViewById(R.id.location);
            this.date = view.findViewById(R.id.date);
            this.count = view.findViewById(R.id.plant_count_badge);
            this.health = view.findViewById(R.id.plant_health);
        }
    }

}
