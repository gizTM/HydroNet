package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ListView;

import com.senior.gizgiz.hydronet.CustomClassAdapter.HomeCardAdapter;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.CustomHelperClass.MicrogearManager;
import com.senior.gizgiz.hydronet.CustomHelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.CustomHelperClass.SquareThumbnail;
import com.senior.gizgiz.hydronet.CustomHelperClass.TwoPageFlipperLayout;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 015 15/1/2018.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TwoPageFlipperLayout flipper;
    private View contentPage,homeContent;
    private ListView cardList;
    private HomeCardAdapter historyAdapter;
    private SquareThumbnail addBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // use recyclable main xml w/ ViewStub content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup toolbar drawer
        setup();

        // define basic recyclable element
        contentPage = findViewById(R.id.page_content);
        addBTN = contentPage.findViewById(R.id.btn_add);
        ViewStub contentStub = contentPage.findViewById(R.id.layout_stub);
        contentStub.setLayoutResource(R.layout.content_home);
        homeContent = contentStub.inflate();

        // define content element
        flipper = homeContent.findViewById(R.id.custom_home_flipper);
        cardList = flipper.getSecondPage().findViewById(R.id.history_list);
        historyAdapter = new HomeCardAdapter(getApplicationContext(),HomeCardAdapter.exampleCards);
        cardList.setAdapter(historyAdapter);

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),MicrogearActivity.class));
            }
        });
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
