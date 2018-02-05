package com.senior.gizgiz.hydronet.CustomClassAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.senior.gizgiz.hydronet.CustomClassForList.HomeCard;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.CustomHelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.CustomHelperClass.SquareThumbnail;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class HomeCardRecyclerViewAdapter extends RecyclerView.Adapter<HomeCardRecyclerViewAdapter.HomeViewHolder> {
    private final Context context;
    private final List<HomeCard> homeCards;
    public static List<HomeCard> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new HomeCard(1,"tomato",3,"A5",32,3));
        exampleCards.add(new HomeCard(2,"cucumber",1,"D8",2,2));
        exampleCards.add(new HomeCard(3,"spinach",5,"A3",21,(float) 2.5));
    }

    public HomeCardRecyclerViewAdapter(Context context, List<HomeCard> homeCards) {
        this.context = context;
        this.homeCards = homeCards;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_home, viewGroup, false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder viewHolder, int position) {
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

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView name,location,date,count;
        public ImageView img;
        public RatingBar health;

        private HomeViewHolder(View view) {
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
