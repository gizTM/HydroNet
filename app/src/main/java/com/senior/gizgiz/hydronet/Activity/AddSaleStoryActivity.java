package com.senior.gizgiz.hydronet.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.github.kevinsawicki.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.MentionedPlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.BottomSheetListView;
import com.senior.gizgiz.hydronet.HelperClass.CustomEditText;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Admins on 013 13/03/2018.
 */

public class AddSaleStoryActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private CustomEditText storyDetail,tradeCondition;
    private Switch dueDataSwitch;
    private CustomTextView dueDateStatus;
    private ImageView dateTimePicker;

    private MentionedPlantAdapter historyAdapter;
    private List<Plant> plants = new ArrayList<>();
    private List<GrowHistory> histories = new ArrayList<>();
    private List<String> nowChosenPlantId = new ArrayList<>();
    private List<Integer> historyNumbers = new ArrayList<>();
    private int nowChosenHistoryNumber = 0;
    private String nowChosenPlantName = "",nowChosenId="";
    private Date dueDate;
    private UserPlant nowChosenPlant;
    private User owner;
    private boolean lockSwitch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale_story);
        setup();
        handleHistory();
        handleDueDate();
        storyDetail = findViewById(R.id.story_detail);
        tradeCondition = findViewById(R.id.trade_condition);
        toolbar.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleShareStory();
            }
        });
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_add_sale_story);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void handleHistory() {
        findViewById(R.id.add_sale_story_layout).post(new Runnable() {
            @Override
            public void run() {
                fetchHistory();
            }
        });
        final CustomTextView choosePlantBTN = findViewById(R.id.btn_choose_plant);
        if(nowChosenPlant==null) {
            findViewById(R.id.selected_plant_layout).setVisibility(View.GONE);
            choosePlantBTN.setText("Choose plant to sell");
            choosePlantBTN.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.btn_change_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dueDataSwitch.isChecked()) resetDueDate();
                nowChosenPlantName = "";
                nowChosenId = "";
                nowChosenPlant = null;
                findViewById(R.id.selected_plant_layout).setVisibility(View.GONE);
                choosePlantBTN.setVisibility(View.VISIBLE);
                choosePlantBTN.setText("Choose plant to sell");
                dueDataSwitch.setEnabled(true);
            }
        });
        historyAdapter = new MentionedPlantAdapter(this,histories);
        choosePlantBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                View customView = getLayoutInflater().inflate(R.layout.bottom_sheet_sale_plant_select, null);
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddSaleStoryActivity.this);
                bottomSheetDialog.setContentView(customView);
                bottomSheetDialog.show();
                BottomSheetListView plantList = customView.findViewById(R.id.select_plant_list);
                plantList.setAdapter(historyAdapter);
