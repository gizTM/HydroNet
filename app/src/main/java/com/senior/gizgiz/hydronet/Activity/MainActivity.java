package com.senior.gizgiz.hydronet.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TypefaceSpan;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.senior.gizgiz.hydronet.Fragment.PlantCarouselFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.HomeOverviewFragment;
import com.senior.gizgiz.hydronet.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private String username;

    public static TypefaceSpan regularSpan, boldSpan;

    private PlantCarouselFragment profileSubmenuFragment;
    private boolean nowCarouselFrag = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_nav);

        findViewById(R.id.action_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeOverviewFragment overviewFragment = new HomeOverviewFragment();
//                else overviewFragment = (HomeOverviewFragment) getSupportFragmentManager().getFragments().get(0);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, overviewFragment);
                transaction.commit();
//                startActivity(new Intent(getApplication(), HomeActivity.class));
            }
        });

        findViewById(R.id.action_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowCarouselFrag = true;
                if(savedInstanceState == null) profileSubmenuFragment = new PlantCarouselFragment();
                else profileSubmenuFragment = (PlantCarouselFragment) getSupportFragmentManager().getFragments().get(0);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, profileSubmenuFragment);
                transaction.commit();
//                startActivity(new Intent(getApplication(), HomeActivity.class));
            }
        });

//        startActivity(new Intent(this, LoginActivity.class));

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

//    @Override
//    public void onBackPressed() {
//        if (nowCarouselFrag && !profileSubmenuFragment.onBackPressed()) {
//            super.onBackPressed();
//
//        } else { }
//    }
    @Override
    public void onBackPressed() {
        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible() && nowCarouselFrag && !profileSubmenuFragment.onBackPressed()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
        super.onBackPressed();
    }
}
