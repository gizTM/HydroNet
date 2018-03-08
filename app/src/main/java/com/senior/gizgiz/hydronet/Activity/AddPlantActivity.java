package com.senior.gizgiz.hydronet.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.ChangePlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.LocationAdapter;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.RecyclerViewAdapter.PlantBadgeRecyclerViewAdapter;
import com.senior.gizgiz.hydronet.ClassForList.DropdownItem;
import com.senior.gizgiz.hydronet.ClassForList.ToGrowPlant;
import com.senior.gizgiz.hydronet.Entity.SystemDefaultPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.HelperClass.ValueStepper;
import com.senior.gizgiz.hydronet.Listener.RecyclerTouchListener;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class AddPlantActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private View contentPage, addPlantContent;
    private static View locationPopup;

    private RecyclerView plantBadgeList;
    private GridView locationList,systemPlantList,userPlantList;
    private CustomTextView dropdownMenu;

    private PlantBadgeRecyclerViewAdapter plantBadgeAdapter;
    private ChangePlantAdapter systemPlantAdapter,userPlantAdapter;
    private LocationAdapter locationGridViewAdapter;

    private int plantCount,selectableCount = plantCount;
    private Plant nowEditingPlant = new Plant("preview");
    private List<String> nowEditingLocation = new ArrayList<>();

    private List<ToGrowPlant> badgeList = new ArrayList<>();
    private static float avgPHLow, avgPHHigh,avgECLow,avgECHigh;
    final DecimalFormat decimalFormat = new DecimalFormat("0.0#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
        contentPage = findViewById(R.id.page_content);
        ViewStub contentStub = contentPage.findViewById(R.id.layout_stub);
        contentStub.setLayoutResource(R.layout.activity_add_plant);
        addPlantContent = contentStub.inflate();
        findViewById(R.id.fab_layout).setVisibility(View.GONE);
        setupPlantBadgeView();
        handlePlantCount();
        handleChangePlant();
        handleLocationDropdown();
        handleAddPlant();
        addPlantContent.findViewById(R.id.plant_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { printDebug(); }
        });
        addPlantContent.findViewById(R.id.btn_start_growing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { startActivity(new Intent(getApplication(),SensorManagerActivity.class)); }
        });
        addPlantContent.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { handleCancelAddPlant(); }
        });
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_add_plant);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.findViewById(R.id.action_quick_user).setVisibility(View.GONE);
        toolbar.findViewById(R.id.action_quick_notification).setVisibility(View.GONE);
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void setupPlantBadgeView() {
        plantBadgeList = addPlantContent.findViewById(R.id.added_badge_list);
        plantBadgeAdapter = new PlantBadgeRecyclerViewAdapter(getApplicationContext(),badgeList);
        plantBadgeList.setAdapter(plantBadgeAdapter);
        plantBadgeList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), plantBadgeList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final ToGrowPlant card = badgeList.get(position);
                locationGridViewAdapter.viewSelectedItem(card.getPlant().getName());
