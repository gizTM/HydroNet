package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;

/**
 * Created by Admins on 020 20/02/2018.
 */

public class StoryAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Story> stories;
    public static ArrayList<Story> exampleCards = new ArrayList<>();

    static {
        exampleCards.add(new Story(new User("gizgiz"),"sale","Arduino board", PlantAdapter.exampleUserPlants.get(0)));
//        exampleCards.add(new Story("e2","Raspberry pi", 2000,
//                "basic model: Pi3;used to connect to internet"));
//        exampleCards.add(new Story("e3","ESP8266-01 module",100,
//                "basic model: model01;used to connect to internet (alternatives to rpi)"));
//        exampleCards.add(new Story("e4","Ultra sonic sensor",1000,
//                "model: ...;used to measure farm water level;"));
//        exampleCards.add(new Story("e5","pH sensor",2000,
//                "model: ...;used to measure farm pH level"));
//        exampleCards.add(new Story("e6","EC sensor",3000,
//                "model: ...;used to measure farm EC level"));
    }

    public StoryAdapter(Context context, ArrayList<Story> stories) {
        this.context = context;
        this.stories = stories;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        StoryAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (StoryAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_story,null);
            viewHolder = new StoryAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }

    @Override
    public int getCount() {
        return stories.size();
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
        private CustomTextView owner,detail,type;
        private ImageView img;
        private ImageButton like,share;

        public ViewHolder(View view) {
            this.owner = view.findViewById(R.id.owner_username);
            this.type = view.findViewById(R.id.story_type);
//            this.detail = view.findViewById(R.id.equipment_detail);
            this.img = view.findViewById(R.id.feed_img);
            like = view.findViewById(R.id.btn_like);
            share = view.findViewById(R.id.btn_share);
        }

        public void bind(int position) {
            final Story card = stories.get(position);
//            img.setImageResource(ResourceManager.getDrawableID(context,"ic_"+card.getId()));
            owner.setText(card.getOwner().getUsername());
            type.setText(card.getType());
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    card.addLikedUser(owner);
                    card.setLiked(!card.getLiked());
                    like.setImageResource(ResourceManager.getDrawableID(context,
                            (card.getLiked()?"ic_liked":"ic_like")));
//                    Log.i("liked",card.getLiked()+"");
                }
            });
        }
    }
}