//                plantList.setOnTouchListener(new ListView.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int action = event.getAction();
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//                                v.getParent().requestDisallowInterceptTouchEvent(true);
//                                break;
//                            case MotionEvent.ACTION_UP:
//                                v.getParent().requestDisallowInterceptTouchEvent(false);
//                                break;
//                        }
//                        v.onTouchEvent(event);
//                        return true;
//                    }
//                });
                plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        findViewById(R.id.selected_plant_layout).setVisibility(View.VISIBLE);
                        GrowHistory history = histories.get(i);
                        nowChosenPlantName = history.getPlantName();
                        nowChosenId = nowChosenPlantId.get(i);
                        nowChosenPlant = (UserPlant) plants.get(i);
                        nowChosenHistoryNumber = i;
                        String startDate = histories.get(i).getStartDate();
                        String harvestDate = (history.isHarvested()) ? histories.get(i).getHarvestDate()
                                : String.valueOf(Calendar.getInstance().getTime().getTime());
                        ((ImageView)findViewById(R.id.selected_plant_img)).setImageResource(ResourceManager.getDrawableID(getBaseContext(),"ic_plant_"+nowChosenPlantName));
                        ((CustomTextView)findViewById(R.id.selected_plant_name)).setText(nowChosenPlantName);
                        ((CustomTextView)findViewById(R.id.selected_plant_start_date)).setText("planted on ".concat(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(startDate))))
                                .concat(" (").concat(new TimeAgo().timeAgo(new Date(Long.valueOf(startDate)))).concat(")"));
                        ((CustomTextView)findViewById(R.id.selected_plant_harvest_date)).setText((history.isHarvested())?"harvested on "
                                .concat(ResourceManager.shortDateFormat.format(new Date(Long.valueOf(history.getHarvestDate())))).concat(" (")
                                .concat(new TimeAgo().timeAgo(Long.valueOf(history.getHarvestDate()))).concat(")")
                                :"not harvested");
                        ((CustomTextView)findViewById(R.id.selected_plant_count)).setText("x".concat(String.valueOf(history.getCount())));
                        long timeDiff = Long.valueOf(harvestDate)-Long.valueOf(startDate);
                        CustomTextView time = findViewById(R.id.selected_plant_time);
                        String timeDiffString = String.valueOf(Math.ceil(timeDiff/(24*60*60*1000)));
                        time.setText("grown for ");
                        time.append(timeDiffString.substring(0,timeDiffString.indexOf(".")));
                        time.append(" days");
                        time.append(" (ideally ".concat(String.valueOf(nowChosenPlant.getGrowthDuration())).concat(" days)"));
                        Log.e("nowChosenPlantName", nowChosenPlantName);
                        bottomSheetDialog.hide();
                        choosePlantBTN.setVisibility(View.GONE);
                        if(dueDataSwitch.isChecked()) handleHarvestedDueDate(history.isHarvested());
                    }
                });
            }
        });
    }
    private void fetchHistory() {
        Log.e("fetch history",">>>");
        final String uid = MainActivity.currentUser.getUid();
        RealTimeDBManager.getDatabase().child("userPlants/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                histories.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        final String upid = childSnapshot.getKey();
                        final UserPlant userPlant = childSnapshot.getValue(UserPlant.class);
                        RealTimeDBManager.getDatabase().child("growHistories/" + upid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int historyNumber = 0;
                                String previousPlantName = "";
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                    histories.add(growHistory);
                                    historyAdapter.notifyDataSetChanged();
                                    plants.add(userPlant);
                                    nowChosenPlantId.add(upid);
                                    if(growHistory.getPlantName().equalsIgnoreCase(previousPlantName)) historyNumber++;
                                    else historyNumber = 0;
                                    historyNumbers.add(historyNumber);
                                    previousPlantName = growHistory.getPlantName();
                                }
                            }
                            @Override public void onCancelled(DatabaseError databaseError) {}
                        });
//                        emptyStateHistory.setVisibility(View.GONE);
                    }
                }
