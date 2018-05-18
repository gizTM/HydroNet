package com.senior.gizgiz.hydronet.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;
import android.widget.Toolbar;
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
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.ChangePlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.LocationAdapter;
import com.senior.gizgiz.hydronet.Adapter.RecyclerViewAdapter.PlantBadgeRecyclerViewAdapter;
import com.senior.gizgiz.hydronet.ClassForList.DropdownItem;
import com.senior.gizgiz.hydronet.ClassForList.ToGrowPlant;
import com.senior.gizgiz.hydronet.Entity.SystemDefaultPlant;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.HelperClass.ValueStepper;
import com.senior.gizgiz.hydronet.Listener.RecyclerTouchListener;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class AddPlantActivity extends MicrogearActivity {
    public String APPID = "dht22project";
    public String KEY = "EM9SneXxbpBaoWK";
    public String SECRET = "bMDtXwIWtOA914XhSVtqWdfcX";
    public String ALIAS = "android";

    private DrawerLayout drawer;
    private Toolbar toolbar;
    @SuppressLint("StaticFieldLeak")
    private static View locationPopup;

    private RecyclerView plantBadgeList;
    private GridView locationList,systemPlantList,userPlantList;
    private CustomTextView dropdownMenu,choosePlantBTN;
    private SeekBar plantCountSeekBar;
    private ValueStepper plantCountStepper;

    private PlantBadgeRecyclerViewAdapter plantBadgeAdapter;
    private ChangePlantAdapter systemPlantAdapter,userPlantAdapter;
    private LocationAdapter locationGridViewAdapter;

    private int plantCount,selectableCount = plantCount;
    private Plant nowEditingPlant;
    private List<String> nowEditingLocation = new ArrayList<>();

    private List<ToGrowPlant> badgeList = new ArrayList<>();
    private List<Plant> systemPlants = new ArrayList<>(), userPlants = new ArrayList<>();
    private static float avgPHLow, avgPHHigh,avgECLow,avgECHigh;
    private LinearLayout emptyState;
    private String uid = MainActivity.currentUser.getUid();

    private static int FARM_SIZE = 32, COL_COUNT = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        choosePlantBTN = findViewById(R.id.btn_choose_plant);
        plantBadgeList = findViewById(R.id.added_badge_list);

        if(getIntent().getIntExtra("TYPE",0)==0) {
            nowEditingPlant = getIntent().getParcelableExtra("PLANT");
            ((ImageView) findViewById(R.id.selected_plant_img)).setImageResource(
                    ResourceManager.getDrawableID(getApplicationContext(), "ic_plant_" + nowEditingPlant.getName()));
            ((CustomTextView) findViewById(R.id.selected_plant_name)).setText(nowEditingPlant.getName());
            String pHRange = String.valueOf(nowEditingPlant.getpHLow()).concat(" - ").concat(String.valueOf(nowEditingPlant.getpHHigh()));
            String ecRange = String.valueOf(nowEditingPlant.geteCLow()).concat(" - ").concat(String.valueOf(nowEditingPlant.geteCHigh()));
            ((CustomTextView) findViewById(R.id.avg_ph_range)).setText(pHRange);
            ((CustomTextView) findViewById(R.id.avg_ec_range)).setText(ecRange);
            NavigationManager.expand(findViewById(R.id.selected_plant_layout));
            findViewById(R.id.btn_choose_plant).setVisibility(View.GONE);
            findViewById(R.id.btn_add_more).setVisibility(View.VISIBLE);
        } else if(getIntent().getIntExtra("TYPE",1)==1) {
            nowEditingPlant = getIntent().getParcelableExtra("PLANT");
            ((ImageView) findViewById(R.id.selected_plant_img)).setImageResource(
                    ResourceManager.getDrawableID(getApplicationContext(), "ic_plant_" + nowEditingPlant.getName()));
            ((CustomTextView) findViewById(R.id.selected_plant_name)).setText(nowEditingPlant.getName());
            String pHRange = String.valueOf(nowEditingPlant.getpHLow()).concat(" - ").concat(String.valueOf(nowEditingPlant.getpHHigh()));
            String ecRange = String.valueOf(nowEditingPlant.geteCLow()).concat(" - ").concat(String.valueOf(nowEditingPlant.geteCHigh()));
            ((CustomTextView) findViewById(R.id.avg_ph_range)).setText(pHRange);
            ((CustomTextView) findViewById(R.id.avg_ec_range)).setText(ecRange);
            NavigationManager.expand(findViewById(R.id.selected_plant_layout));
            findViewById(R.id.btn_choose_plant).setVisibility(View.GONE);
            findViewById(R.id.btn_add_more).setVisibility(View.VISIBLE);
        } else {
            String pHRange = "? - ?";
            String ecRange = "? - ?";
            ((CustomTextView) findViewById(R.id.avg_ph_range)).setText(pHRange);
            ((CustomTextView) findViewById(R.id.avg_ec_range)).setText(ecRange);
        }
        setup();
        setupPlantBadgeView();
        handlePlantCount();
        handleChangePlant();
        handleLocationDropdown();
        handleAddPlant();
        dropdownMenu.setText(R.string.plant_location);
        ((CustomTextView)findViewById(R.id.avg_ph_range)).setText(R.string.label_sensor_ph);
        ((CustomTextView)findViewById(R.id.avg_ec_range)).setText(R.string.label_sensor_ec);
        toolbar.findViewById(R.id.btn_start_growing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStartGrowing();
            }
        });
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_add_plant);
        setActionBar(toolbar);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void setupPlantBadgeView() {
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getBaseContext())
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.CENTER;
                    }
                })
                .setRowBreaker(new IRowBreaker() {
                    @Override
                    public boolean isItemBreakRow(@IntRange(from = 0) int position) {
                        return position == 6 || position == 11 || position == 2;
                    }
                })
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .withLastRow(true)
                .build();
        plantBadgeList.setLayoutManager(chipsLayoutManager);
        plantBadgeAdapter = new PlantBadgeRecyclerViewAdapter(getApplicationContext(),badgeList);
        plantBadgeList.setAdapter(plantBadgeAdapter);
        plantBadgeList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), plantBadgeList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final ToGrowPlant card = badgeList.get(position);
                final List<String> location = locationGridViewAdapter.viewSelectedItem(card.getPlant().getName());
