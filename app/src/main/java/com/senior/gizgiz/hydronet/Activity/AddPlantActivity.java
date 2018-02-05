package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.CustomClassAdapter.LocationGridViewAdapter;
import com.senior.gizgiz.hydronet.CustomClassAdapter.PlantBadgeRecyclerViewAdapter;
import com.senior.gizgiz.hydronet.CustomClassForList.DropdownItem;
import com.senior.gizgiz.hydronet.CustomClassForList.PlantBadge;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomFlipperLayout;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.CustomHelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.R;
import com.senior.gizgiz.hydronet.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class AddPlantActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    private DrawerLayout drawer;
//    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
//    private NavigationView navigationView;
//    private CustomFlipperLayout flipper;
    private View contentPage, addPlantContent;

    private RecyclerView plantBadgeList;
    private PlantBadgeRecyclerViewAdapter plantBadgeAdapter;
    private CustomTextView addMoreBTN;
    private LocationGridViewAdapter gridViewAdapter;
    //    private DropdownMenu dropdownMenu;
    private com.exblr.dropdownmenu.DropdownMenu dropdownMenu;

    private int plantCount;
    private Plant nowEditingPlant;
    private List<Plant> toAddPlantList = new ArrayList<>();
    private List<PlantBadge> badgeList = new ArrayList<>();

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
        contentStub.setLayoutResource(R.layout.popup_add_plant);
        addPlantContent = contentStub.inflate();
        findViewById(R.id.fab_layout).setVisibility(View.GONE);

        setup();

        // add plant badge recycler view
        plantBadgeList = addPlantContent.findViewById(R.id.added_badge_list);
        plantBadgeAdapter = new PlantBadgeRecyclerViewAdapter(getApplicationContext(),badgeList);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        plantBadgeList.setLayoutManager(layoutManager);
//        plantBadgeList.setItemAnimator(new DefaultItemAnimator());
        plantBadgeList.setAdapter(plantBadgeAdapter);
        plantBadgeList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), plantBadgeList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PlantBadge card = badgeList.get(position);
                Toast.makeText(getApplicationContext(), card.getPlant().getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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

        dropdownMenu = addPlantContent.findViewById(R.id.dropdown_menu);
        gridViewAdapter = new LocationGridViewAdapter(this, createMockLocationList(32,8));
        View customContentView = getLayoutInflater().inflate(R.layout.popup_location, null, false);
        final GridView gridView = customContentView.findViewById(R.id.location_gridview);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<DropdownItem> items = LocationGridViewAdapter.selectedItem;
                boolean isSelected = LocationGridViewAdapter.itemList.get(position).isSelected();
                if (!isSelected) gridViewAdapter.selectItem(position,plantCount);
                else gridViewAdapter.unselectItem(position);
                Log.i("plantCount : ",plantCount+"");
                Log.i("selected size : ",items.size()+"");
                if (items.size()==0) dropdownMenu.setCurrentTitle("Location");
                else {
                    String title = "";
                    for (int i = 0; i < items.size(); i++) title+=(items.get(i).getInfo()+"\n");
                    dropdownMenu.setCurrentTitle(title);
                }
//                dropdownMenu.dismissCurrentPopupWindow();
            }
        });
//        dropdownMenu.add("Menu1",createMockLocationList(5,false));
        dropdownMenu.add("Location",customContentView);

        nowEditingPlant = new UserPlant("",new GrowHistory());

        addMoreBTN = addPlantContent.findViewById(R.id.btn_add_more);
        addMoreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private ArrayList createMockLocationList(int count, int colNum) {
        ArrayList list = new ArrayList();
        ArrayList<String> row = new ArrayList() {{add("A"); add("B"); add("C"); add("D"); }};
        for(int i=0; i<row.size(); i++) for (int j = 1; j <= colNum; j++) {
            list.add(new DropdownItem(row.get(i)+j+"", false));
        }
        return list;
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
//        drawer = findViewById(R.id.drawer);
//        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.drawer_open,R.string.drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return NavigationManager.navigateTo(this,id);
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawer);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(toggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
        startActivity(new Intent(this, HomeActivity.class));
        return false;
    }
}
