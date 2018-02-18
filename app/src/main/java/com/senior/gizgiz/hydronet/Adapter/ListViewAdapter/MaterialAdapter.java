package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.senior.gizgiz.hydronet.ClassForList.MaterialCard;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class MaterialAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<MaterialCard> partOverviewCards;
    public static ArrayList<MaterialCard> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new MaterialCard(1,"Pot for hydroponics gardening",""));
//        exampleCards.add(new MaterialCard(2,"Raspberry pi 3",""));
//        exampleCards.add(new MaterialCard(3,"ESP8266-01 wifi module",""));
//        exampleCards.add(new MaterialCard(4,"Ultra sonic sensor",""));
//        exampleCards.add(new MaterialCard(5,"pH sensor",""));
//        exampleCards.add(new MaterialCard(6,"EC sensor",""));
    }

    public MaterialAdapter(Context context, ArrayList<MaterialCard> partOverviewCards) {
        this.context = context;
        this.partOverviewCards = partOverviewCards;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MaterialAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (MaterialAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_material,null);
            viewHolder = new MaterialAdapter.ViewHolder(view);
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
            this.name = view.findViewById(R.id.material_name);
            this.detail = view.findViewById(R.id.material_detail);
            this.img = view.findViewById(R.id.material_img);
        }

        public void bind(int position) {
            MaterialCard card = partOverviewCards.get(position);
            img.setImageResource(ResourceManager.getDrawableID(context,"ic_material_"+card.getId()));
            name.setText(card.getName());
            detail.setText(card.getDetail());
        }
    }
}
