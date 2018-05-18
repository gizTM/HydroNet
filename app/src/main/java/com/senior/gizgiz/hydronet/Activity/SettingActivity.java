package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.R;

public class SettingActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // use recyclable main xml w/ ViewStub content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setup();
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                String uid = MainActivity.currentUser.getUid();
                RealTimeDBManager.getDatabase().child("users/"+uid+"/notificationTokens").setValue(null);
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            }
        });
        findViewById(R.id.btn_my_farm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, SensorManagerActivity.class));
            }
        });
    }
    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_setting);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
