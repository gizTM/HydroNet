package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.ClassForList.PartOverviewCard;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Admins on 018 18/02/2018.
 */

public class PartOverviewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<PartOverviewCard> partOverviewCards;
    public static ArrayList<PartOverviewCard> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new PartOverviewCard(1,"Arduino UNO R3",500));
        exampleCards.add(new PartOverviewCard(2,"Raspberry pi 3",2000));
        exampleCards.add(new PartOverviewCard(3,"ESP8266-01 wifi module",100));
        exampleCards.add(new PartOverviewCard(4,"Ultra sonic sensor",1000));
        exampleCards.add(new PartOverviewCard(5,"pH sensor",2000));
        exampleCards.add(new PartOverviewCard(6,"EC sensor",3000));
    }

    public PartOverviewAdapter(Context context, ArrayList<PartOverviewCard> partOverviewCards) {
        this.context = context;
        this.partOverviewCards = partOverviewCards;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PartOverviewAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (PartOverviewAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_part_overview,null);
            viewHolder = new PartOverviewAdapter.ViewHolder(view);
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
            PartOverviewCard card = partOverviewCards.get(position);
            id.setText(card.getId()+"");
            name.setText(card.getName());
            DecimalFormat decimalFormat = new DecimalFormat("฿###,###.###");
            cost.setText(decimalFormat.format(card.getCost()));
        }
    }
}