//                Toast.makeText(getApplicationContext(),card.getPlant().getName()+" is selected!",Toast.LENGTH_SHORT).show();
                final View customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_to_grow_plant,null);
                ((ImageView)customView.findViewById(R.id.badge_plant_thumbnail)).setImageResource(
                        ResourceManager.getDrawableID(getApplicationContext(),"ic_plant_"+card.getPlant().getName()));
                CustomTextView badgePlantName = customView.findViewById(R.id.badge_plant_name);
                badgePlantName.setText(card.getPlant().getName());
                badgePlantName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String locations = "";
                        for (String location : card.getLocationList()) locations+=location+", ";
                        Log.e("badge location",location.size()+" "+locations);
                        Log.e("badge name",card.getPlant().getName());
                        Log.e("badge count",card.getCount()+"");
                    }
                });
                ((CustomTextView)customView.findViewById(R.id.badge_plant_count)).setText("x".concat(String.valueOf(card.getCount())));
                CustomTextView avgPH = customView.findViewById(R.id.avg_ph_range);
                CustomTextView avgEC = customView.findViewById(R.id.avg_ec_range);
                if(avgPHLow==avgPHHigh) avgPH.setText(ResourceManager.twoDecimalPlaceFormat.format(avgPHLow));
                else avgPH.setText(ResourceManager.twoDecimalPlaceFormat.format(avgPHLow).concat(" - ").concat(ResourceManager.twoDecimalPlaceFormat.format(avgPHHigh)));
                if(avgECLow==avgECHigh) avgEC.setText(ResourceManager.twoDecimalPlaceFormat.format(avgECLow));
                else avgEC.setText(ResourceManager.twoDecimalPlaceFormat.format(avgECLow).concat(" - ").concat(ResourceManager.twoDecimalPlaceFormat.format(avgECHigh)));
                if(!card.ispHOK()) {
                    ((CustomTextView)customView.findViewById(R.id.ph_range))
                            .setTextColor(ResourceManager.getColor(getBaseContext(),R.color.red));
                    ((CustomTextView)customView.findViewById(R.id.avg_ph_range))
                            .setTextColor(ResourceManager.getColor(getBaseContext(),R.color.red));
                } else {
                    ((CustomTextView)customView.findViewById(R.id.ph_range))
                            .setTextColor(ResourceManager.getColor(getBaseContext(),R.color.boulder_gray));
                    ((CustomTextView)customView.findViewById(R.id.avg_ph_range))
                            .setTextColor(ResourceManager.getColor(getBaseContext(),R.color.boulder_gray));
                }
                if(!card.isECOK()) {
                    ((CustomTextView)customView.findViewById(R.id.ec_range))
                            .setTextColor(ResourceManager.getColor(getBaseContext(),R.color.red));
                    ((CustomTextView)customView.findViewById(R.id.avg_ec_range))
                            .setTextColor(ResourceManager.getColor(getBaseContext(),R.color.red));
                } else {
                    ((CustomTextView)customView.findViewById(R.id.ec_range))
                            .setTextColor(ResourceManager.getColor(getBaseContext(),R.color.boulder_gray));
                    ((CustomTextView)customView.findViewById(R.id.avg_ec_range))
                            .setTextColor(ResourceManager.getColor(getBaseContext(),R.color.boulder_gray));
                }
                final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popup.setOutsideTouchable(true);
                popup.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);
                NavigationManager.dimBehind(popup);
                GridView selectedBadgeGridView = customView.findViewById(R.id.plant_location);
                selectedBadgeGridView.setAdapter(locationGridViewAdapter);
                float pHLow = card.getPlant().getpHLow(), pHHigh = card.getPlant().getpHHigh(),
                        ECLow = card.getPlant().geteCLow(), ECHigh = card.getPlant().geteCHigh();
                CustomTextView pHRange = customView.findViewById(R.id.ph_range), ECRange = customView.findViewById(R.id.ec_range);
                if(pHLow==pHHigh) pHRange.setText(String.valueOf(pHLow));
                else pHRange.setText(String.valueOf(pHLow).concat(" - ").concat(String.valueOf(pHHigh)));
                if(ECLow==ECHigh) ECRange.setText(String.valueOf(ECLow));
                else ECRange.setText(String.valueOf(ECLow).concat(" - ").concat(String.valueOf(ECHigh)));
                customView.findViewById(R.id.btn_remove_plant).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                new ContextThemeWrapper(AddPlantActivity.this,R.style.myDialog));
                        View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog,null);
                        ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message)).setText(
                                String.valueOf("Remove ").concat(card.getPlant().getName()).concat(" from planting list?"));
                        dialogBuilder.setView(dialogCustomLayout);
                        final AlertDialog dialog = dialogBuilder.create();
                        dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                popup.dismiss();
                                badgeList.remove(card);
                                updatePHEC();
                                updateNotifyPHEC();
                                clearErrorMessage();
                                choosePlantBTN.setClickable(true);
                                handleChangePlant();
                                choosePlantBTN.setText("Choose plant to add");
