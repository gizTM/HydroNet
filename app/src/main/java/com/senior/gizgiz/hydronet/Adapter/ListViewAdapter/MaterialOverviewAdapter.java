package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.ClassForList.MaterialOverviewCard;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class MaterialOverviewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<MaterialOverviewCard> partOverviewCards;
    public static ArrayList<MaterialOverviewCard> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new MaterialOverviewCard(1,"Pot for hydroponics gardening",50));
//        exampleCards.add(new MaterialOverviewCard(2,"Raspberry pi 3",2000));
//        exampleCards.add(new MaterialOverviewCard(3,"ESP8266-01 wifi module",100));
//        exampleCards.add(new MaterialOverviewCard(4,"Ultra sonic sensor",1000));
//        exampleCards.add(new MaterialOverviewCard(5,"pH sensor",2000));
//        exampleCards.add(new MaterialOverviewCard(6,"EC sensor",3000));
    }

    public MaterialOverviewAdapter(Context context, ArrayList<MaterialOverviewCard> partOverviewCards) {
        this.context = context;
        this.partOverviewCards = partOverviewCards;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MaterialOverviewAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (MaterialOverviewAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_part_overview,null);
            viewHolder = new MaterialOverviewAdapter.ViewHolder(view);
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
        private CustomTextView id, name, cost, totalCost;

        public ViewHolder(View view) {
            this.id = view.findViewById(R.id.part_id);
            this.name = view.findViewById(R.id.part_name);
            this.cost = view.findViewById(R.id.part_cost);
        }

        public void bind(int position) {
            MaterialOverviewCard card = partOverviewCards.get(position);
            id.setText(card.getId()+"");
            name.setText(card.getName());
            DecimalFormat decimalFormat = new DecimalFormat("฿###,###.###");
            cost.setText(decimalFormat.format(card.getCost()));
        }
    }
}
