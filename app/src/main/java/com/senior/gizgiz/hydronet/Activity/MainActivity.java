package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Entity.Notification;
import com.senior.gizgiz.hydronet.Fragment.FeedFragment;
import com.senior.gizgiz.hydronet.Fragment.NotificationFragment;
import com.senior.gizgiz.hydronet.Fragment.PlantCarouselFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.HomeOverviewFragment;
import com.senior.gizgiz.hydronet.Fragment.TradeCarouselFragment;
import com.senior.gizgiz.hydronet.Fragment.UserFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String username;

    private PlantCarouselFragment profileSubmenuFragment;
    private TradeCarouselFragment tradeSubmenuFragment;

    private boolean nowProfileCarousel = false;
    private boolean nowTradeCarousel = false;

    private List<ImageView> menuList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_nav);
        FirebaseUser currentUser = RealTimeDBManager.getCurrentUser();
        if(currentUser != null) {
            final String[] user = new String[1];
            RealTimeDBManager.getDatabase().child("users").orderByChild("email").equalTo(currentUser.getEmail())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                                Log.d("key", childSnapshot.getKey());
                                user[0] = childSnapshot.child("username").getValue(String.class);
                                Log.d("onDataChange", "inside orderByChild('email')" + user[0]);
                                username = user[0];
                            }
                        }
                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });

            menuList = new ArrayList<ImageView>() {{
                add((ImageView) findViewById(R.id.icon_home));
                add((ImageView) findViewById(R.id.icon_my_plant));
                add((ImageView) findViewById(R.id.icon_community));
                add((ImageView) findViewById(R.id.icon_trade));
                add((ImageView) findViewById(R.id.icon_notification));
                add((ImageView) findViewById(R.id.icon_user));
            }};

            final Bundle bundle = new Bundle();
            bundle.putString("USERNAME",username);
            bundle.putString("EMAIL",currentUser.getEmail());

            setActiveTab(0);
            nowProfileCarousel = false;
            nowTradeCarousel = false;
            HomeOverviewFragment overviewFragment;
            if(savedInstanceState == null) {
                overviewFragment = new HomeOverviewFragment();
                overviewFragment.setArguments(bundle);
            } else overviewFragment = (HomeOverviewFragment) getSupportFragmentManager().findFragmentByTag("HOME");
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack("HOME")
                    .replace(R.id.container, overviewFragment,"HOME").commit();
            findViewById(R.id.action_home).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActiveTab(0);
                    nowProfileCarousel = false;
                    nowTradeCarousel = false;
                    HomeOverviewFragment overviewFragment;
                    if(savedInstanceState == null) {
                        overviewFragment = new HomeOverviewFragment();
                        overviewFragment.setArguments(bundle);
                    } else overviewFragment = (HomeOverviewFragment) getSupportFragmentManager().findFragmentByTag("HOME");
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("HOME")
                            .replace(R.id.container, overviewFragment,"HOME").commit();
                }
            });
            findViewById(R.id.action_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActiveTab(1);
                    nowProfileCarousel = true;
                    nowTradeCarousel = false;
                    if(savedInstanceState == null) {
                        profileSubmenuFragment = new PlantCarouselFragment();
                        profileSubmenuFragment.setArguments(bundle);
                    }
                    else profileSubmenuFragment = (PlantCarouselFragment) getSupportFragmentManager().findFragmentByTag("PROFILE");
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("PROFILE")
                            .replace(R.id.container, profileSubmenuFragment,"PROFILE").commit();
                }
            });
            findViewById(R.id.action_community).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActiveTab(2);
                    nowProfileCarousel = false;
                    nowTradeCarousel = false;
                    FeedFragment feedFragment;
                    if(savedInstanceState == null) {
                        feedFragment = new FeedFragment();
                        feedFragment.setArguments(bundle);
                    }
                    else feedFragment = (FeedFragment) getSupportFragmentManager().findFragmentByTag("FEED");
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("FEED")
                            .replace(R.id.container, feedFragment,"FEED").commit();
                }
            });
            findViewById(R.id.action_trade).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActiveTab(3);
                    nowProfileCarousel = false;
                    nowTradeCarousel = true;
                    if(savedInstanceState == null) {
                        tradeSubmenuFragment = new TradeCarouselFragment();
                        tradeSubmenuFragment.setArguments(bundle);
                    }
                    else tradeSubmenuFragment = (TradeCarouselFragment) getSupportFragmentManager().findFragmentByTag("TRADE");
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("TRADE")
                            .replace(R.id.container, tradeSubmenuFragment,"TRADE").commit();
                }
            });
            findViewById(R.id.action_notification).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActiveTab(4);
                    nowProfileCarousel = false;
                    nowTradeCarousel = false;
                    NotificationFragment notificationFragment;
                    if(savedInstanceState == null) {
                        notificationFragment = new NotificationFragment();
                        notificationFragment.setArguments(bundle);
                    } else notificationFragment = (NotificationFragment) getSupportFragmentManager().findFragmentByTag("NOTIFICATION");
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("NOTIFICATION")
                            .replace(R.id.container, notificationFragment,"NOTIFICATION").commit();
                }
            });
            findViewById(R.id.action_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActiveTab(5);
                    nowProfileCarousel = false;
                    nowTradeCarousel = false;
                    UserFragment userFragment;
                    if(savedInstanceState == null) {
                        userFragment = new UserFragment();
                        userFragment.setArguments(bundle);
                    } else userFragment = (UserFragment) getSupportFragmentManager().findFragmentByTag("USER");
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("USER")
                            .replace(R.id.container, userFragment,"USER").commit();
                }
            });

            findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplication(),AddPlantActivity.class));
                }
            });
        } else {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(MainActivity.this,R.style.myDialog));
            View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog,null);
            ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message))
                    .setText("You are not logged in!\nPlease log in to the system to continue.");
            dialogBuilder.setView(dialogCustomLayout);
            final AlertDialog dialog = dialogBuilder.create();
            dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });
            dialogCustomLayout.findViewById(R.id.btn_negative).setClickable(false);
            dialog.show();
        }

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
