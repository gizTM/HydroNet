package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.Entity.ExchangeTransaction;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.TradeInfo;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

import java.util.List;

/**
 * Created by Admins on 005 05/03/2018.
 */

public class TradeDetailAdapter extends BaseAdapter {
    private final Context context;
    private final List<TradeInfo> tradeDetailList;

    public TradeDetailAdapter(Context context, List<TradeInfo> tradeInfos) {
        this.context = context;
        this.tradeDetailList = tradeInfos;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TradeDetailAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (TradeDetailAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_trade_detail,null);
            viewHolder = new TradeDetailAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }

    @Override
    public int getCount() {
        return tradeDetailList.size();
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
        private CustomTextView id, name, negotiating, bought, onSale, sold, total;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.plant_name);
            this.negotiating = view.findViewById(R.id.num_negotiating);
            this.bought = view.findViewById(R.id.num_bought);
            this.onSale = view.findViewById(R.id.num_on_sale);
            this.sold = view.findViewById(R.id.num_sold);
            this.total = view.findViewById(R.id.num_total);
        }

        public void bind(int position) {
            TradeInfo card = tradeDetailList.get(position);
            List<ExchangeTransaction> transactions = card.getUserTransaction();
            List<ProductAnnouncementStory> announcementStories = card.getUserSaleStory();
            int numNegotiating=0,numBought=0,numOnSale=0,numSold=0;
            for (ExchangeTransaction transaction : transactions) {
                if(transaction.getExchangePlant().equals(card.getUserPlant())) {
                    if (transaction.getBuyer().getUsername().equalsIgnoreCase("gizgiz"))
                        numBought++;
                    else if (transaction.getSeller().getUsername().equalsIgnoreCase("gizgiz"))
                        numSold++;
                }
            }
            for (ProductAnnouncementStory story : announcementStories) {
                if(story.getMentionedPlant().equals(card.getUserPlant())) {
//                    if(story.get)
                }
            }
            id.setText(card.getId().substring(2));
            name.setText(card.getUserPlant().getName());
            negotiating.setText(numNegotiating);
            bought.setText(numBought);
            onSale.setText(numOnSale);
            total.setText(numNegotiating+numBought+numOnSale);
        }
    }
}
