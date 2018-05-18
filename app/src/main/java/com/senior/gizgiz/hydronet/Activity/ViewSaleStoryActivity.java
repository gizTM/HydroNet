package com.senior.gizgiz.hydronet.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.NegotiationAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Negotiation;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.Fragment.FeedFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.HelperClass.ValueStepper;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ViewSaleStoryActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private CustomTextView owner,postDate,detail,likeCount,count,startDate,harvestDate,condition,
                    remark,reserved,total,dealRemainTime,tryCount,reserveBTN,reserveLabel;
    private ImageView img,tryBTN,likeBTN,statBTN, cancelReserveBTN,optionBTN;
    private ProgressBar countDownProgress;
    private ListView negotiationList;
    private ValueStepper reserveStepper;
    private RelativeLayout reserveLayout,stepperLayout;

    private Story card;
    private GrowHistory history;
    final private String uid = MainActivity.currentUser.getUid();
    private NegotiationAdapter negotiationAdapter;
    private List<Negotiation> negotiations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sale_story);
        setup();
        this.negotiationList = findViewById(R.id.negotiation_list);
        this.owner = findViewById(R.id.owner_username);
        this.img = findViewById(R.id.feed_img);
        this.likeBTN = findViewById(R.id.btn_like);
        this.tryBTN = findViewById(R.id.btn_try);
        this.postDate = findViewById(R.id.post_date);
        this.likeCount = findViewById(R.id.like_count);
        this.detail = findViewById(R.id.story_detail);
        this.count = findViewById(R.id.sale_plant_count);
        this.startDate = findViewById(R.id.sale_start_date);
        this.harvestDate = findViewById(R.id.sale_harvest_date);
        this.condition = findViewById(R.id.sale_init_condition);
        this.statBTN = findViewById(R.id.btn_show_grow_stat);
        this.remark = findViewById(R.id.sale_remark);
        this.reserved = findViewById(R.id.num_reserved);
        this.total = findViewById(R.id.total);
        this.dealRemainTime = findViewById(R.id.deal_remaining_time);
        this.countDownProgress = findViewById(R.id.count_down_progress);
        this.tryCount = findViewById(R.id.try_count);
        this.reserveStepper = findViewById(R.id.user_reserve);
        this.stepperLayout = findViewById(R.id.stepper_layout);
        this.reserveLayout = findViewById(R.id.reserve_layout);
        this.reserveBTN = findViewById(R.id.btn_make_reservation);
        this.reserveLabel = findViewById(R.id.user_reserve_label);
        this.cancelReserveBTN = findViewById(R.id.btn_cancel_reserve);
        this.optionBTN = findViewById(R.id.btn_option);
        card = getIntent().getExtras().getParcelable("STORY");
        history = card.getMentionedUserPlant().getGrowHistories().get(0);
        handleSaleStory();
        handleDeleteStory();
        handleBTNLayout();
