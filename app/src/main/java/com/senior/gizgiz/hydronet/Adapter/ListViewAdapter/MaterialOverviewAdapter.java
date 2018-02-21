package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class MaterialOverviewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Item> partOverviewCards;

    public MaterialOverviewAdapter(Context context, ArrayList<Item> partOverviewCards) {
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
        private CustomTextView id, name, cost;

        public ViewHolder(View view) {
            this.id = view.findViewById(R.id.part_id);
            this.name = view.findViewById(R.id.part_name);
            this.cost = view.findViewById(R.id.part_cost);
        }

        public void bind(int position) {
            Item card = partOverviewCards.get(position);
            id.setText(card.getId().substring(1));
            name.setText(card.getName());
            DecimalFormat decimalFormat = new DecimalFormat("฿###,###.###");
            cost.setText(decimalFormat.format(card.getCost()));
        }
    }
}
