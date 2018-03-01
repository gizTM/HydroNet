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
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admins on 020 20/02/2018.
 */

public class StoryAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Story> stories;
    public static ArrayList<Story> exampleCards = new ArrayList<>();

    private static final int TYPE_STORY = 0;
    private static final int TYPE_PROGRESS = 1;
    private static final int TYPE_SALE = 2;
    private static final int TYPE_MAX_COUNT = TYPE_SALE + 1;

    private static final Map<Integer,String> TYPE = new HashMap<Integer, String>() {{
        put(TYPE_STORY,"general");
        put(TYPE_PROGRESS,"progress");
        put(TYPE_SALE,"sale");
    }};

    static {
        exampleCards.add(new Story(new User("gizgiz"),TYPE_STORY,"This plant is good!", PlantAdapter.exampleUserPlants.get(4)));
        exampleCards.add(new Story(new User("gizgiz"),TYPE_SALE,"x2 cucumber on sale!", PlantAdapter.exampleUserPlants.get(0)));
        exampleCards.add(new Story(new User("gizgiz"),TYPE_SALE,"x5 spinach on sale!", PlantAdapter.exampleUserPlants.get(1)));
        exampleCards.add(new Story(new User("gizgiz"),TYPE_PROGRESS,"I'm growing x4 salad!", PlantAdapter.exampleUserPlants.get(2)));
        exampleCards.add(new Story(new User("gizgiz"),TYPE_PROGRESS,"I'm growing x3 celery!", PlantAdapter.exampleUserPlants.get(3)));
    }

    public StoryAdapter(Context context, ArrayList<Story> stories) {
        this.context = context;
        this.stories = stories;
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

    @Override public int getCount() {
        return stories.size();
    }
    @Override public long getItemId(int position) {
        return position;
    }
    @Override public Object getItem(int position) {
        return exampleCards.get(position);
    }
    @Override public int getViewTypeCount() { return TYPE_MAX_COUNT; }
    @Override public int getItemViewType(int position) { return exampleCards.get(position).getType(); }

    public static int getTypeStory() { return TYPE_STORY; }
    public static int getTypeProgress() { return TYPE_PROGRESS; }
    public static int getTypeSale() { return TYPE_SALE; }
    public static int getTypeMaxCount() { return TYPE_MAX_COUNT; }

    private class ViewHolder {
        private CustomTextView owner,detail,type;
        private ImageView img,like,share;
        private LinearLayout likeBTN, shareBTN;

        public ViewHolder(View view,int type) {
            this.owner = view.findViewById(R.id.owner_username);
            this.type = view.findViewById(R.id.story_type);
            this.img = view.findViewById(R.id.feed_img);
            this.like = view.findViewById(R.id.btn_like_img);
            this.likeBTN = view.findViewById(R.id.btn_like);
            this.shareBTN = view.findViewById(R.id.btn_share);
            if(type==TYPE_STORY) this.detail = view.findViewById(R.id.story_detail_bullet);
            else if(type==TYPE_SALE) {

            } else {

            }
        }

        public void bind(int position,int type) {
            final Story card = stories.get(position);
//            img.setImageResource(ResourceManager.getDrawableID(context,"ic_"+card.getId()));
            Glide.with(context)
                    .load(ResourceManager.getDrawableID(context,"ic_plant_"+card.getMentionedPlant().getName()))
                    .fitCenter()
                    .into(img);
            if(type==TYPE_STORY) this.detail.setText(card.getDetail());
            else if(type==TYPE_SALE) {

            } else {

            }
            this.owner.setText(card.getOwner().getUsername());
            this.type.setText(TYPE.get(card.getType()));
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