//                                ((CustomTextView) findViewById(R.id.selected_plant_count)).setText("x1");
                                plantCount = 1;
                                locationGridViewAdapter.resetViewSelectedItem(card.getPlant().getName());
                                locationGridViewAdapter.removeFromAdapter(card.getPlant().getName().trim());
                                dropdownMenu.setText(String.valueOf(locationGridViewAdapter.getRemainingSlot()).concat(" spots left"));
                                plantBadgeAdapter.notifyDataSetChanged();
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
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        locationGridViewAdapter.resetViewSelectedItem(card.getPlant().getName());
                    }
                });
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
    private void handleChangePlant() {
        if(nowEditingPlant==null) {
            findViewById(R.id.selected_plant_layout).setVisibility(View.GONE);
            NavigationManager.collapse(findViewById(R.id.selected_plant_layout));
            choosePlantBTN.setText("Choose plant to add");
            choosePlantBTN.setVisibility(View.VISIBLE);
            Log.e("nowEditingPlant","null");
        }
        choosePlantBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("choosePlantBTN","clicked");
                NavigationManager.expand(findViewById(R.id.selected_plant_layout));
                choosePlantBTN.setVisibility(View.GONE);
                findViewById(R.id.btn_add_more).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.btn_change_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("change", "clicked");
                if (nowEditingLocation.size() > 0) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                            new ContextThemeWrapper(AddPlantActivity.this, R.style.myDialog));
                    View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog, null);
                    ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Selection for "
                            .concat(nowEditingPlant.getName()).concat(" is pending.\n'Yes' - clear selection\n'No' - continue with ")
                            .concat(nowEditingPlant.getName()));
                    dialogBuilder.setView(dialogCustomLayout);
                    final AlertDialog dialog = dialogBuilder.create();
                    dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            nowEditingLocation.clear();
                            locationGridViewAdapter.removeFromAdapter(nowEditingPlant.getName());
                            dropdownMenu.setText(String.valueOf(locationGridViewAdapter.getRemainingSlot()).concat(" spots left"));
                            openChangePlantPopup();
                        }
                    });
                    dialogCustomLayout.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                } else openChangePlantPopup();
            }
        });
    }
    private void populateSystemPlant() {
        systemPlants.clear();
        RealTimeDBManager.getDatabase().child("systemplants").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                systemPlants.add(dataSnapshot.getValue(SystemDefaultPlant.class));
                systemPlantAdapter.notifyDataSetChanged();
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                systemPlants.remove(dataSnapshot.getValue(SystemDefaultPlant.class));
                systemPlantAdapter.notifyDataSetChanged();
            }
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void populateUserPlant() {
        userPlants.clear();
        String uid = MainActivity.currentUser.getUid();
        RealTimeDBManager.getDatabase().child("userPlants/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (!childSnapshot.getKey().equalsIgnoreCase("key")) {
                        final UserPlant userPlant = childSnapshot.getValue(UserPlant.class);
                        userPlants.add(userPlant);
                        userPlantAdapter.notifyDataSetChanged();
                        emptyState.setVisibility(View.GONE);
                    } else emptyState.setVisibility(View.VISIBLE);
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void openChangePlantPopup() {
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
//                findViewById(R.id.selected_plant_layout).setVisibility(View.VISIBLE);
//                NavigationManager.expand(findViewById(R.id.selected_plant_layout));
//                choosePlantBTN.setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.selected_plant_img)).setImageResource(
                        ResourceManager.getDrawableID(getApplicationContext(), "ic_plant_" + nowEditingPlant.getName()));
                ((CustomTextView) findViewById(R.id.selected_plant_name)).setText(nowEditingPlant.getName());
                String pHRange = ResourceManager.twoDecimalPlaceFormat.format(nowEditingPlant.getpHLow()).concat(" - ")
                        .concat(ResourceManager.twoDecimalPlaceFormat.format(nowEditingPlant.getpHHigh()));
                String ecRange = ResourceManager.twoDecimalPlaceFormat.format(nowEditingPlant.geteCLow()).concat(" - ")
                        .concat(ResourceManager.twoDecimalPlaceFormat.format(nowEditingPlant.geteCHigh()));
                ((CustomTextView) findViewById(R.id.ph_range)).setText(pHRange);
                ((CustomTextView) findViewById(R.id.ec_range)).setText(ecRange);
            }
        });
        userPlantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowEditingPlant = userPlants.get(i);
                popup.dismiss();