//                else emptyStateHistory.setVisibility(View.VISIBLE);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void openDateTimePicker(Date date) {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(new SlideDateTimeListener() {
                    @Override
                    public void onDateTimeSet(Date date) {
                        dueDate = date;
                        dueDateStatus.setTextColor(ResourceManager.getColor(getBaseContext(),R.color.boulder_gray));
                        dueDateStatus.setText("due in ".concat(ResourceManager.shortDateTimeFormat.format(dueDate)));
                        long diff = dueDate.getTime()-new Date().getTime();
                        int dayDiff = (int) Math.ceil(diff/1000/60/60/24);
                        dueDateStatus.append(" (in ".concat(String.valueOf(dayDiff)).concat(" days)"));
                        dateTimePicker.setVisibility(View.VISIBLE);
                    }
                    @Override public void onDateTimeCancel() { }
                })
                .setInitialDate(date)
                .setMinDate(new Date())
                .build()
                .show();
    }
    private void handleDueDate() {
        dateTimePicker = findViewById(R.id.datetime_picker);
        dueDateStatus = findViewById(R.id.due_date_status);
        dueDataSwitch = findViewById(R.id.due_date_switch);
        dueDataSwitch.setChecked(true);
        dateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateTimePicker(dueDate);
            }
        });
        dueDataSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                dueDateStatus.setVisibility(View.VISIBLE);
                if(!checked) {
                    if(dueDate == null && !lockSwitch) openDateTimePicker(new Date());
                    else {
                        dateTimePicker.setVisibility(View.VISIBLE);
                        dueDateStatus.setTextColor(ResourceManager.getColor(getBaseContext(),R.color.boulder_gray));
                        dueDateStatus.setText("choose due date and time");
                    }
                } else {
                    dateTimePicker.setVisibility(View.GONE);
                    if(nowChosenPlant != null) handleHarvestedDueDate(histories.get(nowChosenHistoryNumber).isHarvested());
                    else {
                        dueDateStatus.setTextColor(ResourceManager.getColor(getBaseContext(),R.color.red));
                        dueDateStatus.setText("choose plant to sell first");
                    }
                }
            }
        });
    }
    private void handleHarvestedDueDate(boolean harvested) {
        dueDateStatus.setTextColor(ResourceManager.getColor(getBaseContext(),R.color.boulder_gray));
        if(harvested) {
            lockSwitch = true;
            dueDataSwitch.setChecked(false);
            dueDataSwitch.setEnabled(false);
            dateTimePicker.setVisibility(View.VISIBLE);
            dueDateStatus.setText("set due date manually");
        } else {
            Calendar estimateHarvest = Calendar.getInstance();
            estimateHarvest.setTime(new Date(Long.valueOf(histories.get(nowChosenHistoryNumber).getStartDate())));
            estimateHarvest.add(Calendar.DATE, nowChosenPlant.getGrowthDuration());
            dueDate = estimateHarvest.getTime();
            dueDateStatus.setText("estimated due : ".concat(ResourceManager.shortDateTimeFormat.format(dueDate)));
            long diff = dueDate.getTime()-new Date().getTime();
            int dayDiff = (int) Math.ceil(diff/1000/60/60/24);
            dueDateStatus.append(" (in ".concat(String.valueOf(dayDiff)).concat(" days)"));
        }
    }
    private void resetDueDate() {
        dueDate = null;
        dueDateStatus.setTextColor(ResourceManager.getColor(getBaseContext(),R.color.gray));
        dueDateStatus.setText("");
    }
    private void handleCancelAddStory() {
        if(storyDetail.getText().toString().equalsIgnoreCase("") &&
                tradeCondition.getText().toString().equalsIgnoreCase("") &&
                nowChosenPlantName.equalsIgnoreCase("")) finish();
        else {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(AddSaleStoryActivity.this, R.style.myDialog));
            View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog, null);
            ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Exit page?\nEntered data will be lost.");
            dialogBuilder.setView(dialogCustomLayout);
            final AlertDialog dialog = dialogBuilder.create();
            dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
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
    }
    private void handleShareStory() {
        if(storyDetail.getText().toString().equalsIgnoreCase("") ||
                tradeCondition.getText().toString().equalsIgnoreCase("") ||
                nowChosenPlantName.equalsIgnoreCase("") ||
                dueDate == null) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(AddSaleStoryActivity.this, R.style.myDialog));
            View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog, null);
            ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Some fields are missing.\nCheck again");
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
        }
        else {
            RealTimeDBManager.writeNewSaleStory(new User(MainActivity.username), StoryAdapter.getTypeSale(),storyDetail.getText().toString(),
                    nowChosenId,historyNumbers.get(nowChosenHistoryNumber),0,tradeCondition.getText().toString(),String.valueOf(dueDate.getTime()));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        handleCancelAddStory();
        return false;
    }

    @Override public void onBackPressed() {
        handleCancelAddStory();
    }
}
