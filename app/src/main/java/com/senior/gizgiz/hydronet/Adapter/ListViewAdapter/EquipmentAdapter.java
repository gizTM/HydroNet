package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.senior.gizgiz.hydronet.ClassForList.EquipmentCard;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class EquipmentAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<EquipmentCard> partOverviewCards;
    public static ArrayList<EquipmentCard> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new EquipmentCard(1,"Arduino UNO R3","\t-detail1\n\t-detail2"));
        exampleCards.add(new EquipmentCard(2,"Raspberry pi 3",""));
        exampleCards.add(new EquipmentCard(3,"ESP8266-01 wifi module",""));
        exampleCards.add(new EquipmentCard(4,"Ultra sonic sensor",""));
        exampleCards.add(new EquipmentCard(5,"pH sensor",""));
        exampleCards.add(new EquipmentCard(6,"EC sensor",""));
    }

    public EquipmentAdapter(Context context, ArrayList<EquipmentCard> partOverviewCards) {
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
        private CustomTextView name, detail;
        private ImageView img;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.equipment_name);
            this.detail = view.findViewById(R.id.equipment_detail);
            this.img = view.findViewById(R.id.equipment_img);
        }

        public void bind(int position) {
            EquipmentCard card = partOverviewCards.get(position);
            img.setImageResource(ResourceManager.getDrawableID(context,"ic_equip_"+card.getId()));
            name.setText(card.getName());
            detail.setText(card.getDetail());
        }
    }
}
