package com.senior.gizgiz.hydronet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.senior.gizgiz.hydronet.CustomClassAdapter.HomeCardAdapter;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;

/**
 * Created by Admins on 015 15/1/2018.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ViewFlipper flipper;
    private View contentPage,homeContent;
    private ImageButton expandBT;
    private ListView cardList;
    private HomeCardAdapter historyAdapter;
    private CustomTextView customMenuTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // use recyclable main xml w/ ViewStub content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup toolbar drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // define object element
        contentPage = findViewById(R.id.page_content);
        ViewStub contentStub = contentPage.findViewById(R.id.layout_stub);
        contentStub.setLayoutResource(R.layout.content_home);
        homeContent = contentStub.inflate();
        flipper = homeContent.findViewById(R.id.home_flipper);
        expandBT = homeContent.findViewById(R.id.expand_activities_button);
        cardList = homeContent.findViewById(R.id.history_list);
        historyAdapter = new HomeCardAdapter(getApplicationContext(),HomeCardAdapter.exampleCards);

        // define listener to element
        expandBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showNext();
            }
        });
        cardList.setAdapter(historyAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("id:",id+"");
        switch (id) {
            case R.id.home :
                startActivity(new Intent(this,HomeActivity.class));
                Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                break;
            case R.id.my_plant :
                Toast.makeText(this,"My Plant",Toast.LENGTH_SHORT).show();
                break;
            case R.id.community :
                Toast.makeText(this,"Community",Toast.LENGTH_SHORT).show();
                break;
            case R.id.feed :
                Toast.makeText(this,"Feed",Toast.LENGTH_SHORT).show();
                break;
            case R.id.trading :
                Toast.makeText(this,"Trading",Toast.LENGTH_SHORT).show();
                break;
            default: break;
        }
        return false;
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
