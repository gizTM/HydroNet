package com.senior.gizgiz.hydronet.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.LocationAdapter;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.RecyclerViewAdapter.PlantBadgeRecyclerViewAdapter;
import com.senior.gizgiz.hydronet.ClassForList.DropdownItem;
import com.senior.gizgiz.hydronet.ClassForList.ToGrowPlantBadge;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Listener.RecyclerTouchListener;
import com.exblr.dropdownmenu.DropdownMenu;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class AddPlantActivity extends AppCompatActivity {
    private DrawerLayout drawer;
//    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
//    private NavigationView navigationView;
//    private CustomFlipperLayout flipper;
    private View contentPage, addPlantContent;

    private RecyclerView plantBadgeList;
    private GridView locationList,systemPlantList,userPlantList;
    private DropdownMenu dropdownMenu;
//    private PlantRecyclerViewAdapter systemPlantAdapter,userPlantAdapter;

    private PlantBadgeRecyclerViewAdapter plantBadgeAdapter;
    private PlantAdapter systemPlantAdapter,userPlantAdapter;
    private LocationAdapter locationGridViewAdapter;

    private boolean isExpandSystemPlant = false, isExpandUserPlant = false, isExpandCustomPlant = false;
    private int plantCount;
    private Plant nowEditingPlant = new Plant("salad");
    private List<Plant> toAddPlantList = new ArrayList<>();
    private List<ToGrowPlantBadge> badgeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // use recyclable main xml w/ ViewStub content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup toolbar drawer
        setup();

        // define basic recyclable element
        contentPage = findViewById(R.id.page_content);
        ViewStub contentStub = contentPage.findViewById(R.id.layout_stub);
        contentStub.setLayoutResource(R.layout.activity_add_plant);
        addPlantContent = contentStub.inflate();
        findViewById(R.id.fab_layout).setVisibility(View.GONE);

        setupPlantBadgeView();
        handlePlantCount();
        handleChangePlant();
        handleLocationDropdown();

        addPlantContent.findViewById(R.id.btn_add_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeList.add(new ToGrowPlantBadge(nowEditingPlant,plantCount));
                setupPlantBadgeView();
            }
        });

        addPlantContent.findViewById(R.id.btn_start_growing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),SensorManagerActivity.class));
            }
        });

        addPlantContent.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void setup() {
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
//        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.drawer_open,R.string.drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }
    void setupPlantBadgeView() {
        plantBadgeList = addPlantContent.findViewById(R.id.added_badge_list);
        plantBadgeAdapter = new PlantBadgeRecyclerViewAdapter(getApplicationContext(),badgeList);
        plantBadgeList.setAdapter(plantBadgeAdapter);
        plantBadgeList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), plantBadgeList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToGrowPlantBadge card = badgeList.get(position);
                Toast.makeText(getApplicationContext(), card.getPlant().getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
    void handleChangePlant() {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customView = inflater.inflate(R.layout.popup_change_plant,null);
        addPlantContent.findViewById(R.id.btn_change_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                popup.setOutsideTouchable(true);
                popup.showAtLocation(customView, Gravity.CENTER,0,0);
                systemPlantList = customView.findViewById(R.id.system_plant_list);
                userPlantList = customView.findViewById(R.id.user_plant_list);
                systemPlantAdapter = new PlantAdapter(getApplicationContext(),PlantAdapter.exampleSystemPlants);
                userPlantAdapter = new PlantAdapter(getApplicationContext(),PlantAdapter.exampleUserPlants);
                systemPlantList.setAdapter(systemPlantAdapter);
                userPlantList.setAdapter(userPlantAdapter);
                systemPlantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        nowEditingPlant = PlantAdapter.exampleSystemPlants.get(i);
                        popup.dismiss();
                        ((ImageView)addPlantContent.findViewById(R.id.plant_thumbnail)).setImageResource(
                                ResourceManager.getDrawableID(getApplicationContext(),"ic_plant_"+nowEditingPlant.getName()));
                        ((CustomTextView)addPlantContent.findViewById(R.id.plant_name)).setText(nowEditingPlant.getName());
                    }
                });
                userPlantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        nowEditingPlant = PlantAdapter.exampleUserPlants.get(i);
                        popup.dismiss();
                        ((ImageView) addPlantContent.findViewById(R.id.plant_thumbnail)).setImageResource(
                                ResourceManager.getDrawableID(getApplicationContext(),"ic_plant_"+nowEditingPlant.getName()));
                        ((CustomTextView)addPlantContent.findViewById(R.id.plant_name)).setText(nowEditingPlant.getName());
                    }
                });
                expandAt(customView,1);
                customView.findViewById(R.id.toggle_expand1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        NavigationManager.handleSingleExpand(customView,R.id.expand1,R.id.system_plant_layout,isExpandSystemPlant);
                        expandAt(customView,1);
//                        isExpandSystemPlant = !isExpandSystemPlant;
                    }
                });
                customView.findViewById(R.id.toggle_expand2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        NavigationManager.handleSingleExpand(customView,R.id.expand2,R.id.user_plant_layout,isExpandUserPlant);
                        expandAt(customView,2);
//                        isExpandUserPlant = !isExpandUserPlant;
                    }
                });
                customView.findViewById(R.id.toggle_expand3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        NavigationManager.handleSingleExpand(customView,R.id.expand3,R.id.custom_plant_layout,isExpandCustomPlant);
                        expandAt(customView,3);
//                        isExpandCustomPlant = !isExpandCustomPlant;
                    }
                });
                customView.findViewById(R.id.popup_change_content).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {}
                });
                customView.findViewById(R.id.dim_popup_overlay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { popup.dismiss(); }
                });
            }
        });
    }
    void expandAt(View customView, int position) {
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
    void handleLocationDropdown() {
        dropdownMenu = addPlantContent.findViewById(R.id.dropdown_menu);
        locationGridViewAdapter = new LocationAdapter(this, createMockLocationList(32,8));
        View customContentView = getLayoutInflater().inflate(R.layout.popup_location, null, false);
        locationList = customContentView.findViewById(R.id.location_gridview);
        locationList.setAdapter(locationGridViewAdapter);
        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<DropdownItem> items = LocationAdapter.selectedItem;
                boolean isSelected = LocationAdapter.itemList.get(position).isSelected();
                if (!isSelected) locationGridViewAdapter.selectItem(position,plantCount);
                else locationGridViewAdapter.unselectItem(position);
                if (items.size()==0) dropdownMenu.setCurrentTitle("Location");
                else {
                    String title = "";
                    for (int i = 0; i < items.size(); i++) title+=(items.get(i).getInfo()+"\n");
                    dropdownMenu.setCurrentTitle(title);
                }
//                dropdownMenu.dismissCurrentPopupWindow();
            }
        });
        dropdownMenu.add("Location",customContentView);
    }
    ArrayList createMockLocationList(int count, int colNum) {
        ArrayList list = new ArrayList();
        ArrayList<String> row = new ArrayList() {{add("A"); add("B"); add("C"); add("D"); }};
        for(int i=0; i<row.size(); i++) for (int j = 1; j <= colNum; j++) {
            list.add(new DropdownItem(row.get(i)+j+"", false));
        }
        return list;
    }
    void handlePlantCount() {
        String count = ((CustomTextView)addPlantContent.findViewById(R.id.plant_count_badge)).getText().toString();
        plantCount = new Integer(count.substring(1));
        addPlantContent.findViewById(R.id.btn_add_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantCount = (plantCount>=32)?32:++plantCount;
                ((CustomTextView)addPlantContent.findViewById(R.id.plant_count_badge)).setText("x"+plantCount);
            }
        });
        addPlantContent.findViewById(R.id.btn_minus_plant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantCount = (plantCount<=1)?1:--plantCount;
                ((CustomTextView)addPlantContent.findViewById(R.id.plant_count_badge)).setText("x"+plantCount);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(toggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
        finish();
        return false;
    }
}
