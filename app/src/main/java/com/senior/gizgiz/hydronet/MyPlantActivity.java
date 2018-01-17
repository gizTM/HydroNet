package com.senior.gizgiz.hydronet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.CustomHelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.CustomHelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Fragment.MaterialFragment;
import com.senior.gizgiz.hydronet.Fragment.PartFragment;
import com.senior.gizgiz.hydronet.Fragment.PlantFragment;

/**
 * Created by Admins on 015 15/1/2018.
 */

public class MyPlantActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View contentPage, myProfileContent,submenu;
    private LinearLayout plantBTN,partBTN,materialBTN;

    private Fragment plantFragment,partFragment,materialFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup toolbar drawer
        setup();

        // define basic recyclable element
        contentPage = findViewById(R.id.page_content);
        ViewStub contentStub = contentPage.findViewById(R.id.layout_stub);
        contentStub.setLayoutResource(R.layout.content_submenu_fragment);
        myProfileContent = contentStub.inflate();

        // define content element
        submenu = findViewById(R.id.submenu);
        ((CustomTextView)submenu.findViewById(R.id.btn_first_label)).setText(R.string.menu_sub_plant);
        ((CustomTextView)submenu.findViewById(R.id.btn_second_label)).setText(R.string.menu_sub_part);
        ((CustomTextView)submenu.findViewById(R.id.btn_third_label)).setText(R.string.menu_sub_material);

        plantBTN = submenu.findViewById(R.id.btn_first);
        partBTN = submenu.findViewById(R.id.btn_second);
        materialBTN = submenu.findViewById(R.id.btn_third);

        //initial fragment
        plantFragment = new PlantFragment();
        partFragment = new PartFragment();
        materialFragment = new MaterialFragment();
        plantBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_frame_content"));
        partBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_transparent"));
        materialBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_transparent"));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, plantFragment);
        ft.commit();

        plantBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_frame_content"));
                partBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_transparent"));
                materialBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_transparent"));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_fragment, plantFragment);
                ft.commit();
            }
        });
        partBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_transparent"));
                partBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_frame_content"));
                materialBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_transparent"));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_fragment, partFragment);
                ft.commit();
            }
        });
        materialBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_transparent"));
                partBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_transparent"));
                materialBTN.setBackground(ResourceManager.getDrawable(getBaseContext(),"bg_frame_content"));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_fragment, materialFragment);
                ft.commit();
            }
        });
    }

    void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_my_profile);
        toolbar.findViewById(R.id.action_quick_user).setVisibility(View.GONE);
        ImageButton notiBTN = toolbar.findViewById(R.id.action_quick_notification);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)notiBTN.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        notiBTN.setLayoutParams(params);
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