//        handleDataListener();
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_sub_story);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void handleSaleStory() {
        reserveLabel.setVisibility(View.GONE);
        this.owner.setText(card.getOwner().getUsername());
        Glide.with(getBaseContext())
                .load(ResourceManager.getDrawableID(getBaseContext(),"ic_plant_"+card.getMentionedUserPlant().getName()))
                .fitCenter()
                .into(img);
        final Date dueDate = new Date(Long.valueOf(((ProductAnnouncementStory)card).getDueDate()));
        final Date postDate = new Date(Long.valueOf(card.getPostDate()));
        final String dueTimeString = NavigationManager.calculateTimeDiff("string",dueDate);
        if(dueTimeString.substring(0,1).equals("+")) dealRemainTime.setText("Ends in ".concat(dueTimeString.substring(1)));
        else {
            dealRemainTime.setStyle("Bold");
            dealRemainTime.setText("Reservation period ends");
        }
        final long totalTemp = 24*60*60*1000;
        final long totalTime = dueDate.getTime()-postDate.getTime();
        final long passTime = Long.valueOf(NavigationManager.calculateTimeDiff("long",postDate).substring(1));
        final long remainingTime = Long.valueOf(NavigationManager.calculateTimeDiff("long",dueDate));
        final long percentage;
        if(remainingTime>totalTemp) percentage = 0;
        else percentage = 100-remainingTime*100/totalTemp;
//        owner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("dueTime",String.valueOf(dueTimeString));
//                Log.e("passss",String.valueOf(passTime));
//                Log.e("remain",String.valueOf(remainingTime));
//                Log.e("total",String.valueOf(totalTime));
//                Log.e("progress",String.valueOf(percentage));
//            }
//        });
        countDownProgress.setProgress((int)percentage);
        total.setText(" / ".concat(String.valueOf(history.getCount())));
        reserved.setText(String.valueOf(((ProductAnnouncementStory) card).getReserved()).concat(" Reserved"));
        if(((ProductAnnouncementStory)card).getReserved()==history.getCount()) {
            total.setText("");
            reserved.setText(R.string.all_reserved);
        }
        negotiationAdapter = new NegotiationAdapter(getBaseContext(),negotiations);
        negotiationList.setAdapter(negotiationAdapter);
        if(card.getOwnerId().equalsIgnoreCase(uid)) {
            reserveLayout.setVisibility(View.GONE);
            handleCheckNegotiation();
            negotiationAdapter.notifyDataSetChanged();
            NavigationManager.justifyListViewHeightBasedOnChildren(negotiationList);
        } else handleReserve();
        count.setText("x".concat(String.valueOf(history.getCount())));
        startDate.setText(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(history.getStartDate()))));
        if(!history.isHarvested()) harvestDate.setText("not yet");
        else harvestDate.setText(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(history.getHarvestDate()))));
        condition.setText(((ProductAnnouncementStory) card).getCondition());
        statBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    }
    private int handleCheckNegotiation() {
        int myReserved = 0;
        int allReserved = 0;
        negotiations.clear();
        for(final Negotiation negotiation : ((ProductAnnouncementStory) card).getNegotiations()) {
            negotiations.add(negotiation);
            allReserved++;
            if(negotiation.getNegotiatorId().equalsIgnoreCase(uid)) {
                NavigationManager.collapse(stepperLayout);
                reserveBTN.setText("Update reservation");
                reserveLabel.setVisibility(View.VISIBLE);
                reserveLabel.setText(ResourceManager.getString(getBaseContext(),"label_reserved")
                        .concat(" ").concat(String.valueOf(negotiation.getCondition())).concat(" units"));
                myReserved = Integer.valueOf(negotiation.getCondition());
//                NavigationManager.collapse(reserveLayout);
                final int finalMyReserved = myReserved;
                final int finalAllReserved = allReserved;
                reserveBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleExpandExistingReserve(negotiation, finalMyReserved, finalAllReserved);
                    }
                });
                break;
            }
        }
        Log.e("negotiation count",((ProductAnnouncementStory) card).getNegotiations().size()+"");
        return myReserved;
    }
    private void handleReserve() {
        stepperLayout.setVisibility(View.GONE);
        int available = history.getCount() - ((ProductAnnouncementStory) card).getReserved();
        reserveStepper.setMaxValue(available);
        reserveStepper.setMinValue(0);
        int myReserved = handleCheckNegotiation();
        if(myReserved==0) {
            final int finalMyReserved = myReserved;
            reserveBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleExpandNewReserve(finalMyReserved);
                }
            });
        }
    }
    private void handleDeleteStory() {
        if(card.getOwnerId().equalsIgnoreCase(MainActivity.currentUser.getUid())) {
            optionBTN.setVisibility(View.VISIBLE);
            optionBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getBaseContext(),v);
                    popup.getMenuInflater().inflate(R.menu.menu_item_option, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(((ProductAnnouncementStory) card).getNegotiations().size()!=0) {
                                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                        new ContextThemeWrapper(ViewSaleStoryActivity.this,R.style.myDialog));
                                View dialogCustomLayout = LayoutInflater.from(ViewSaleStoryActivity.this).inflate(R.layout.confirm_dialog,null);
                                ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message)).setText(
                                        "Cannot delete. You get reservation");
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
                                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                        new ContextThemeWrapper(ViewSaleStoryActivity.this,R.style.myDialog));
                                View dialogCustomLayout = LayoutInflater.from(ViewSaleStoryActivity.this).inflate(R.layout.confirm_dialog,null);
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
                                        finish();
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
    }
    private void handleExpandExistingReserve(final Negotiation negotiation, final int reserve, final int allReserved) {
        NavigationManager.expand(stepperLayout);
        cancelReserveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.collapse(stepperLayout);
                reserveBTN.setText("Update reservation");
            }
        });
        reserveBTN.setText("Confirm reservation");
        reserveStepper.setValue(reserve);
