package com.senior.gizgiz.hydronet.CustomClassAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.CustomHelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class HomeCardAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<HomeCard> homeCards;
    public static ArrayList<HomeCard> exampleCards = new ArrayList<>();

    private CustomTextView name,location,date,count;
    private ImageView img;
    private RatingBar health;

    static {
        exampleCards.add(new HomeCard(1,"tomato",3,"A5",32,3));
        exampleCards.add(new HomeCard(2,"cucumber",1,"D8",2,2));
        exampleCards.add(new HomeCard(3,"spinach",5,"A3",21,(float) 2.5));
    }

    public HomeCardAdapter(Context context, ArrayList<HomeCard> homeCards) {
        this.context = context;
        this.homeCards = homeCards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HomeCard card = homeCards.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.card_home, null);

            name = convertView.findViewById(R.id.plant_name);
            img = convertView.findViewById(R.id.plant_img);
            location = convertView.findViewById(R.id.location);
            date = convertView.findViewById(R.id.date);
            count = convertView.findViewById(R.id.plant_count_badge);
            health = convertView.findViewById(R.id.plant_health);

            name.setText(card.name);
            img.setImageResource(ResourceManager.getDrawableID(parent.getContext(),"ic_plant_"+card.name));
            location.setText(card.location);
            date.setText(card.date+"");
            date.append(card.date%10==1?"st":card.date%10==2?"nd":card.date%10==3?"rd":"th");
            date.append(" day");
            count.setText("x"+card.count);
            health.setRating(card.health);
        }
        return convertView;
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

    public static class HomeCard {
        private int id;
        private String name;
        private int count;
        private final String location;
        private int date;
        private float health;
        public HomeCard(int id,String name,int count,String location,int date,float health) {
            this.id = id;
            this.name = name;
            this.count = count;
            this.location = location;
            this.date = date;
            this.health = health;
        }
    }
}
