package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.kevinsawicki.timeago.TimeAgo;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.Entity.Notification;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.SystemDefaultPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
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
    public static List<Notification> exampleCards = new ArrayList<>();

    private static final int TYPE_TRANSACTION = 0;
    private static final int TYPE_NEGOTIATION_UPDATE = 1;
    private static final int TYPE_SENSOR_WARNING = 2;
    private static final int TYPE_FARM_WEEKLY = 3;
    private static final int TYPE_SYSTEM_PLANT = 4;
    private static final int TYPE_SYSTEM_ITEM = 5;
    private static final int TYPE_MAX_COUNT = TYPE_SYSTEM_ITEM + 1;

    private static final Map<Integer,String> TYPE = new HashMap<Integer, String>() {{
        put(TYPE_TRANSACTION,"general");
        put(TYPE_FARM_WEEKLY,"progress");
        put(TYPE_SYSTEM_PLANT,"sale");
    }};

    private static Date createMockTimeStamp() {
        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, rand.nextInt(1)+2018);
        cal.set(Calendar.MONTH, rand.nextInt(1)+2);
        cal.set(Calendar.DAY_OF_MONTH, rand.nextInt(31)+1);
        return cal.getTime();
    }

    static {
        exampleCards.add(new Notification("n1", TYPE_SYSTEM_PLANT,createMockTimeStamp(), PlantAdapter.exampleSystemPlants.get(3)));
        exampleCards.add(new Notification("n2", TYPE_FARM_WEEKLY,createMockTimeStamp()));
        exampleCards.add(new Notification("n3", TYPE_TRANSACTION,createMockTimeStamp(), StoryAdapter.exampleCards.get(2)));
        exampleCards.add(new Notification("n4", TYPE_SENSOR_WARNING,createMockTimeStamp()));
        exampleCards.add(new Notification("n5", TYPE_SYSTEM_ITEM,createMockTimeStamp(),
                new Item("m3","A & B fertilizer",200,"already formulated A and B fertilizer for hydroponics;" +
                        "mix 5cc. each with 1L water;put A in for 4-6 hours then mix B")));
        exampleCards.add(new Notification("n6", TYPE_NEGOTIATION_UPDATE,createMockTimeStamp(), StoryAdapter.exampleCards.get(2)));
        exampleCards.add(new Notification("n7", TYPE_TRANSACTION,createMockTimeStamp(), StoryAdapter.exampleCards.get(1)));
        exampleCards.add(new Notification("n8", TYPE_SYSTEM_PLANT,createMockTimeStamp(), new SystemDefaultPlant("sp8","apple")));
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
        private ImageView img;

        public ViewHolder(View view) {
            this.img = view.findViewById(R.id.notification_img);
            this.message = view.findViewById(R.id.notification_message);
            this.timestamp = view.findViewById(R.id.notification_timestamp);
        }

        public void bind(int position,int type) {
            Notification card = notificationList.get(position);
            switch (type) {
                case TYPE_TRANSACTION :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_plant_".concat(card.getStory().getMentionedPlant().getName())))
                            .fitCenter()
                            .into(img);
                    card.setMessage("You have bought ".concat(card.getStory().getMentionedPlant().getName()));
                    break;
                case TYPE_SENSOR_WARNING :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_warning"))
                            .fitCenter()
                            .into(img);
                    card.setMessage("There is problem with your sensor.\nCheck the farm!");
                    break;
                case TYPE_FARM_WEEKLY :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_warning"))
                            .fitCenter()
                            .into(img);
                    card.setMessage("Another week has passed\nDo you still want to continue?");
                    break;
                case TYPE_NEGOTIATION_UPDATE :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_plant_".concat(card.getStory().getMentionedPlant().getName())))
                            .fitCenter()
                            .into(img);
                    card.setMessage("Negotiation is updated!\nCheck the story!");
                    break;
                case TYPE_SYSTEM_PLANT :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_plant_".concat(card.getPlant().getName())))
                            .fitCenter()
                            .into(img);
                    card.setMessage("New system default plant is added!\nCheck it out!");
                    break;
                case TYPE_SYSTEM_ITEM :
                    Glide.with(context).
                            load(ResourceManager.getDrawableID(context,"ic_".concat(card.getItem().getId())))
                            .fitCenter()
                            .into(img);
                    card.setMessage("New item is added!\n Check it out!");
                    break;
                default:
            }
            message.setText(card.getMessage());
            Date cardTime = card.getTimeStamp();
            TimeAgo time = new TimeAgo();
            if(cardTime.before(Calendar.getInstance().getTime()))
                timestamp.setText(time.timeAgo(cardTime.getTime()));
            else timestamp.setText(time.timeUntil(cardTime.getTime()));
//            timestamp.append(" - "+DateFormat.getDateInstance(DateFormat.SHORT, new Locale("th","TH")).format(cardTime));
        }
    }
}