//                findViewById(R.id.selected_plant_layout).setVisibility(View.VISIBLE);
//                NavigationManager.expand(findViewById(R.id.selected_plant_layout));
//                choosePlantBTN.setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.selected_plant_img)).setImageResource(
                        ResourceManager.getDrawableID(getApplicationContext(), "ic_plant_" + nowEditingPlant.getName()));
                ((CustomTextView) findViewById(R.id.selected_plant_name)).setText(nowEditingPlant.getName());
                String pHRange = ResourceManager.twoDecimalPlaceFormat.format(nowEditingPlant.getpHLow()).concat(" - ")
                        .concat(ResourceManager.twoDecimalPlaceFormat.format(nowEditingPlant.getpHHigh()));
                String ecRange = ResourceManager.twoDecimalPlaceFormat.format(nowEditingPlant.geteCLow()).concat(" - ")
                        .concat(ResourceManager.twoDecimalPlaceFormat.format(nowEditingPlant.geteCHigh()));
                ((CustomTextView) findViewById(R.id.ph_range)).setText(pHRange);
                ((CustomTextView) findViewById(R.id.ec_range)).setText(ecRange);
            }
        });
        expandAt(customView, 1);
        customView.findViewById(R.id.toggle_expand1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandAt(customView, 1);
//                        isExpandSystemPlant = !isExpandSystemPlant;
            }
        });
        customView.findViewById(R.id.toggle_expand2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandAt(customView, 2);
//                        isExpandUserPlant = !isExpandUserPlant;
            }
        });
        customView.findViewById(R.id.toggle_expand3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandAt(customView, 3);
//                        isExpandCustomPlant = !isExpandCustomPlant;
            }
        });
        customView.findViewById(R.id.popup_change_content).setOnClickListener(null);
    }
    private void handleLocationDropdown() {
        final View customView = getLayoutInflater().inflate(R.layout.popup_location, null, false);
        locationPopup = customView;
        locationGridViewAdapter = new LocationAdapter(this, createMockLocationList(FARM_SIZE,COL_COUNT));
        locationList = customView.findViewById(R.id.location_gridview);
        locationList.setAdapter(locationGridViewAdapter);
        dropdownMenu = findViewById(R.id.dropdown_menu);
        findViewById(R.id.btn_choose_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowEditingPlant == null) setErrorMessage("Choose plant first!!!");
                else {
                    int locationCount = 0;
                    for (ToGrowPlant toGrowPlant : badgeList)
                        if (toGrowPlant.getPlant().getName().equalsIgnoreCase(nowEditingPlant.getName())) {
                            locationCount = toGrowPlant.getCount();
                            break;
                        }
                    selectableCount = Math.max(plantCount, plantCount + locationCount);
                    locationGridViewAdapter.updatePreviousSelection(nowEditingPlant.getName());
                    final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popup.setOutsideTouchable(true);
                    popup.showAtLocation(customView, Gravity.TOP, 0, 0);
                    NavigationManager.dimBehind(popup);
                    clearLocationErrorMessage();
                    locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            List<DropdownItem> items = locationGridViewAdapter.getItemList();
                            List<String> selectedItems, previouslySelectedItems = locationGridViewAdapter.getPreviouslySelectedItems();
                            boolean isSelected = locationGridViewAdapter.getItemList().get(position).isSelected();
                            if (!previouslySelectedItems.contains(items.get(position).getInfo())) {
                                if (!isSelected)
                                    selectedItems = locationGridViewAdapter.selectItem(nowEditingPlant.getName(), position, selectableCount);
                                else
                                    selectedItems = locationGridViewAdapter.unSelectItem(nowEditingPlant.getName(), position);
                                nowEditingLocation.clear();
                                nowEditingLocation.addAll(selectedItems);
                                Collections.sort(nowEditingLocation);
                                ((CustomTextView)findViewById(R.id.selected_plant_location)).setText(nowEditingLocation.toString());
                            } else
                                setLocationErrorMessage("This location is selected!!!\nSelect another location in white.");
                            dropdownMenu.setText(String.valueOf(locationGridViewAdapter.getRemainingSlot()).concat(" spots left"));
                        }
                    });
                    customView.findViewById(R.id.btn_select_all).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            setLocationErrorMessage("not yet fixed");
                            List<String> selectedItems = locationGridViewAdapter.selectAll(nowEditingPlant.getName(), selectableCount);
                            nowEditingLocation.clear();
                            nowEditingLocation.addAll(selectedItems);
                            Collections.sort(nowEditingLocation);
                            ((CustomTextView)findViewById(R.id.selected_plant_location)).setText(nowEditingLocation.toString());
                        }
                    });
                    customView.findViewById(R.id.btn_select_none).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            setLocationErrorMessage("not yet fixed");
                            List<String> selectedItems = locationGridViewAdapter.selectNone(nowEditingPlant.getName());
                            nowEditingLocation.clear();
