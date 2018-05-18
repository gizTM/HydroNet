package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.kevinsawicki.timeago.TimeAgo;
import com.senior.gizgiz.hydronet.Entity.Notification;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Admins on 002 02/03/2018.
 */

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private List<Notification> notificationList;

    public static final int TYPE_SENSOR_WARNING = 0;
    public static final int TYPE_STORY = 1;
    public static final int TYPE_NEGOTIATION_UPDATE = 2;
    public static final int TYPE_FARM_WEEKLY = 3;
    public static final int TYPE_SYSTEM_PLANT = 4;
    public static final int TYPE_SYSTEM_ITEM = 5;
    public static final int TYPE_MAX_COUNT = TYPE_SYSTEM_ITEM + 1;

    private static Date createMockTimeStamp() {
        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, rand.nextInt(1)+2018);
        cal.set(Calendar.MONTH, rand.nextInt(1)+5);
        cal.set(Calendar.DAY_OF_MONTH, rand.nextInt(31)+1);
        return cal.getTime();
    }

    public NotificationAdapter(Context context,List<Notification> notifications) {
        this.context = context;
        this.notificationList = notifications;
    }

    @Override public int getCount() {
        return notificationList.size();
    }
    @Override public Object getItem(int i) {
        return notificationList.get(i);
    }
    @Override public long getItemId(int i) {
        return 0;
    }
    @Override public int getViewTypeCount() { return TYPE_MAX_COUNT; }
    @Override public int getItemViewType(int i) { return notificationList.get(i).getType(); }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NotificationAdapter.ViewHolder viewHolder;
        int type = getItemViewType(i);
        if(view != null) {
            viewHolder = (NotificationAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_notification,null);
            viewHolder = new NotificationAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(i,type);
        return view;
    }

    private class ViewHolder {
        private CustomTextView message,timestamp;
        private ImageView img,read;

        public ViewHolder(View view) {
            this.img = view.findViewById(R.id.notification_img);
            this.message = view.findViewById(R.id.notification_message);
            this.timestamp = view.findViewById(R.id.notification_timestamp);
            this.read = view.findViewById(R.id.read);
        }

        public void bind(int position,int type) {
            Notification card = notificationList.get(position);
            read.setVisibility(card.isRead()?View.INVISIBLE:View.VISIBLE);
            switch (type) {
                case TYPE_STORY:
                    if(card.getStoryType()==0) {
                        Story story = card.getStory();
                        Glide.with(context).load(ResourceManager.getDrawableID(context, "ic_plant_".concat(
                                (story.getMentionedPlantType() == 0) ? story.getMentionedPlant().getName() : story.getMentionedUserPlant().getName())))
                                .fitCenter().into(img);
                    } else if(card.getStoryType()==1) {
                        ProgressStory story = card.getProgressStory();
                        Glide.with(context)
                                .load(ResourceManager.getDrawableID(context,"ic_plant_".concat(story.getMentionedUserPlant().getName())))
                                .fitCenter()
                                .into(img);
                    } else {
                        ProductAnnouncementStory story = card.getSaleStory();
                        Glide.with(context)
                                .load(ResourceManager.getDrawableID(context,"ic_plant_".concat(story.getMentionedUserPlant().getName())))
                                .fitCenter()
                                .into(img);
                    }
                    break;
                case TYPE_SENSOR_WARNING :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_warning"))
                            .fitCenter()
                            .into(img);
//                    card.setMessage("There is problem with your sensor.\nCheck the farm!");
                    break;
                case TYPE_FARM_WEEKLY :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_warning"))
                            .fitCenter()
                            .into(img);
//                    card.setMessage("Another week has passed\nDo you still want to continue?");
                    break;
                case TYPE_NEGOTIATION_UPDATE :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_plant_".concat(card.getStory().getMentionedPlant().getName())))
                            .fitCenter()
                            .into(img);
//                    card.setMessage("Negotiation is updated!\nCheck the story!");
                    break;
                case TYPE_SYSTEM_PLANT :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_plant_".concat(card.getPlant().getName())))
                            .fitCenter()
                            .into(img);
//                    card.setMessage("New system default plant is added!\nCheck it out!");
                    break;
                case TYPE_SYSTEM_ITEM :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_".concat(card.getItem().getId())))
                            .fitCenter()
                            .into(img);
//                    card.setMessage("New item is added!\n Check it out!");
                    break;
                default:
            }
            message.setText(card.getMessage());
            String cardTime = card.getTimeStamp();
            timestamp.setText(new TimeAgo().timeAgo(Long.valueOf(cardTime)));
        }
    }
}
