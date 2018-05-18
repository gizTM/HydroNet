package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.github.kevinsawicki.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Activity.AddPlantActivity;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Activity.NegotiateActivity;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Negotiation;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.Fragment.FeedFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Admins on 020 20/02/2018.
 */

public class StoryAdapter extends BaseAdapter {
    private final Context context;
    private static List<Story> stories = new ArrayList<>();

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
        return rand.nextInt(userPlant.getGrowHistories().size());
    }

    public StoryAdapter(Context context, List<Story> stories) {
        this.context = context;
        StoryAdapter.stories = stories;
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
        private CustomTextView owner,detail,startDate,harvestDate,status,condition,postDate,
                remark,count,reserved,dealRemainTime,total,negotiationCount,tryCount,likeCount,reservedLabel;
        private ImageView img, likeBTN, shareBTN, negotiateBTN, tryBTN, statBTN,optionBTN;
        private ProgressBar countDownProgress;

        public ViewHolder(View view,int type) {
            this.owner = view.findViewById(R.id.owner_username);
            this.img = view.findViewById(R.id.feed_img);
            this.likeBTN = view.findViewById(R.id.btn_like);
            this.shareBTN = view.findViewById(R.id.btn_share);
            this.tryBTN = view.findViewById(R.id.btn_try);
            this.postDate = view.findViewById(R.id.post_date);
            this.likeCount = view.findViewById(R.id.like_count);
            this.optionBTN = view.findViewById(R.id.btn_option);
            if(type==TYPE_STORY) this.detail = view.findViewById(R.id.story_detail);
            else if(type==TYPE_SALE) {
                this.count = view.findViewById(R.id.sale_plant_count);
                this.startDate = view.findViewById(R.id.sale_start_date);
                this.harvestDate = view.findViewById(R.id.sale_harvest_date);
                this.condition = view.findViewById(R.id.sale_init_condition);
                this.statBTN = view.findViewById(R.id.btn_show_grow_stat);
                this.remark = view.findViewById(R.id.sale_remark);
                this.negotiateBTN = view.findViewById(R.id.btn_negotiate);
                this.reserved = view.findViewById(R.id.num_reserved);
                this.total = view.findViewById(R.id.total);
                this.dealRemainTime = view.findViewById(R.id.deal_remaining_time);
                this.countDownProgress = view.findViewById(R.id.count_down_progress);
                this.negotiationCount = view.findViewById(R.id.negotiation_count);
                this.tryCount = view.findViewById(R.id.try_count);
                this.reservedLabel = view.findViewById(R.id.user_reserve);
            } else {
                this.count = view.findViewById(R.id.progress_plant_count);
                this.startDate = view.findViewById(R.id.progress_start_date);
                this.harvestDate = view.findViewById(R.id.progress_harvest_date);
                this.status = view.findViewById(R.id.progress_status);
                this.statBTN = view.findViewById(R.id.btn_show_grow_stat);
                this.remark = view.findViewById(R.id.progress_remark);
            }
        }

        public void bind(int position, final int type) {
            final Story card = stories.get(position);
            this.owner.setText(card.getOwner().getUsername());
            this.postDate.setText(new TimeAgo().timeAgo(Long.valueOf(card.getPostDate())));
            this.tryBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                            new ContextThemeWrapper(context, R.style.myDialog));
                    View dialogCustomLayout = LayoutInflater.from(context).inflate(R.layout.confirm_dialog, null);
                    ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Want to grow this plant?");
                    dialogBuilder.setView(dialogCustomLayout);
                    final AlertDialog dialog = dialogBuilder.create();
                    dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            Intent intent = new Intent(context, AddPlantActivity.class);
                            if(card.getMentionedPlantType() == 0) {
//                                intent.putExtra("PLANTNAME",card.getMentionedPlant().getName());
                                intent.putExtra("PLANT",card.getMentionedPlant());
                                intent.putExtra("TYPE",0);
                            }
                            else {
//                                intent.putExtra("PLANTNAME",card.getMentionedUserPlant().getName());
                                intent.putExtra("PLANT",card.getMentionedUserPlant());
                                intent.putExtra("TYPE",1);
                                intent.putExtra("UID",card.getOwnerId());
                            }
                            context.startActivity(intent);
                        }
                    });
                    dialogCustomLayout.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            });
            likeBTN.setImageResource(ResourceManager.getDrawableID(context,card.getLiked()?"ic_liked":"ic_like"));
            likeBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    card.setLiked(!card.getLiked());
                    if(card.getLiked()) {
                        likeBTN.setImageResource(ResourceManager.getDrawableID(context,"ic_liked"));
                        RealTimeDBManager.likeStory(card.getId(),card.getOwnerId(),card.getType());
                    } else {
                        likeBTN.setImageResource(ResourceManager.getDrawableID(context,"ic_like"));
                        RealTimeDBManager.unlikeStory(card.getId(),card.getOwnerId(),card.getType());
                    }
                }
            });
            int likedCount = card.getLikedUsers().size();
            likeCount.setText(likedCount==0?"":String.valueOf(likedCount));
            if(type==TYPE_STORY) {
                String plantName;
                if(card.getMentionedPlantType() == 1) plantName = card.getMentionedUserPlant().getName();
                else plantName = card.getMentionedPlant().getName();
                Glide.with(context)
                        .load(ResourceManager.getDrawableID(context,"ic_plant_"+plantName))
                        .fitCenter()
                        .into(img);
                detail.setText(stories.get(position).getRemark());
                if(card.getOwnerId().equalsIgnoreCase(MainActivity.currentUser.getUid())) {
                    optionBTN.setVisibility(View.VISIBLE);
                    optionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popup = new PopupMenu(context,v);
                            popup.getMenuInflater().inflate(R.menu.menu_item_option, popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                        new ContextThemeWrapper(context,R.style.myDialog));
                                    View dialogCustomLayout = LayoutInflater.from(context).inflate(R.layout.confirm_dialog,null);
                                    ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message)).setText(
                                            "Delete this story?");
                                    dialogBuilder.setView(dialogCustomLayout);
                                    final AlertDialog dialog = dialogBuilder.create();
                                    dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                            RealTimeDBManager.deleteStory(card.getType(),card.getId(),card.getOwnerId());
                                            FeedFragment.deleteStory(card.getType(),card.getId());
                                        }
                                    });
                                    dialogCustomLayout.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                    return true;
                                }
                            });
                        }
                    });
                } else optionBTN.setVisibility(View.GONE);
            } else if(type==TYPE_SALE) {
                Glide.with(context)
                        .load(ResourceManager.getDrawableID(context,"ic_plant_"+card.getMentionedUserPlant().getName()))
                        .fitCenter()
                        .into(img);
                final GrowHistory history = card.getMentionedUserPlant().getGrowHistories().get(0);
                final Date dueDate = new Date(Long.valueOf(((ProductAnnouncementStory)card).getDueDate()));
                final Date postDate = new Date(Long.valueOf(card.getPostDate()));
                final String dueTimeString = NavigationManager.calculateTimeDiff("string",dueDate);
                negotiateBTN.setImageResource(R.drawable.ic_negotiation);
                negotiateBTN.setEnabled(true);
                if(dueTimeString.substring(0,1).equals("+")) dealRemainTime.setText("Ends in ".concat(dueTimeString.substring(1)));
                else {
                    dealRemainTime.setStyle("Bold");
                    dealRemainTime.setText("Reservation period ends");
                    negotiateBTN.setImageResource(R.drawable.ic_negotiation_disabled);
                    negotiateBTN.setEnabled(false);
                    final List<String> uids = new ArrayList<>(), counts = new ArrayList<>();
                    for(Negotiation negotiation : ((ProductAnnouncementStory) card).getNegotiations()) {
                        uids.add(negotiation.getNegotiatorId());
                        counts.add(negotiation.getCondition());
                    }
                    for (int i=0; i<((ProductAnnouncementStory) card).getNegotiations().size(); i++) {
                        String uid = uids.get(i);
                        final String count = counts.get(i);
                        RealTimeDBManager.getDatabase().child("boughtStories/"+uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    boolean existed = false; int a=0;
                                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String boughtStoryId = childSnapshot.getKey();
                                        if(card.getId().equalsIgnoreCase(boughtStoryId)) existed = true;
                                        a++;
                                        if(a==dataSnapshot.getChildrenCount()) {
                                            if(!existed) RealTimeDBManager.writeTransaction(uids, card.getId(), card.getMentionedUserPlant().getName(),card.getOwnerId(),card.getOwner().getUsername(),count);
                                        }
                                    }
                                }
                                else RealTimeDBManager.writeTransaction(uids, card.getId(), card.getMentionedUserPlant().getName(), card.getOwnerId(),card.getOwner().getUsername(),count);
                            }
                            @Override public void onCancelled(DatabaseError databaseError) { }
                        });
                    }
                }
                final long totalTemp = 86400000L;
                final long totalTime = dueDate.getTime()-new Date(Long.valueOf(card.getPostDate())).getTime();
                final long passTime = Long.valueOf(NavigationManager.calculateTimeDiff("long",postDate).substring(1));
                final long remainingTime = Long.valueOf(NavigationManager.calculateTimeDiff("long",dueDate));
                final long percentage;
                if(remainingTime>totalTemp) percentage = 0;
                else percentage = 100-remainingTime*100/totalTemp;
                owner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("dueTime",String.valueOf(dueTimeString));
                        Log.e("passss",String.valueOf(passTime));
                        Log.e("remain",String.valueOf(remainingTime));
                        Log.e("total",String.valueOf(totalTime));
                        Log.e("progress",String.valueOf(percentage));
                    }
                });
                final String uid = MainActivity.currentUser.getUid();
                countDownProgress.setProgress((int)percentage);
                negotiateBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, NegotiateActivity.class);
                        intent.putExtra("STORY_ID",card.getId());
                        intent.putExtra("OWNER_ID",card.getOwnerId());
                        intent.putExtra("COUNT",history.getCount());
                        intent.putExtra("UPDATE_TYPE","add");
                        intent.putExtra("UPDATE_COUNT",((ProductAnnouncementStory) card).getReserved());
                        intent.putExtra("RESERVE",((ProductAnnouncementStory) card).getReserved());
                        intent.putExtra("COND_TYPE",((ProductAnnouncementStory) card).getExchangeConditionType());
                        intent.putExtra("CONDITION",((ProductAnnouncementStory) card).getCondition().substring(1));
                        Log.e("storyAdapter","startActivityForResult");
                        ((Activity)context).startActivityForResult(intent,NegotiateActivity.ADD_NEGOTIATION);
                    }
                });
                total.setText(" / ".concat(String.valueOf(history.getCount())));
                reserved.setText(String.valueOf(((ProductAnnouncementStory) card).getReserved()).concat(" Reserved"));
                if(((ProductAnnouncementStory)card).getReserved()==history.getCount()) {
                    total.setText("");
                    reserved.setText(R.string.all_reserved);
                    negotiateBTN.setImageResource(R.drawable.ic_negotiation_disabled);
                    negotiateBTN.setEnabled(false);
                }
                reservedLabel.setVisibility(View.GONE);
                reservedLabel.setText("");
                if(card.getOwnerId().equalsIgnoreCase(uid)) {
                    negotiateBTN.setImageResource(R.drawable.ic_negotiation_disabled);
                    negotiateBTN.setEnabled(false);
                }
                for(final Negotiation negotiation : ((ProductAnnouncementStory) card).getNegotiations()) {
                    if(negotiation.getNegotiatorId().equalsIgnoreCase(uid)) {
                        negotiateBTN.setImageResource(R.drawable.ic_negotiated);
                        negotiateBTN.setEnabled(true);
                        reservedLabel.setVisibility(View.VISIBLE);
                        reservedLabel.setText(ResourceManager.getString(context,"label_reserved")
                                .concat(" ").concat(negotiation.getCondition()).concat(" units"));
                        negotiateBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, NegotiateActivity.class);
                                intent.putExtra("STORY_ID",card.getId());
                                intent.putExtra("OWNER_ID",card.getOwnerId());
                                intent.putExtra("COUNT",history.getCount());
                                intent.putExtra("UPDATE_TYPE","update");
                                intent.putExtra("UPDATE_COUNT",Integer.valueOf(negotiation.getCondition()));
                                intent.putExtra("RESERVE",((ProductAnnouncementStory) card).getReserved());
                                intent.putExtra("COND_TYPE",((ProductAnnouncementStory) card).getExchangeConditionType());
                                intent.putExtra("CONDITION",((ProductAnnouncementStory) card).getCondition().substring(1));
                                Log.e("storyAdapter","startActivityForResult");
                                ((Activity)context).startActivityForResult(intent,NegotiateActivity.UPDATE_NEGOTIATION);
                            }
                        });
                        break;
                    }
                }
                count.setText("x".concat(String.valueOf(history.getCount())));
                if(((ProductAnnouncementStory) card).getNegotiations()!=null) {
                    int negoCount = ((ProductAnnouncementStory) card).getNegotiations().size();
                    negotiationCount.setText((negoCount == 0) ? "" : String.valueOf(negoCount));
                }
                startDate.setText(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(history.getStartDate()))));
                if(!history.isHarvested()) harvestDate.setText("not yet");
                else harvestDate.setText(ResourceManager.shortDateFormat.format(new Date(Long.valueOf((history.getHarvestDate())))));
                condition.setText(((ProductAnnouncementStory) card).getCondition());
                statBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View customView = inflater.inflate(R.layout.popup_sensor_data,null);
                        final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        popup.setOutsideTouchable(true);
                        popup.showAtLocation(customView, Gravity.CENTER, 0, 0);
                        List<PointValue> waterValues = new ArrayList<>(), pHValues = new ArrayList<>(), ecValues = new ArrayList<>();
                        for(int i=0; i<12; i++) {
                            waterValues.add(new PointValue(i, new Random().nextFloat()+9));
                            pHValues.add(new PointValue(i, new Random().nextFloat()+4));
                            ecValues.add(new PointValue(i, new Random().nextFloat()+1));
                        }
                        Line waterLine = new Line(waterValues).setColor(Color.BLUE).setCubic(false);
                        Line pHLine = new Line(pHValues).setColor(Color.GREEN).setCubic(false);
                        Line ecLine = new Line(ecValues).setColor(Color.RED).setCubic(false);
                        List<Line> waterLines = new ArrayList<>();
                        List<Line> pHLines = new ArrayList<>();
                        List<Line> ecLines = new ArrayList<>();
                        waterLines.add(waterLine);
                        pHLines.add(pHLine);
                        ecLines.add(ecLine);
                        LineChartData waterData = new LineChartData();
                        LineChartData pHData = new LineChartData();
                        LineChartData ecData = new LineChartData();
                        waterData.setLines(waterLines);
                        pHData.setLines(pHLines);
                        ecData.setLines(ecLines);
                        ((LineChartView) customView.findViewById(R.id.story_stat_water)).setLineChartData(waterData);
                        ((LineChartView) customView.findViewById(R.id.story_stat_ph)).setLineChartData(pHData);
                        ((LineChartView) customView.findViewById(R.id.story_stat_ec)).setLineChartData(ecData);
                        NavigationManager.dimBehind(popup);
                    }
                });
                remark.setText(card.getRemark());
                if(card.getOwnerId().equalsIgnoreCase(MainActivity.currentUser.getUid())) {
                    optionBTN.setVisibility(View.VISIBLE);
                    optionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popup = new PopupMenu(context,v);
                            popup.getMenuInflater().inflate(R.menu.menu_item_option, popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                            new ContextThemeWrapper(context,R.style.myDialog));
                                    View dialogCustomLayout = LayoutInflater.from(context).inflate(R.layout.confirm_dialog,null);
                                    if(((ProductAnnouncementStory) card).getNegotiations().size()!=0) {
                                        ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText(
                                                "Cannot delete. You get reservation.");
                                        dialogBuilder.setView(dialogCustomLayout);
                                        final AlertDialog dialog = dialogBuilder.create();
                                        dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                            }
                                        });
                                        dialogCustomLayout.findViewById(R.id.btn_negative).setVisibility(View.GONE);
                                        dialog.show();
                                    } else {
                                        ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText(
                                                "Delete this story?");
                                        dialogBuilder.setView(dialogCustomLayout);
                                        final AlertDialog dialog = dialogBuilder.create();
                                        dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                RealTimeDBManager.deleteStory(card.getType(), card.getId(),card.getOwnerId());
                                                FeedFragment.deleteStory(card.getType(),card.getId());
                                            }
                                        });
                                        dialogCustomLayout.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                            }
                                        });
                                        dialog.show();
                                    }
                                    return true;
                                }
                            });
                        }
                    });
                } else optionBTN.setVisibility(View.GONE);
            } else {
                Glide.with(context)
                        .load(ResourceManager.getDrawableID(context,"ic_plant_"+card.getMentionedUserPlant().getName()))
                        .fitCenter()
                        .into(img);
                int historyNumber = ((ProgressStory)card).getHistoryNumber();
                count.setText("x".concat(String.valueOf(card.getMentionedUserPlant().getGrowHistories().get(historyNumber).getCount())));
                startDate.setText(ResourceManager.shortDateFormat.format(new Date(Long.valueOf((card.getMentionedUserPlant()).getGrowHistories().get(historyNumber).getStartDate()))));
                harvestDate.setText(ResourceManager.shortDateFormat.format(new Date(Long.valueOf((card.getMentionedUserPlant()).getGrowHistories().get(historyNumber).getHarvestDate()))));
                status.setText((card.getMentionedUserPlant()).getGrowHistories().get(historyNumber).getResult());
                statBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                remark.setText(card.getRemark());
                if(card.getOwnerId().equalsIgnoreCase(MainActivity.currentUser.getUid())) {
                    optionBTN.setVisibility(View.VISIBLE);
                    optionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popup = new PopupMenu(context,v);
                            popup.getMenuInflater().inflate(R.menu.menu_item_option, popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                            new ContextThemeWrapper(context,R.style.myDialog));
                                    View dialogCustomLayout = LayoutInflater.from(context).inflate(R.layout.confirm_dialog,null);
                                    ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message)).setText(
                                            "Delete this story?");
                                    dialogBuilder.setView(dialogCustomLayout);
                                    final AlertDialog dialog = dialogBuilder.create();
                                    dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                            RealTimeDBManager.deleteStory(card.getType(),card.getId(),card.getOwnerId());
                                            FeedFragment.deleteStory(card.getType(),card.getId());
                                        }
                                    });
                                    dialogCustomLayout.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                    return true;
                                }
                            });
                        }
                    });
                } else optionBTN.setVisibility(View.GONE);
            }
        }
    }
}