//                Toast.makeText(getApplicationContext(),card.getPlant().getName()+" is selected!",Toast.LENGTH_SHORT).show();
                final View customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_to_grow_plant,null);
                ((ImageView)customView.findViewById(R.id.badge_plant_thumbnail)).setImageResource(
                        ResourceManager.getDrawableID(getApplicationContext(),"ic_plant_"+card.getPlant().getName()));
                ((CustomTextView)customView.findViewById(R.id.badge_plant_name)).setText(card.getPlant().getName());
                ((CustomTextView)customView.findViewById(R.id.badge_plant_count)).setText("x".concat(String.valueOf(card.getCount())));
                CustomTextView avgPH = customView.findViewById(R.id.avg_ph_range);
                CustomTextView avgEC = customView.findViewById(R.id.avg_ec_range);
                if(avgPHLow==avgPHHigh) avgPH.setText(decimalFormat.format(avgPHLow));
                else avgPH.setText(decimalFormat.format(avgPHLow).concat(" - ").concat(decimalFormat.format(avgPHHigh)));
                if(avgECLow==avgECHigh) avgEC.setText(decimalFormat.format(avgECLow));
                else avgEC.setText(decimalFormat.format(avgECLow).concat(" - ").concat(decimalFormat.format(avgECHigh)));
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
                final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                popup.setOutsideTouchable(true);
                popup.showAtLocation(addPlantContent, Gravity.CENTER, 0, 0);
                GridView selectedBadgeGridView = customView.findViewById(R.id.plant_location);
                selectedBadgeGridView.setAdapter(locationGridViewAdapter);
                float pHLow = card.getPlant().getpHLow(), pHHigh = card.getPlant().getpHHigh(),
                        ECLow = card.getPlant().getECLow(), ECHigh = card.getPlant().getECHigh();
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
                                String.valueOf("Do you really want to remove ").concat(card.getPlant().getName()).concat(" from the list?"));
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
                                setErrorMessage("");
                                CustomTextView changeBTN = addPlantContent.findViewById(R.id.btn_change_plant);
                                changeBTN.setClickable(true);
                                changeBTN.setText("change");
                                ((CustomTextView) addPlantContent.findViewById(R.id.plant_count_badge)).setText("x1");
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
//                        dialogBuilder.setTitle(R.string.dialog_confirm).setPositiveButton("Yes",null).setNegativeButton("No",null);
                        dialog.show();
                    }
                });
                customView.findViewById(R.id.dim_popup_overlay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
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
        addPlantContent.findViewById(R.id.btn_change_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowEditingLocation.size()>0) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                            new ContextThemeWrapper(AddPlantActivity.this,R.style.myDialog));
                    View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog,null);
                    ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Your selected spots for "+
                            nowEditingPlant.getName()+" is pending.\n'Yes' to clear un-submitted locations and change to new plant\n" +
                            "'No' to continue with un-submitted plant");
                    dialogBuilder.setView(dialogCustomLayout);
                    final AlertDialog dialog = dialogBuilder.create();
                    dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            nowEditingLocation.clear();
                            locationGridViewAdapter.removeFromAdapter(nowEditingPlant.getName());
                            dropdownMenu.setText(String.valueOf(locationGridViewAdapter.getRemainingSlot())+" spots left");
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
    private void openChangePlantPopup() {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customView = inflater.inflate(R.layout.popup_change_plant,null);
        final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setOutsideTouchable(true);
        popup.showAtLocation(customView, Gravity.CENTER, 0, 0);
        systemPlantList = customView.findViewById(R.id.system_plant_list);
        userPlantList = customView.findViewById(R.id.user_plant_list);
        systemPlantAdapter = new ChangePlantAdapter(getApplicationContext(), PlantAdapter.systemPlants);
        userPlantAdapter = new ChangePlantAdapter(getApplicationContext(), PlantAdapter.exampleUserPlants);
        systemPlantList.setAdapter(systemPlantAdapter);
        userPlantList.setAdapter(userPlantAdapter);
        PlantAdapter.systemPlants.clear();
        RealTimeDBManager.getDatabase().child("systemplants").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PlantAdapter.systemPlants.add(dataSnapshot.getValue(SystemDefaultPlant.class));
                systemPlantAdapter.notifyDataSetChanged();
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                PlantAdapter.systemPlants.remove(dataSnapshot.getValue(SystemDefaultPlant.class));
                systemPlantAdapter.notifyDataSetChanged();
            }
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
        systemPlantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowEditingPlant = PlantAdapter.systemPlants.get(i);
                popup.dismiss();
                ((ImageView) addPlantContent.findViewById(R.id.plant_thumbnail)).setImageResource(
                        ResourceManager.getDrawableID(getApplicationContext(), "ic_plant_" + nowEditingPlant.getName()));
                ((CustomTextView) addPlantContent.findViewById(R.id.plant_name)).setText(nowEditingPlant.getName());
            }
        });
        userPlantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowEditingPlant = PlantAdapter.exampleUserPlants.get(i);
                popup.dismiss();
                ((ImageView) addPlantContent.findViewById(R.id.plant_thumbnail)).setImageResource(
                        ResourceManager.getDrawableID(getApplicationContext(), "ic_plant_" + nowEditingPlant.getName()));
                ((CustomTextView) addPlantContent.findViewById(R.id.plant_name)).setText(nowEditingPlant.getName());
            }
        });
        expandAt(customView, 1);
        customView.findViewById(R.id.toggle_expand1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        NavigationManager.handleSingleExpand(customView,R.id.expand1,R.id.system_plant_layout,isExpandSystemPlant);
                expandAt(customView, 1);
//                        isExpandSystemPlant = !isExpandSystemPlant;
            }
        });
        customView.findViewById(R.id.toggle_expand2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        NavigationManager.handleSingleExpand(customView,R.id.expand2,R.id.user_plant_layout,isExpandUserPlant);
                expandAt(customView, 2);
//                        isExpandUserPlant = !isExpandUserPlant;
            }
        });
        customView.findViewById(R.id.toggle_expand3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        NavigationManager.handleSingleExpand(customView,R.id.expand3,R.id.custom_plant_layout,isExpandCustomPlant);
                expandAt(customView, 3);
//                        isExpandCustomPlant = !isExpandCustomPlant;
            }
        });
        customView.findViewById(R.id.dim_popup_overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });
    }
    private void handleLocationDropdown() {
        final View customView = getLayoutInflater().inflate(R.layout.popup_location, null, false);
        locationPopup = customView;
        locationGridViewAdapter = new LocationAdapter(this, createMockLocationList(32,8));
        locationList = customView.findViewById(R.id.location_gridview);
        locationList.setAdapter(locationGridViewAdapter);
        dropdownMenu = addPlantContent.findViewById(R.id.dropdown_menu);
        dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowEditingPlant.getName().equalsIgnoreCase("preview")) setErrorMessage("Choose plant first!!!");
                else {
                    int locationCount = 0;
                    for (ToGrowPlant toGrowPlant : badgeList)
                        if (toGrowPlant.getPlant().getName().equalsIgnoreCase(nowEditingPlant.getName())) {
                            locationCount = toGrowPlant.getCount();
                            break;
                        }
                    selectableCount = Math.max(plantCount, plantCount + locationCount);
                    locationGridViewAdapter.updatePreviousSelection(nowEditingPlant.getName());
                    final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    popup.setOutsideTouchable(true);
                    popup.showAtLocation(customView, Gravity.CENTER, 0, 0);
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
                        }
                    });
                    customView.findViewById(R.id.btn_select_none).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            setLocationErrorMessage("not yet fixed");
                            List<String> selectedItems = locationGridViewAdapter.selectNone(nowEditingPlant.getName());
                            nowEditingLocation.clear();
