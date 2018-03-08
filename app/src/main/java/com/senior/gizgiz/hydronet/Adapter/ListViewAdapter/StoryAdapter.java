package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Admins on 020 20/02/2018.
 */

public class StoryAdapter extends BaseAdapter {
    private final Context context;
    public static ArrayList<Story> stories = new ArrayList<>();

    private static final int TYPE_STORY = 0;
    private static final int TYPE_PROGRESS = 1;
    private static final int TYPE_SALE = 2;
    private static final int TYPE_MAX_COUNT = TYPE_SALE + 1;

    private static final Map<Integer,String> TYPE = new HashMap<Integer, String>() {{
        put(TYPE_STORY,"general");
        put(TYPE_PROGRESS,"progress");
        put(TYPE_SALE,"sale");
    }};

    public static int createMockHistoryNumber(Plant plant) {
        UserPlant userPlant = (UserPlant) plant;
        Random rand = new Random();
        return rand.nextInt(userPlant.getGrowHistory().size());
    }

    public StoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        StoryAdapter.ViewHolder viewHolder;
        int type = getItemViewType(position);
        if(view != null) {
            viewHolder = (StoryAdapter.ViewHolder) view.getTag();
        } else {
            switch (type) {
                case TYPE_STORY:
                    view = LayoutInflater.from(context).inflate(R.layout.card_story,null);
                    viewHolder = new StoryAdapter.ViewHolder(view,TYPE_STORY);
                    break;
                case TYPE_PROGRESS:
                    view = LayoutInflater.from(context).inflate(R.layout.card_story_progress,null);
                    viewHolder = new StoryAdapter.ViewHolder(view,TYPE_PROGRESS);
                    break;
                case TYPE_SALE:
                    view = LayoutInflater.from(context).inflate(R.layout.card_story_announcement,null);
                    viewHolder = new StoryAdapter.ViewHolder(view,TYPE_SALE);
                    break;
                default:
                    view = LayoutInflater.from(context).inflate(R.layout.card_story,null);
                    viewHolder = new StoryAdapter.ViewHolder(view,TYPE_STORY);
                    break;
            }
            view.setTag(viewHolder);
        }
        viewHolder.bind(position,type);
        return view;
    }

    @Override public int getCount() { return stories.size(); }
    @Override public long getItemId(int position) {
        return position;
    }
    @Override public Object getItem(int position) { return stories.get(position); }
    @Override public int getViewTypeCount() { return TYPE_MAX_COUNT; }
    @Override public int getItemViewType(int position) { return stories.get(position).getType(); }

    public static int getTypeStory() { return TYPE_STORY; }
    public static int getTypeProgress() { return TYPE_PROGRESS; }
    public static int getTypeSale() { return TYPE_SALE; }
    public static int getTypeMaxCount() { return TYPE_MAX_COUNT; }

    private class ViewHolder {
        private CustomTextView owner,detail,startDate,harvestDate,status,condition,remark,count;
        private ImageView img,like,share,statBTN;
        private LinearLayout likeBTN, shareBTN;

        public ViewHolder(View view,int type) {
            this.owner = view.findViewById(R.id.owner_username);
            this.img = view.findViewById(R.id.feed_img);
            this.like = view.findViewById(R.id.btn_like_img);
            this.likeBTN = view.findViewById(R.id.btn_like);
            this.shareBTN = view.findViewById(R.id.btn_share);
            if(type==TYPE_STORY) this.detail = view.findViewById(R.id.story_detail_bullet);
            else if(type==TYPE_SALE) {
                this.count = view.findViewById(R.id.sale_plant_count);
                this.startDate = view.findViewById(R.id.sale_start_date);
                this.harvestDate = view.findViewById(R.id.sale_harvest_date);
                this.status = view.findViewById(R.id.sale_status);
                this.condition = view.findViewById(R.id.sale_init_condition);
                this.statBTN = view.findViewById(R.id.btn_show_grow_stat);
                this.remark = view.findViewById(R.id.sale_remark);
            } else {
                this.count = view.findViewById(R.id.progress_plant_count);
                this.startDate = view.findViewById(R.id.progress_start_date);
                this.harvestDate = view.findViewById(R.id.progress_harvest_date);
                this.status = view.findViewById(R.id.progress_status);
                this.statBTN = view.findViewById(R.id.btn_show_grow_stat);
                this.remark = view.findViewById(R.id.progress_remark);
            }
        }

        public void bind(int position,int type) {
            final Story card = stories.get(position);
            Glide.with(context)
                    .load(ResourceManager.getDrawableID(context,"ic_plant_"+card.getMentionedPlant().getName()))
                    .fitCenter()
                    .into(img);
            if(type==TYPE_STORY)
                detail.setText(stories.get(position).getRemark());
            else if(type==TYPE_SALE) {
                int historyNumber = ((ProductAnnouncementStory) card).getHistoryNumber();
                count.setText("x".concat(String.valueOf(card.getMentionedPlant().getGrowHistory().get(historyNumber).getCount())));
                startDate.setText(ResourceManager.shortDateFormat.format(card.getMentionedPlant().getGrowHistory().get(historyNumber).getStartDate()));
                harvestDate.setText(ResourceManager.shortDateFormat.format((card.getMentionedPlant()).getGrowHistory().get(historyNumber).getHarvestDate()));
                status.setText(((ProductAnnouncementStory) card).getSaleStatus());
                condition.setText(((ProductAnnouncementStory) card).getCondition());
                statBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                remark.setText(card.getRemark());
            } else {
                int historyNumber = ((ProgressStory)card).getHistoryNumber();
                count.setText("x".concat(String.valueOf(card.getMentionedPlant().getGrowHistory().get(historyNumber).getCount())));
                startDate.setText(ResourceManager.shortDateFormat.format((card.getMentionedPlant()).getGrowHistory().get(historyNumber).getStartDate()));
                harvestDate.setText(ResourceManager.shortDateFormat.format((card.getMentionedPlant()).getGrowHistory().get(historyNumber).getHarvestDate()));
                status.setText((card.getMentionedPlant()).getGrowHistory().get(historyNumber).getResult());
                statBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                remark.setText(card.getRemark());
            }
            this.owner.setText(card.getOwner().getUsername());
            this.likeBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    card.setLiked(!card.getLiked());
                    like.setImageResource(ResourceManager.getDrawableID(context,
                            (card.getLiked()?"ic_liked":"ic_like")));
                }
            });
        }
    }
}
