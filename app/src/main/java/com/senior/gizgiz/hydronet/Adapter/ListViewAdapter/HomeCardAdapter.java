package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.senior.gizgiz.hydronet.ClassForList.HomeCard;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class HomeCardAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<HomeCard> homeCards;
    public static ArrayList<HomeCard> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new HomeCard(1,"tomato",1,"A1",32,3));
        exampleCards.add(new HomeCard(2,"cucumber",2,"D2",2,2));
        exampleCards.add(new HomeCard(3,"spinach",3,"A3",21,(float) 2.5));
        exampleCards.add(new HomeCard(4,"cucumber",4,"D4",26,1));
        exampleCards.add(new HomeCard(5,"tomato",5,"A5",45,(float) 2.6));
        exampleCards.add(new HomeCard(6,"spinach",6,"B6",1,(float) 1.2));
    }

    public HomeCardAdapter(Context context, ArrayList<HomeCard> homeCards) {
        this.context = context;
        this.homeCards = homeCards;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        HomeCardAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (HomeCardAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_home,null);
            viewHolder = new HomeCardAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }

    @Override
    public int getCount() {
        return homeCards.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    private class ViewHolder {
        private CustomTextView name,location,date,count;
        private ImageView img;
        private RatingBar health;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.plant_name);
            this.img = view.findViewById(R.id.plant_img);
            this.location = view.findViewById(R.id.location);
            this.date = view.findViewById(R.id.date);
            this.count = view.findViewById(R.id.plant_count_badge);
            this.health = view.findViewById(R.id.plant_health);
        }

        public void bind(int position) {
            HomeCard card = homeCards.get(position);
            name.setText(card.getName());
            img.setImageResource(ResourceManager.getDrawableID(context,"ic_plant_"+card.getName()));
            location.setText(card.getLocation());
            date.setText(card.getDate()+"");
            date.append(card.getDate()%10==1?"st":card.getDate()%10==2?"nd":card.getDate()%10==3?"rd":"th");
            date.append(" day");
            count.setText("x"+card.getCount());
            health.setRating(card.getHealth());
        }
    }
}