//                            nowEditingLocation.addAll(selectedItems);
//                                Collections.sort(nowEditingLocation);
                            ((CustomTextView)findViewById(R.id.selected_plant_location)).setText(nowEditingLocation.toString());
                        }
                    });
                    customView.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup.dismiss();
                        }
                    });
                    customView.findViewById(R.id.popup_location).setOnClickListener(null);
                }
            }
        });
    }
    private void handlePlantCount() {
        plantCountStepper = findViewById(R.id.selected_plant_count);
        plantCountStepper.setMinValue(0);
        plantCountStepper.setMaxValue(FARM_SIZE);
        plantCountStepper.setOnValueChangedListener(new ValueStepper.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                plantCount = value;
            }
        });
    }
    private void handleAddPlant() {
        findViewById(R.id.btn_add_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int locationCount = 0;
                for(ToGrowPlant toGrowPlant : badgeList)
                    if(nowEditingPlant != null && toGrowPlant.getPlant().getName().equalsIgnoreCase(nowEditingPlant.getName())) {
                        locationCount = toGrowPlant.getCount();
                        break;
                    }
                selectableCount = Math.max(plantCount,plantCount+locationCount);
                if(nowEditingPlant == null) setErrorMessage("Choose plant first!!!");
                else if(nowEditingLocation.size()!=0 && nowEditingLocation.size()==Math.max(selectableCount,plantCount)) {
                    Log.e("recent location",nowEditingLocation.size()+"");
                    dropdownMenu.setText(String.valueOf(locationGridViewAdapter.getRemainingSlot()).concat(" spots left"));
                    setErrorMessage("");
                    ToGrowPlant nowEditingToGrowPlant = new ToGrowPlant(nowEditingPlant,plantCount,nowEditingLocation);
                    boolean existed = false;
                    for(ToGrowPlant toGrowPlant : badgeList)
                        if(toGrowPlant.getPlant().getName().equalsIgnoreCase(nowEditingToGrowPlant.getPlant().getName())) {
                            existed = true;
                            toGrowPlant.addCount(plantCount);
                            Log.e("=====",".=======");
                            toGrowPlant.setLocationList(nowEditingLocation);
                            break;
                        }
                    if(!existed) badgeList.add(nowEditingToGrowPlant);
                    plantBadgeAdapter.notifyDataSetChanged();
                    for(ToGrowPlant inBadge : badgeList) {
                        Log.e("in badge",inBadge.getPlant().getName());
                        Log.e("in badge",inBadge.getCount()+"");
                        String locate = "";
                        for(String location : inBadge.getLocationList()) locate+=location+",";
                        Log.e("in badge",inBadge.getLocationList().size()+" "+locate);
                    }
                    updatePHEC();
                    updateNotifyPHEC();
                    locationGridViewAdapter.addPreviouslySelection(nowEditingPlant.getName());
                    //reset layout
//                    ((CustomTextView)findViewById(R.id.btn_add_max)).setText(String.valueOf(locationGridViewAdapter.getRemainingSlot()));
                    nowEditingPlant = null;
//                    findViewById(R.id.selected_plant_layout).setVisibility(View.GONE);
                    NavigationManager.collapse(findViewById(R.id.selected_plant_layout));
                    choosePlantBTN.setText("Choose plant to add");
                    choosePlantBTN.setVisibility(View.VISIBLE);
                    findViewById(R.id.btn_add_more).setVisibility(View.GONE);
                    ((ImageView) findViewById(R.id.selected_plant_img)).setImageResource(
                            ResourceManager.getDrawableID(getApplicationContext(), "ic_plant_preview"));
                    ((CustomTextView) findViewById(R.id.selected_plant_name)).setText("???");
                    ((CustomTextView) findViewById(R.id.ph_range)).setText("?");
                    ((CustomTextView) findViewById(R.id.ec_range)).setText("?");
                    plantCountStepper.setValue(0);
                    ((CustomTextView) findViewById(R.id.selected_plant_location)).setText(R.string.add_location_prompt);
                    nowEditingLocation.clear();
                    if(locationGridViewAdapter.getRemainingSlot()==0) {
                        choosePlantBTN.setClickable(false);
                        choosePlantBTN.setText("Slot full");
                        choosePlantBTN.setVisibility(View.GONE);
//                        ((CustomTextView) findViewById(R.id.selected_plant_count)).setText("x0");
                        setErrorMessage("Slot full!!! Delete some to add another");
                    } else {
                        choosePlantBTN.setClickable(true);
                        handleChangePlant();
//                        ((CustomTextView) findViewById(R.id.selected_plant_count)).setText("x1");
                        plantCount = 1;
                    }
                } else setErrorMessage("Choose "+selectableCount+" location!!!");
            }
        });
    }
    private void handleCancelAddPlant() {
        if(badgeList.size()==0 && nowEditingPlant==null) finish();
        else {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(AddPlantActivity.this, R.style.myDialog));
            View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog, null);
            ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Exit page?\nEntered data will be lost.");
            dialogBuilder.setView(dialogCustomLayout);
            final AlertDialog dialog = dialogBuilder.create();
            dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    locationGridViewAdapter.resetLocationAdapter();
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
    private void updatePHEC() {
        float sumpHLow=0,sumpHHigh=0,sumECLow=0,sumECHigh=0;
        for(ToGrowPlant badge : badgeList) {
            sumpHLow+=badge.getPlant().getpHLow();
            sumpHHigh+=badge.getPlant().getpHHigh();
            sumECLow+=badge.getPlant().geteCLow();
            sumECHigh+=badge.getPlant().geteCHigh();
        }
        avgPHLow = sumpHLow/Math.max(badgeList.size(),1);
        avgPHHigh = sumpHHigh/Math.max(badgeList.size(),1);
        avgECLow = sumECLow/Math.max(badgeList.size(),1);
        avgECHigh = sumECHigh/Math.max(badgeList.size(),1);
//        ((ValueStepper)findViewById(R.id.stepper_ph_lo)).setValue(avgPHLow);
//        ((ValueStepper)findViewById(R.id.stepper_ph_hi)).setValue(avgPHHigh);
//        ((ValueStepper)findViewById(R.id.stepper_ec_lo)).setValue(avgECLow);
//        ((ValueStepper)findViewById(R.id.stepper_ec_hi)).setValue(avgECHigh);
        String pHRange = ResourceManager.twoDecimalPlaceFormat.format(avgPHLow).concat(" - ").concat(String.valueOf(avgPHHigh));
        String ecRange = ResourceManager.twoDecimalPlaceFormat.format(avgECLow).concat(" - ").concat(String.valueOf(avgECHigh));
        ((CustomTextView)findViewById(R.id.avg_ph_range)).setText(pHRange);
        ((CustomTextView)findViewById(R.id.avg_ec_range)).setText(ecRange);
    }
    private void updateNotifyPHEC() {
        for(ToGrowPlant badge : badgeList) {
            float limit = 0.5f;
            if(Math.abs(badge.getPlant().getpHLow()-avgPHLow)>limit ||
                    Math.abs(badge.getPlant().getpHHigh()-avgPHHigh)>limit) {
                badge.setpHOK(false);
                setErrorMessage("Some plant(s) are not OK! Maybe grow plants with similar values?");
            }
            else badge.setpHOK(true);
            if(Math.abs(badge.getPlant().geteCLow()-avgECLow)>limit ||
                    Math.abs(badge.getPlant().geteCHigh()-avgECHigh)>limit) {
                badge.setECOK(false);
                setErrorMessage("Some plant(s) are not OK! Maybe grow plants with similar values?");
            }
            else badge.setECOK(true);
            plantBadgeAdapter.notifyDataSetChanged();
        }
    }
    private void handleStartGrowing() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
            new ContextThemeWrapper(AddPlantActivity.this, R.style.myDialog));
        View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog, null);
        ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Connected farm will start working.\nGrow now?");
        dialogBuilder.setView(dialogCustomLayout);
        final AlertDialog dialog = dialogBuilder.create();
        dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                final String uid = MainActivity.currentUser.getUid();
                RealTimeDBManager.getDatabase().child("userPlants/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String startDate = String.valueOf(Calendar.getInstance().getTime().getTime());
                        for(int i=0; i<badgeList.size(); i++) {
                            ToGrowPlant plantInBadge = badgeList.get(i);
                            Plant plant = plantInBadge.getPlant();
                            List<String> locations = locationGridViewAdapter.viewSelectedItem(plant.getName());
                            boolean existed = false;
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                UserPlant userPlant = childSnapshot.getValue(UserPlant.class);
                                Log.e("user plant name", userPlant.getName());
                                if (userPlant.getName().equalsIgnoreCase(plant.getName())) {
                                    RealTimeDBManager.writeExistingUserPlant(plant.getName(),childSnapshot.getKey(),plantInBadge.getCount(),locations,userPlant.getGrowthDuration(),startDate);
                                    existed = true;
                                }
                            }
                            if(!existed)
                                RealTimeDBManager.writeNewUserPlant(plant.getName(),plantInBadge.getCount(),locations,plantInBadge.getPlant().getGrowthDuration(),plant.getpHLow(),plant.getpHHigh(),plant.geteCLow(),plant.geteCHigh(),startDate,plant.getProperty());
//                            microgear.connect(APPID,KEY,SECRET,ALIAS);
//                            microgear.setCallback(callback);
//                            microgear.subscribe("user");
//                            microgear.publish("user","start");
                            locationGridViewAdapter.resetViewSelectedItem(plant.getName());
                        }
                        Toast.makeText(getBaseContext(),"You have successfully added new plants",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                    }
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
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
    private void setErrorMessage(String errorMessage) {
        findViewById(R.id.error_field).setVisibility(View.VISIBLE);
        ((CustomTextView)findViewById(R.id.error_field)).setText(errorMessage);
    }
    private void clearErrorMessage() {
        findViewById(R.id.error_field).setVisibility(View.GONE);
        ((CustomTextView)findViewById(R.id.error_field)).setText("");
    }
    public static void setLocationErrorMessage(String errorMessage) {
        CustomTextView error = locationPopup
                .findViewById(R.id.location_error_field);
        error.setVisibility(View.VISIBLE);
        error.setText(errorMessage);
    }
    private void clearLocationErrorMessage() {
        locationPopup.findViewById(R.id.location_error_field).setVisibility(View.GONE);
        ((CustomTextView) locationPopup.findViewById(R.id.location_error_field)).setText("");
    }
    private void printDebug() {
        String locations = "",previousSelections="",currentPlantLocation="",plantInMap="";
        for(String unit:nowEditingLocation) locations += unit+", ";
        for(String unit:locationGridViewAdapter.getPreviouslySelectedItems()) previousSelections+=unit+", ";
//        for(String unit:locationGridViewAdapter.getCurrentlySelectedItems()) currentPlantLocation+=unit+", ";
        for(Map.Entry<String,List<String>> entry : locationGridViewAdapter.getLocationListForPlant().entrySet()) {
            plantInMap+=entry.getKey()+", ";
        }
        Log.e("nowEditingPlant",nowEditingPlant.getName());
        Log.e("nowEditingPlant value",nowEditingPlant.getpHLow()+","+nowEditingPlant.getpHHigh()+","+nowEditingPlant.geteCLow()+","+nowEditingPlant.geteCHigh());
        Log.e("nowEditingLocation",nowEditingLocation.size()+" "+locations);
//        Log.e("current selection",locationGridViewAdapter.getCurrentlySelectedItems().size()+" "+currentPlantLocation);
        Log.e("previous selection",locationGridViewAdapter.getPreviouslySelectedItems().size()+" "+previousSelections);
//        Log.i("plant count",plantCount+"");
        Log.e("selectable plant count",selectableCount+"");
        Log.e("plant in map",locationGridViewAdapter.getLocationListForPlant().size()+" "+plantInMap);
//        Log.e("avg value",avgPHLow+","+avgPHHigh+","+avgECLow+","+avgECHigh);
        for(ToGrowPlant badge : badgeList) {
            Log.e("diff1",Math.abs(badge.getPlant().getpHLow()-avgPHLow)+"");
            Log.e("diff2",Math.abs(badge.getPlant().getpHHigh()-avgPHHigh)+"");
            Log.e("diff3",Math.abs(badge.getPlant().geteCLow()-avgECLow)+"");
            Log.e("diff4",Math.abs(badge.getPlant().geteCHigh()-avgECHigh)+"");
            System.out.println();
            plantBadgeAdapter.notifyDataSetChanged();
        }
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
    private ArrayList<DropdownItem> createMockLocationList(int count, int colNum) {
        ArrayList<DropdownItem> list = new ArrayList<>();
        ArrayList<String> row = new ArrayList<String>() {{
            add("A"); add("B"); add("C"); add("D"); add("E"); add("F"); add("G"); add("H"); add("I"); add("J");
            add("K"); add("L"); add("M"); add("N"); add("O"); add("P"); add("Q"); add("R"); add("S"); add("T");
            add("U"); add("V"); add("W"); add("X"); add("Y"); add("Z");
        }};
        int x = 0;
        for(int i=0; i<row.size(); i++) for (int j = 1; j <= colNum; j++) {
            x++;
            if(x<=count) list.add(new DropdownItem(row.get(i)+j+"", false));
        }
        return list;
    }
    public static void setFarmSize(int farmSize,int colNum) { FARM_SIZE = farmSize; COL_COUNT = colNum; }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        handleCancelAddPlant();
        return false;
    }
    @Override public void onBackPressed() {
        handleCancelAddPlant();
    }
}
