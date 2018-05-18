package com.senior.gizgiz.hydronet.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.ChangePlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;
import com.senior.gizgiz.hydronet.Adapter.SlidingTabAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.SystemDefaultPlant;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.Fragment.PlantGridViewFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomEditText;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.CustomViewPager;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

public class AddStoryActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private CustomEditText storyDetail;
    private CustomTextView choosePlantBTN;
    private TabLayout tabLayout;
    private CustomViewPager tabPager;
    private SlidingTabAdapter tabAdapter;
    private LinearLayout emptyState;

    @SuppressLint("StaticFieldLeak")
    public static ChangePlantAdapter systemPlantAdapter,userPlantAdapter;
    private List<Plant> systemPlants = new ArrayList<>(),userPlants = new ArrayList<>();
    private GridView systemPlantList,userPlantList;
    private List<String> chosenSystemPlantIds = new ArrayList<>(), chosenUserPlantIds = new ArrayList<>();
    private Plant nowEditingPlant;
    private int nowChosenPlantType;
    private String nowChosenPlantId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        storyDetail = findViewById(R.id.story_detail);
        choosePlantBTN = findViewById(R.id.btn_choose_plant);
        setup();
        handlePlantList();
        findViewById(R.id.selected_plant_layout).setVisibility(View.VISIBLE);
        if(nowEditingPlant == null) findViewById(R.id.selected_plant_layout).setVisibility(View.GONE);
        else findViewById(R.id.selected_plant_layout).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleShareStory();
            }
        });
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_add_story);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void handlePlantList() {
        choosePlantBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
//                View customView = getLayoutInflater().inflate(R.layout.bottom_sheet_plant_select, null);
//                tabLayout = customView.findViewById(R.id.sliding_tab);
//                tabPager = customView.findViewById(R.id.plant_viewpager);
//                setupFragments();
//                new DialogFragmentWindow().show(getSupportFragmentManager(), "");
//                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddStoryActivity.this);
//                bottomSheetDialog.setContentView(customView);
//                BottomSheetBehavior userLockBehavior = UserLockBottomSheetBehavior.from((View) customView.getParent());
//                userLockBehavior.setHideable(false);
//                bottomSheetDialog.show();
                handleChangePlantPopup();
            }
        });
        findViewById(R.id.btn_change_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowEditingPlant = null;
                findViewById(R.id.selected_plant_layout).setVisibility(View.GONE);
                choosePlantBTN.setVisibility(View.VISIBLE);
                choosePlantBTN.setText("Choose plant to sell");
            }
        });
    }
    private void handleChangePlantPopup() {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customView = inflater.inflate(R.layout.popup_change_plant,null);
        final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.showAtLocation(customView, Gravity.CENTER, 0, 0);
        NavigationManager.dimBehind(popup);
        emptyState = customView.findViewById(R.id.empty_state_userplant);
        systemPlantList = customView.findViewById(R.id.system_plant_list);
        userPlantList = customView.findViewById(R.id.user_plant_list);
        systemPlantAdapter = new ChangePlantAdapter(getApplicationContext(),systemPlants);
        userPlantAdapter = new ChangePlantAdapter(getApplicationContext(),userPlants);
        systemPlantList.setAdapter(systemPlantAdapter);
        userPlantList.setAdapter(userPlantAdapter);
        populateSystemPlant();
        populateUserPlant();
        systemPlantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowEditingPlant = systemPlants.get(i);
                popup.dismiss();
                findViewById(R.id.selected_plant_layout).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.selected_plant_img)).setImageResource(
                        ResourceManager.getDrawableID(getBaseContext(), "ic_plant_".concat(nowEditingPlant.getName())));
                ((CustomTextView) findViewById(R.id.selected_plant_name)).setText(nowEditingPlant.getName());
                ((CustomTextView) findViewById(R.id.brief_detail)).setText(nowEditingPlant.getProperty());
                choosePlantBTN.setVisibility(View.GONE);
                nowChosenPlantType = 0;
                nowChosenPlantId = chosenSystemPlantIds.get(i);
            }
        });
        userPlantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowEditingPlant = userPlants.get(i);
                popup.dismiss();
                findViewById(R.id.selected_plant_layout).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.selected_plant_img)).setImageResource(
                        ResourceManager.getDrawableID(getBaseContext(), "ic_plant_".concat(nowEditingPlant.getName())));
                ((CustomTextView) findViewById(R.id.selected_plant_name)).setText(nowEditingPlant.getName());
                ((CustomTextView) findViewById(R.id.brief_detail)).setText(nowEditingPlant.getProperty());
                choosePlantBTN.setVisibility(View.GONE);
                nowChosenPlantType = 1;
                nowChosenPlantId = chosenUserPlantIds.get(i);
            }
        });
        expandAt(customView, 1);
        customView.findViewById(R.id.toggle_expand1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandAt(customView, 1);
            }
        });
        customView.findViewById(R.id.toggle_expand2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandAt(customView, 2);
            }
        });
        customView.findViewById(R.id.toggle_expand3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandAt(customView, 3);
            }
        });
        customView.findViewById(R.id.popup_change_content).setOnClickListener(null);
    }
    private void populateSystemPlant() {
        Log.e("populate","systemPlants");
        systemPlants.clear();
        RealTimeDBManager.getDatabase().child("systemplants").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                systemPlants.add(dataSnapshot.getValue(SystemDefaultPlant.class));
                chosenSystemPlantIds.add(dataSnapshot.getKey());
                systemPlantAdapter.notifyDataSetChanged();
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                systemPlants.remove(dataSnapshot.getValue(SystemDefaultPlant.class));
                chosenSystemPlantIds.remove(dataSnapshot.getKey());
                systemPlantAdapter.notifyDataSetChanged();
            }
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void populateUserPlant() {
        Log.e("populate","userPlants");
        userPlants.clear();
        String uid = MainActivity.currentUser.getUid();
        RealTimeDBManager.getDatabase().child("userPlants/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (!childSnapshot.getKey().equalsIgnoreCase("key")) {
                        final UserPlant userPlant = childSnapshot.getValue(UserPlant.class);
                        userPlants.add(userPlant);
                        chosenUserPlantIds.add(childSnapshot.getKey());
                        userPlantAdapter.notifyDataSetChanged();
                        emptyState.setVisibility(View.GONE);
                    } else emptyState.setVisibility(View.VISIBLE);
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void setupFragments() {
        tabLayout.addTab(tabLayout.newTab().setText("System plant"));
        tabLayout.addTab(tabLayout.newTab().setText("User plant"));
        List<Fragment> plantFragments = new ArrayList<>();
        PlantGridViewFragment plantGridViewFragment = new PlantGridViewFragment();
        Bundle bundle = new Bundle(); bundle.putInt("TYPE",0); plantGridViewFragment.setArguments(bundle);
        plantFragments.add(plantGridViewFragment);
        bundle.putInt("TYPE",1); plantGridViewFragment.setArguments(bundle);
        plantFragments.add(plantGridViewFragment);
        tabAdapter = new SlidingTabAdapter(getFragmentManager(), plantFragments);
        tabPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(tabPager);
    }
    private void expandAt(View customView, int position) {
        View v1 = customView.findViewById(R.id.system_plant_layout);
        View v2 = customView.findViewById(R.id.user_plant_layout);
        View v3 = customView.findViewById(R.id.custom_plant_layout);
        switch (position) {
            case 1 :
                NavigationManager.expand(v1);
                NavigationManager.collapse(v2);
                NavigationManager.collapse(v3);
                break;
            case 2 :
                NavigationManager.collapse(v1);
                NavigationManager.expand(v2);
                NavigationManager.collapse(v3);
                break;
            case 3 :
                NavigationManager.collapse(v1);
                NavigationManager.collapse(v2);
                NavigationManager.expand(v3);
                break;
            default :
                NavigationManager.collapse(v1);
                NavigationManager.collapse(v2);
                NavigationManager.collapse(v3);
                break;
        }
    }
    private void handleCancelAddStory() {
        if(storyDetail.getText().toString().equalsIgnoreCase("")) finish();
        else {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(AddStoryActivity.this, R.style.myDialog));
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
                nowEditingPlant == null) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(AddStoryActivity.this, R.style.myDialog));
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
            if(nowChosenPlantType == 0) {
                SystemDefaultPlant systemDefaultPlant = new SystemDefaultPlant(nowEditingPlant.getName(),nowEditingPlant.getGrowthDuration(),nowEditingPlant.getpHLow(),
                        nowEditingPlant.getpHHigh(),nowEditingPlant.geteCLow(),nowEditingPlant.geteCHigh(),
                        nowEditingPlant.getProperty(),nowEditingPlant.getOtherInfo());
                RealTimeDBManager.writeNewSystemPlantStory(new User(MainActivity.username), StoryAdapter.getTypeStory(),
                        storyDetail.getText().toString(), nowChosenPlantId, systemDefaultPlant);
            } else {
                UserPlant userPlant = new UserPlant(nowEditingPlant.getName(),nowEditingPlant.getGrowthDuration(),nowEditingPlant.getpHLow(),
                        nowEditingPlant.getpHHigh(),nowEditingPlant.geteCLow(),nowEditingPlant.geteCHigh(),
                        nowEditingPlant.getProperty(),nowEditingPlant.getOtherInfo());
                userPlant.setGrowHistories(new ArrayList<GrowHistory>(){{
                    add(new GrowHistory());
                }});
                RealTimeDBManager.writeNewUserPlantStory(new User(MainActivity.username), StoryAdapter.getTypeStory(),
                        storyDetail.getText().toString(), nowChosenPlantId, userPlant);
            }
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
