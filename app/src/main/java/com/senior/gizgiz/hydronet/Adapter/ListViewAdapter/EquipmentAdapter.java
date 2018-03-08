package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
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
import java.util.List;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class EquipmentAdapter extends BaseAdapter {
    private final Context context;
    private List<Item> partOverviewCards;
    public static List<Item> equipments = new ArrayList<>();

    public EquipmentAdapter(Context context, List<Item> partOverviewCards) {
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
