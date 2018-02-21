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

public class MaterialAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Item> partOverviewCards;
    public static ArrayList<Item> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new Item("m1","Pot for hydroponics gardening",50,
                "small pot for hydroponics planting;size may differ"));
        exampleCards.add(new Item("m2","Sponge for hydroponics",30,
                "50x1\" 1.5\" premium cubic sponge seed grow holder;size should be compatible with pot"));
    }

    public MaterialAdapter(Context context, ArrayList<Item> partOverviewCards) {
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
        private SpannableStringBuilder bulletBuilder;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.material_name);
            this.detail = view.findViewById(R.id.material_detail);
            if(img != null) ((BitmapDrawable) img.getDrawable()).getBitmap().recycle();
            this.img = view.findViewById(R.id.material_img);
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
