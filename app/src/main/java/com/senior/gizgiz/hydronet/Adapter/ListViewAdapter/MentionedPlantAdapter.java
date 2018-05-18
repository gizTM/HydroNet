package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

/**
 * Created by Admins on 016 16/03/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.kevinsawicki.timeago.TimeAgo;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.HelperClass.SquareThumbnail;
import com.senior.gizgiz.hydronet.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MentionedPlantAdapter extends BaseAdapter {
    private Context context;
    private List<GrowHistory> growHistories;

    public MentionedPlantAdapter(Context context, List<GrowHistory> plantList) {
        this.context = context;
        this.growHistories = plantList;
    }

    @Override
    public int getCount() { return growHistories.size(); }

    @Override
    public Object getItem(int i) { return growHistories.get(i); }

    @Override
    public long getItemId(int i) { return 0; }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        MentionedPlantAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (MentionedPlantAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.selection_plant, null);
            viewHolder = new MentionedPlantAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }
    private class ViewHolder {
        private ImageView img;
        private CustomTextView name;
        private SquareThumbnail imgLayout;
        private CustomTextView harvestDate,startDate,time;

        public ViewHolder(View view) {
            this.img = view.findViewById(R.id.plant_thumbnail);
            this.imgLayout = view.findViewById(R.id.plant_img_layout);
            this.name = view.findViewById(R.id.plant_name);
            this.startDate = view.findViewById(R.id.start_date);
            this.harvestDate = view.findViewById(R.id.harvest_date);
            this.time = view.findViewById(R.id.time);
        }

        public void bind(int position) {
            GrowHistory card = growHistories.get(position);
            name.setText(String.valueOf(card.getCount()).concat(" ").concat(card.getPlantName()));
            Glide.with(context)
                    .load(ResourceManager.getDrawableID(context, "ic_plant_" + card.getPlantName()))
                    .fitCenter()
                    .into(img);
            startDate.setText("planted on ".concat(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(card.getStartDate())))));
            startDate.append(" (");
            startDate.append(new TimeAgo().timeAgo(Long.valueOf(card.getStartDate())) + ")");
            if (card.isHarvested()) {
                harvestDate.setText("harvested on ".concat(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(card.getHarvestDate())))));
                harvestDate.append(" (");
                harvestDate.append(new TimeAgo().timeAgo(Long.valueOf(card.getHarvestDate())) + ")");
//                long timeDiff = card.getHarvestDate().getTime() - card.getStartDate().getTime();
//                String timeDiffString = String.valueOf(Math.ceil(timeDiff / (24 * 60 * 60 * 1000)));
//                time.setText(timeDiffString.substring(0, timeDiffString.indexOf(".")).concat("d"));
                time.setText("");
            } else {
                harvestDate.setText("not harvested");
                time.setText("");
            }
        }
    }
//    hide first spinner item
//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        View v;
//        if (position == 0) {
//            CustomTextView tv = new CustomTextView(context);
//            tv.setVisibility(View.GONE);
//            tv.setHeight(0);
//            v = tv;
//            v.setVisibility(View.GONE);
//        }
//        else v = super.getDropDownView(position, null, parent);
//        return v;
//    }
}

