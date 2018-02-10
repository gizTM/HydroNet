package com.senior.gizgiz.hydronet.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.HomeCardListViewAdapter;
import com.senior.gizgiz.hydronet.ClassForList.HomeCard;
import com.senior.gizgiz.hydronet.HelperClass.CustomFlipperLayout;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 015 15/1/2018.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CustomFlipperLayout flipper;
    private View contentPage,homeContent;
    // recycler view
//    private RecyclerView homeCardList;
//    private HomeCardRecyclerViewAdapter historyAdapter;
    // list view
    private ListView homeCardList;
    private HomeCardListViewAdapter historyAdapter;

    private List<HomeCard> homeCards = HomeCardListViewAdapter.exampleCards;
    private RelativeLayout warningLayout;

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
        contentStub.setLayoutResource(R.layout.content_home);
        homeContent = contentStub.inflate();

        // define content element
        flipper = homeContent.findViewById(R.id.custom_home_flipper);
        homeCardList = flipper.getSecondPage().findViewById(R.id.history_list);
        warningLayout = flipper.getSecondPage().findViewById(R.id.sensor_popup);
        warningLayout.findViewById(R.id.btn_close_warning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningLayout.setVisibility(View.GONE);
            }
        });

        // list view
        historyAdapter = new HomeCardListViewAdapter(getApplicationContext(),(ArrayList<HomeCard>) homeCards);
        homeCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                HomeCard card = homeCards.get(position);
                Toast.makeText(getApplicationContext(),card.getName()+"is selected",Toast.LENGTH_SHORT);
            }
        });
        homeCardList.setAdapter(historyAdapter);

        // recycler view
//        historyAdapter = new HomeCardRecyclerViewAdapter(getApplicationContext(),HomeCardRecyclerViewAdapter.exampleCards);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        homeCardList.setLayoutManager(layoutManager);
//        homeCardList.setItemAnimator(new DefaultItemAnimator());
//        homeCardList.setAdapter(historyAdapter);
//        homeCardList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), homeCardList, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                HomeCard card = HomeCardRecyclerViewAdapter.exampleCards.get(position);
//                Toast.makeText(getApplicationContext(), card.getName() + " is selected!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

        FabActivity.initAddFAB(getBaseContext(),contentPage.findViewById(R.id.fab_layout));
    }

    void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_home);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
