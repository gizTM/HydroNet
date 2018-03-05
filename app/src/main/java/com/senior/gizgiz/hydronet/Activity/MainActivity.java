package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.senior.gizgiz.hydronet.Fragment.FeedFragment;
import com.senior.gizgiz.hydronet.Fragment.NotificationFragment;
import com.senior.gizgiz.hydronet.Fragment.PlantCarouselFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.HomeOverviewFragment;
import com.senior.gizgiz.hydronet.Fragment.TradeCarouselFragment;
import com.senior.gizgiz.hydronet.Fragment.UserFragment;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PlantCarouselFragment profileSubmenuFragment;
    private TradeCarouselFragment tradeSubmenuFragment;

    private boolean nowProfileCarousel = false;
    private boolean nowTradeCarousel = false;

    private List<ImageView> menuList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_nav);

        menuList = new ArrayList<ImageView>() {{
            add((ImageView) findViewById(R.id.icon_home));
            add((ImageView) findViewById(R.id.icon_my_plant));
            add((ImageView) findViewById(R.id.icon_community));
            add((ImageView) findViewById(R.id.icon_trade));
            add((ImageView) findViewById(R.id.icon_notification));
            add((ImageView) findViewById(R.id.icon_user));
        }};

        setActiveTab(0);
        nowProfileCarousel = false;
        HomeOverviewFragment overviewFragment = new HomeOverviewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, overviewFragment);
        transaction.commit();
        findViewById(R.id.action_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(0);
                nowProfileCarousel = false;
                nowTradeCarousel = false;
                HomeOverviewFragment overviewFragment = new HomeOverviewFragment();
//                else overviewFragment = (HomeOverviewFragment) getSupportFragmentManager().getFragments().get(0);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, overviewFragment);
                transaction.commit();
            }
        });
        findViewById(R.id.action_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(1);
                nowProfileCarousel = true;
                nowTradeCarousel = false;
//                if(savedInstanceState == null)
                    profileSubmenuFragment = new PlantCarouselFragment();
//                else profileSubmenuFragment = (PlantCarouselFragment) getSupportFragmentManager().getFragments().get(0);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, profileSubmenuFragment);
                transaction.commit();
            }
        });
        findViewById(R.id.action_community).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(2);
                nowProfileCarousel = false;
                nowTradeCarousel = false;
                FeedFragment feedFragment = new FeedFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, feedFragment);
                transaction.commit();
            }
        });
        findViewById(R.id.action_trade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(3);
                nowProfileCarousel = false;
                nowTradeCarousel = true;
                tradeSubmenuFragment = new TradeCarouselFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, tradeSubmenuFragment);
                transaction.commit();
            }
        });
        findViewById(R.id.action_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(4);
                nowProfileCarousel = false;
                nowTradeCarousel = false;
                NotificationFragment notificationFragment = new NotificationFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, notificationFragment);
                transaction.commit();
            }
        });
        findViewById(R.id.action_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(5);
                nowProfileCarousel = false;
                nowTradeCarousel = false;
                UserFragment userFragment = new UserFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, userFragment);
                transaction.commit();
            }
        });

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),AddPlantActivity.class));
            }
        });
    }

    private void setActiveTab(int activeTab) {
        for(int i=0; i<menuList.size(); i++) menuList.get(i).setImageAlpha(125);
        menuList.get(activeTab).setImageAlpha(255);
    }

    @Override
    public void onBackPressed() {
        if (nowProfileCarousel && !profileSubmenuFragment.onBackPressed()) super.onBackPressed();
        else if (nowTradeCarousel && !tradeSubmenuFragment.onBackPressed()) super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
    }
}