//        reserveBTN.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, reserveBTN.getHeight()));
        reserveStepper.setOnValueChangedListener(new ValueStepper.OnValueChangeListener() {
            @Override
            public void onValueChanged(final int value) {
//                if(value != reserve) {
                    reserveStepper.setActiveBackground();
                    reserveBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reserved.setText(String.valueOf(allReserved).concat(" Reserved"));
                            reserveLabel.setVisibility(View.VISIBLE);
                            reserveLabel.setText(ResourceManager.getString(getBaseContext(),"label_reserved")
                                    .concat(" ").concat(String.valueOf(value)).concat(" units"));
                            String timestamp = String.valueOf(new Date().getTime());
                            RealTimeDBManager.writeExistingNegotiation(card.getId(),negotiation.getId(),
                                    String.valueOf(value),allReserved,timestamp);
                            negotiation.setCondition(String.valueOf(value));
                            negotiation.setTimestamp(timestamp);
                            handleCheckNegotiation();
                            negotiationAdapter.notifyDataSetChanged();
                            NavigationManager.justifyListViewHeightBasedOnChildren(negotiationList);
                        }
                    });
//                } else reserveStepper.resetBackground();
            }
        });
    }
    private void handleExpandNewReserve(final int reserve) {
        NavigationManager.expand(stepperLayout);
        cancelReserveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.collapse(stepperLayout);
                reserveBTN.setText("Make reservation");
            }
        });
        reserveBTN.setText("Confirm reservation");
        reserveStepper.setValue(reserve);
