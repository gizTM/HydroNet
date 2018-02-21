package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class EquipmentAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Item> partOverviewCards;
    public static ArrayList<Item> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new Item("e1","Arduino board", 500,
                "basic model: UNO R3;used as farm controller"));
        exampleCards.add(new Item("e2","Raspberry pi", 2000,
                "basic model: Pi3;used to connect to internet"));
        exampleCards.add(new Item("e3","ESP8266-01 module",100,
                "basic model: model01;used to connect to internet (alternatives to rpi)"));
        exampleCards.add(new Item("e4","Ultra sonic sensor",50,
                "model: HC-SR04;used to measure farm water level;"));
        exampleCards.add(new Item("e5","pH sensor",2000,
                "model: ...;used to measure farm pH level"));
        exampleCards.add(new Item("e6","EC sensor",3000,
                "model: ...;used to measure farm EC level"));
        exampleCards.add(new Item("e7","Ultra sonic sensor",350,
                "model: JSN-SR04T;used to measure farm water level;*waterproof"));
    }

    public EquipmentAdapter(Context context, ArrayList<Item> partOverviewCards) {
        this.context = context;
        this.partOverviewCards = partOverviewCards;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        EquipmentAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (EquipmentAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_equipment,null);
            viewHolder = new EquipmentAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }

    @Override
    public int getCount() {
        return partOverviewCards.size();
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
        private CustomTextView name,detail;
        private ImageView img;
        private SpannableStringBuilder bulletBuilder;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.equipment_name);
            this.detail = view.findViewById(R.id.equipment_detail);
            if(img != null) ((BitmapDrawable) img.getDrawable()).getBitmap().recycle();
            this.img = view.findViewById(R.id.equipment_img);
        }

        public void bind(int position) {
            Item card = partOverviewCards.get(position);
//            img.setImageResource(ResourceManager.getDrawableID(context,"ic_"+card.getId()));
            Glide.with(context).
                    load(ResourceManager.getDrawableID(context,"ic_"+card.getId()))
                    .fitCenter()
                    .into(img);
            name.setText(card.getName());
            String spannableString = ResourceManager.getSeparateString(card.getDetail());
            bulletBuilder = new SpannableStringBuilder(spannableString);
            ResourceManager.showBullet(bulletBuilder,spannableString);
            detail.setText(bulletBuilder);
        }
    }
}
