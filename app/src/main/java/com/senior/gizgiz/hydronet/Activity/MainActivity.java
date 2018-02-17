package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.senior.gizgiz.hydronet.Fragment.PlantCarouselFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.HomeOverviewFragment;
import com.senior.gizgiz.hydronet.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private String username;

    private PlantCarouselFragment profileSubmenuFragment;
    private boolean nowCarouselFrag = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_nav);

        findViewById(R.id.action_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowCarouselFrag = false;
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
                nowCarouselFrag = true;
//                if(savedInstanceState == null)
                    profileSubmenuFragment = new PlantCarouselFragment();
//                else profileSubmenuFragment = (PlantCarouselFragment) getSupportFragmentManager().getFragments().get(0);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, profileSubmenuFragment);
                transaction.commit();
            }
        });

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),AddPlantActivity.class));
            }
        });

        // example code for database
//        database = FirebaseDatabase.getInstance();
//        databaseRef = database.getReference();
        // set data reference
//        databaseRef.setValue("message");
        // detect data change from firebase
        /*
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("OnDataChange", dataSnapshot.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("OnCancelled", databaseError.toString());
            }
        });
        */
        // set data and child
//        username = "gizgiz";
//        databaseRef.child("users").child(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).setValue(username);

    }

    @Override
    public void onBackPressed() {
        if (nowCarouselFrag && !profileSubmenuFragment.onBackPressed()) {
//            Toast.makeText(getApplicationContext(),"normal back",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
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