//                reserveBTN.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, reserveBTN.getHeight()));
        reserveStepper.setOnValueChangedListener(new ValueStepper.OnValueChangeListener() {
            @Override
            public void onValueChanged(final int value) {
                if(value != reserve) {
                    reserveStepper.setActiveBackground();
                    reserveBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reserved.setText(String.valueOf(((ProductAnnouncementStory) card).getReserved()+value)
                                    .concat(" Reserved"));
                            reserveLabel.setVisibility(View.VISIBLE);
                            reserveLabel.setText(ResourceManager.getString(getBaseContext(),"label_reserved")
                                    .concat(" ").concat(String.valueOf(value)).concat(" units"));
                            String timestamp = String.valueOf(new Date().getTime());
                            String negotiationId = RealTimeDBManager.writeNewNegotiation(card.getId(),card.getOwnerId(),MainActivity.username,
                                    uid,String.valueOf(value),
                                    String.valueOf(((ProductAnnouncementStory) card).getReserved()+value),timestamp);
                            ((ProductAnnouncementStory) card).addNegotiations(
                                    new Negotiation(negotiationId, card.getId(),uid,String.valueOf(value),
                                            "reserved",MainActivity.username,timestamp));
                            handleCheckNegotiation();
                            negotiationAdapter.notifyDataSetChanged();
                            NavigationManager.justifyListViewHeightBasedOnChildren(negotiationList);
                        }
                    });
                } else reserveStepper.resetBackground();
            }
        });
    }
    private void handleDataListener() {
        RealTimeDBManager.getDatabase().child("saleStories").orderByKey().equalTo(card.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final String uid = MainActivity.currentUser.getUid();
                    final ProductAnnouncementStory story = childSnapshot.getValue(ProductAnnouncementStory.class);
                    final int historyNumber = story.getHistoryNumber();
                    final String storyId = childSnapshot.getKey();
                    final boolean[] likeChanged = {false};
                    story.setId(storyId);
                    story.setReserved(0);
                    if(story.getLikedUsers().get(uid) != null) story.setLiked(true);
                    RealTimeDBManager.getDatabase().child("growHistories/"+story.getMentionedPlantId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                int index = 0;
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    if(index==historyNumber) {
                                        GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                        List<GrowHistory> growHistories = new ArrayList<>();
                                        growHistories.add(growHistory);
                                        story.setMentionedUserPlant(new UserPlant(growHistory.getPlantName(),growHistories));
                                        RealTimeDBManager.getDatabase().child("negotiations").orderByChild("storyId").equalTo(storyId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    int i = 0;
                                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                        final Negotiation negotiation = childSnapshot.getValue(Negotiation.class);
                                                        story.addNegotiations(negotiation);
                                                        story.addReserved(Integer.valueOf(negotiation.getCondition()));
                                                        i++;
                                                        if (!likeChanged[0] && i == dataSnapshot.getChildrenCount()) {
                                                            card = story;
                                                            reserved.setText(String.valueOf(story.getReserved()).concat(" Reserved"));
                                                            for(Negotiation nego : story.getNegotiations()) {
                                                                if(nego.getNegotiatorId().equalsIgnoreCase(uid)) {
                                                                    handleCheckNegotiation();
                                                                    negotiationAdapter.notifyDataSetChanged();
                                                                    NavigationManager.justifyListViewHeightBasedOnChildren(negotiationList);
                                                                    reserveLabel.setText(ResourceManager.getString(getBaseContext(), "label_reserved")
                                                                            .concat(" ").concat(String.valueOf(negotiation.getCondition())).concat(" units"));
                                                                } else {
                                                                    handleCheckNegotiation();
                                                                    negotiationAdapter.notifyDataSetChanged();
                                                                    NavigationManager.justifyListViewHeightBasedOnChildren(negotiationList);
                                                                    reserveLabel.setVisibility(View.GONE);
//                                                                    reserveLabel.setText(ResourceManager.getString(getBaseContext(), "label_reserved")
//                                                                            .concat(" ").concat(String.valueOf(negotiation.getCondition())).concat(" units"));
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if(!likeChanged[0]) {
                                                    card = story;
                                                }
                                            }
                                            @Override public void onCancelled(DatabaseError databaseError) {}
                                        });
                                    }
                                    index++;
                                }
                            } else Log.e("no dataa",">>>");
                        }
                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void handleBTNLayout() {
        this.tryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        new ContextThemeWrapper(ViewSaleStoryActivity.this, R.style.myDialog));
                View dialogCustomLayout = LayoutInflater.from(ViewSaleStoryActivity.this).inflate(R.layout.confirm_dialog, null);
                ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Want to grow this plant?");
                dialogBuilder.setView(dialogCustomLayout);
                final AlertDialog dialog = dialogBuilder.create();
                dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        Intent intent = new Intent(ViewSaleStoryActivity.this, AddPlantActivity.class);
                        if(card.getMentionedPlantType() == 0) {
//                            intent.putExtra("PLANTNAME",card.getMentionedPlant().getName());
                            intent.putExtra("PLANT",card.getMentionedPlant());
                            intent.putExtra("TYPE",0);
                        }
                        else {
//                            intent.putExtra("PLANTNAME",card.getMentionedUserPlant().getName());
                            intent.putExtra("PLANT",card.getMentionedUserPlant());
                            intent.putExtra("TYPE",1);
                            intent.putExtra("UID",card.getOwnerId());
                        }
                        startActivity(intent);
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
        likeBTN.setImageResource(ResourceManager.getDrawableID(ViewSaleStoryActivity.this,card.getLiked()?"ic_liked":"ic_like"));
        likeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.setLiked(!card.getLiked());
                if(card.getLiked()) {
                    likeBTN.setImageResource(ResourceManager.getDrawableID(ViewSaleStoryActivity.this,"ic_liked"));
                    RealTimeDBManager.likeStory(card.getId(),card.getOwnerId(),card.getType());
                } else {
                    likeBTN.setImageResource(ResourceManager.getDrawableID(ViewSaleStoryActivity.this,"ic_like"));
                    RealTimeDBManager.unlikeStory(card.getId(),card.getOwnerId(),card.getType());
                }
            }
        });
        int likedCount = card.getLikedUsers().size();
        likeCount.setText(likedCount==0?"":String.valueOf(likedCount));
    }

    @Override
    public void onResume() {
        super.onResume();
//        handleDataListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