//                            nowEditingLocation.addAll(selectedItems);
                        }
                    });
                    customView.findViewById(R.id.dim_popup_overlay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup.dismiss();
                        }
                    });
                }
            }
        });
    }
    private void handlePlantCount() {
        String count = ((CustomTextView)addPlantContent.findViewById(R.id.plant_count_badge)).getText().toString();
        plantCount = Integer.valueOf(count.substring(1));
        addPlantContent.findViewById(R.id.btn_add_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowEditingPlant.getName().equalsIgnoreCase("preview"))
                    setErrorMessage("Choose plant first!!!");
                else {
                    plantCount = (plantCount >= 32) ? 32 : ++plantCount;
                    ((CustomTextView) addPlantContent.findViewById(R.id.plant_count_badge)).setText("x" + plantCount);
                }
            }
        });
        addPlantContent.findViewById(R.id.btn_minus_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowEditingPlant.getName().equalsIgnoreCase("preview"))
                    setErrorMessage("Choose plant first!!!");
                else {
                    plantCount = (plantCount <= 1) ? 1 : --plantCount;
                    ((CustomTextView) addPlantContent.findViewById(R.id.plant_count_badge)).setText("x" + plantCount);
                }
            }
        });
        addPlantContent.findViewById(R.id.btn_add_max).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantCount = locationGridViewAdapter.getRemainingSlot();
                ((CustomTextView) addPlantContent.findViewById(R.id.plant_count_badge)).setText("x" + plantCount);
            }
        });
    }
    private void handleAddPlant() {
        addPlantContent.findViewById(R.id.btn_add_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int locationCount = 0;
                for(ToGrowPlant toGrowPlant : badgeList)
                    if(toGrowPlant.getPlant().getName().equalsIgnoreCase(nowEditingPlant.getName())) {
                        locationCount = toGrowPlant.getCount();
                        break;
                    }
                selectableCount = Math.max(plantCount,plantCount+locationCount);
                if(nowEditingPlant.getName().equalsIgnoreCase("preview"))
                    setErrorMessage("Choose plant first!!!");
                else if(nowEditingLocation.size()==Math.max(selectableCount,plantCount)) {
                    dropdownMenu.setText(String.valueOf(locationGridViewAdapter.getRemainingSlot()).concat(" spots left"));
                    setErrorMessage("");
                    ToGrowPlant nowEditingToGrowPlant = new ToGrowPlant(nowEditingPlant,plantCount,nowEditingLocation);
                    boolean existed = false;
                    for(ToGrowPlant toGrowPlant : badgeList)
                        if(toGrowPlant.getPlant().getName().equalsIgnoreCase(nowEditingToGrowPlant.getPlant().getName())) {
                            existed = true;
                            toGrowPlant.addCount(plantCount);
                            toGrowPlant.setLocationList(nowEditingLocation);
                            break;
                        }
                    if(!existed) badgeList.add(nowEditingToGrowPlant);
                    plantBadgeList.setAdapter(plantBadgeAdapter);
                    updatePHEC();
                    updateNotifyPHEC();
                    locationGridViewAdapter.addPreviouslySelection(nowEditingPlant.getName());
                    //reset layout
                    nowEditingPlant = new Plant("preview");
                    ((ImageView) addPlantContent.findViewById(R.id.plant_thumbnail)).setImageResource(
                            ResourceManager.getDrawableID(getApplicationContext(), "ic_plant_" + nowEditingPlant.getName()));
                    ((CustomTextView) addPlantContent.findViewById(R.id.plant_name)).setText("???");
                    nowEditingLocation.clear();
                    if(locationGridViewAdapter.getRemainingSlot()==0) {
                        CustomTextView changeBTN = addPlantContent.findViewById(R.id.btn_change_plant);
                        changeBTN.setClickable(false);
                        changeBTN.setText("slot full");
                        ((CustomTextView) addPlantContent.findViewById(R.id.plant_count_badge)).setText("x0");
                        setErrorMessage("slot full!!!\ndelete some to add another");
                    } else {
                        CustomTextView changeBTN = addPlantContent.findViewById(R.id.btn_change_plant);
                        changeBTN.setClickable(true);
                        changeBTN.setText("change");
                        ((CustomTextView) addPlantContent.findViewById(R.id.plant_count_badge)).setText("x1");
                        plantCount = 1;
                    }
                } else setErrorMessage("Choose "+selectableCount+" location!!!");
            }
        });
    }
    private void handleCancelAddPlant() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(AddPlantActivity.this,R.style.myDialog));
        View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog,null);
        ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Do you really want to exit add plant page?\nYour previously entered data will be lost.");
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
    private void updatePHEC() {
        float sumpHLow=0,sumpHHigh=0,sumECLow=0,sumECHigh=0;
        for(ToGrowPlant badge : badgeList) {
            sumpHLow+=badge.getPlant().getpHLow();
            sumpHHigh+=badge.getPlant().getpHHigh();
            sumECLow+=badge.getPlant().getECLow();
            sumECHigh+=badge.getPlant().getECHigh();
        }
        avgPHLow = sumpHLow/Math.max(badgeList.size(),1);
        avgPHHigh = sumpHHigh/Math.max(badgeList.size(),1);
        avgECLow = sumECLow/Math.max(badgeList.size(),1);
        avgECHigh = sumECHigh/Math.max(badgeList.size(),1);
        ((ValueStepper)addPlantContent.findViewById(R.id.stepper_ph_lo)).setValue(avgPHLow);
        ((ValueStepper)addPlantContent.findViewById(R.id.stepper_ph_hi)).setValue(avgPHHigh);
        ((ValueStepper)addPlantContent.findViewById(R.id.stepper_ec_lo)).setValue(avgECLow);
        ((ValueStepper)addPlantContent.findViewById(R.id.stepper_ec_hi)).setValue(avgECHigh);
    }
    private void updateNotifyPHEC() {
        for(ToGrowPlant badge : badgeList) {
            float limit = 0.5f;
            if(Math.abs(badge.getPlant().getpHLow()-avgPHLow)>limit ||
                    Math.abs(badge.getPlant().getpHHigh()-avgPHHigh)>limit) {
                badge.setpHOK(false);
                setErrorMessage("Some plant(s) are not OK!\nMaybe grow plants with similar values?");
            }
            else badge.setpHOK(true);
            if(Math.abs(badge.getPlant().getECLow()-avgECLow)>limit ||
                    Math.abs(badge.getPlant().getECHigh()-avgECHigh)>limit) {
                badge.setECOK(false);
                setErrorMessage("Some plant(s) are not OK!\nMaybe grow plants with similar values?");
            }
            else badge.setECOK(true);
            plantBadgeAdapter.notifyDataSetChanged();
        }
    }
    private void setErrorMessage(String errorMessage) {
        ((CustomTextView)addPlantContent.findViewById(R.id.error_field)).setText(errorMessage);
    }
    public static void setLocationErrorMessage(String errorMessage) {
        CustomTextView error = locationPopup
                .findViewById(R.id.location_error_field);
        error.setVisibility(View.VISIBLE);
        error.setText(errorMessage);
//        Toast.makeText(context,"error location!",Toast.LENGTH_SHORT).show();
    }
    private void clearLocationErrorMessage() {
        locationPopup.findViewById(R.id.location_error_field).setVisibility(View.GONE);
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
        Log.e("nowEditingPlant value",nowEditingPlant.getpHLow()+","+nowEditingPlant.getpHHigh()+","+nowEditingPlant.getECLow()+","+nowEditingPlant.getECHigh());
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
            Log.e("diff3",Math.abs(badge.getPlant().getECLow()-avgECLow)+"");
            Log.e("diff4",Math.abs(badge.getPlant().getECHigh()-avgECHigh)+"");
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
        ArrayList<String> row = new ArrayList<String>() {{add("A"); add("B"); add("C"); add("D"); }};
        for(int i=0; i<row.size(); i++) for (int j = 1; j <= colNum; j++) {
            list.add(new DropdownItem(row.get(i)+j+"", false));
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        handleCancelAddPlant();
        return false;
    }

    @Override public void onBackPressed() {
        handleCancelAddPlant();
    }
}
