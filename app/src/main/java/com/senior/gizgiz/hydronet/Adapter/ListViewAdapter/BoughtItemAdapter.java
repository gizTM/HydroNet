package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Entity.ExchangeTransaction;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Admins on 002 02/03/2018.
 */

public class BoughtItemAdapter extends BaseAdapter {
    private Context context;
    private List<ExchangeTransaction> boughtItems;
    public static List<ExchangeTransaction> exampleCards = new ArrayList<>();

    private static Date createMockTimeStamp() {
        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, rand.nextInt(1)+2018);
        cal.set(Calendar.MONTH, rand.nextInt(12)+1);
        cal.set(Calendar.DAY_OF_MONTH, rand.nextInt(31)+1);
        return cal.getTime();
    }

    static {
        exampleCards.add(new ExchangeTransaction("b1", (UserPlant)PlantAdapter.exampleUserPlants.get(0),3,
                new User("gizgiz"), new User("fernfern"),100,createMockTimeStamp()));
        exampleCards.add(new ExchangeTransaction("b2", (UserPlant)PlantAdapter.exampleUserPlants.get(1),4,
                new User("gizgiz"), new User("fernfern"),90,createMockTimeStamp()));
        exampleCards.add(new ExchangeTransaction("b3", (UserPlant)PlantAdapter.exampleUserPlants.get(2),9,
                new User("gizgiz"), new User("fernfern"),120,createMockTimeStamp()));
        exampleCards.add(new ExchangeTransaction("b4", (UserPlant)PlantAdapter.exampleUserPlants.get(3),2,
                new User("gizgiz"), new User("fernfern"),50,createMockTimeStamp()));
        exampleCards.add(new ExchangeTransaction("b5", (UserPlant)PlantAdapter.exampleUserPlants.get(4),2,
                new User("gizgiz"), new User("fernfern"),110,createMockTimeStamp()));
        exampleCards.add(new ExchangeTransaction("b6", (UserPlant)PlantAdapter.exampleUserPlants.get(5),2,
                new User("gizgiz"), new User("fernfern"),70,createMockTimeStamp()));
    }

    public BoughtItemAdapter(Context context,List<ExchangeTransaction> boughtList) {
        this.context = context;
        this.boughtItems = boughtList;
    }

    @Override
    public int getCount() {
        return exampleCards.size();
    }

    @Override
    public Object getItem(int i) {
        return exampleCards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BoughtItemAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (BoughtItemAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_bought_item,null);
            viewHolder = new BoughtItemAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(i);
        return view;
    }

    private class ViewHolder {
        private CustomTextView id,seller,condition,date,count;
        private ImageView img;

        public ViewHolder(View view) {
            this.img = view.findViewById(R.id.bought_img);
            this.count = view.findViewById(R.id.bought_count);
            this.id = view.findViewById(R.id.bought_transaction_id);
            this.seller = view.findViewById(R.id.bought_transaction_seller);
            this.condition = view.findViewById(R.id.bought_transaction_cond);
            this.date = view.findViewById(R.id.bought_transaction_date);
        }

        public void bind(int position) {
            ExchangeTransaction card = boughtItems.get(position);
            Glide.with(context).
                    load(ResourceManager.getDrawableID(context,"ic_plant_"+card.getExchangePlant().getName()))
                    .fitCenter()
                    .into(img);
            count.setText("x".concat(String.valueOf(card.getExchangePlantCount())));
            id.setText(card.getId());
            seller.setText(card.getSeller().getUsername());
            condition.setText(String.valueOf(card.getExchangeCondition()));
            date.setText(ResourceManager.shortDateTimeFormat.format(card.getExchangeTime()));
        }
    }
}
