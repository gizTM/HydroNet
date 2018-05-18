package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.HelperClass.CustomEditText;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 026 26/03/2018.
 */

public class NegotiateActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private CustomEditText negotiateCondition;
    private CustomTextView available;
    public static final int ADD_NEGOTIATION = 1,UPDATE_NEGOTIATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negotiate);
        final String storyOwnerId = getIntent().getStringExtra("OWNER_ID");
        Log.e("ownerId",storyOwnerId);
        final int count = getIntent().getExtras().getInt("COUNT");
        final int[] reserved = {getIntent().getExtras().getInt("RESERVE")};
        final String storyId = getIntent().getStringExtra("STORY_ID");
        int updateCount = getIntent().getExtras().getInt("UPDATE_COUNT");
        String type = getIntent().getExtras().getString("UPDATE_TYPE");
        setup();
        negotiateCondition = findViewById(R.id.negotiate_condition);
        available = findViewById(R.id.remaining_slot);
        final int[] remaining = {count - reserved[0]};
        available.setText(String.valueOf(remaining[0]).concat(" available"));
        if(type.equalsIgnoreCase("update")) {
            remaining[0] += updateCount;
            negotiateCondition.setText(String.valueOf(updateCount));
        } else {
            remaining[0] = 0;
            negotiateCondition.setText(String.valueOf(remaining[0]));
        }
        findViewById(R.id.btn_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int value = Integer.valueOf(negotiateCondition.getText().toString());
//                negotiateCondition.setText(String.valueOf((value/10-1)*10));
                if(remaining[0]>1) negotiateCondition.setText(String.valueOf(--remaining[0]));
            }
        });
        findViewById(R.id.btn_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int value = Integer.valueOf(negotiateCondition.getText().toString());
//                negotiateCondition.setText(String.valueOf((value/10+1)*10));
                if(remaining[0]<count) negotiateCondition.setText(String.valueOf(++remaining[0]));
            }
        });
        findViewById(R.id.btn_negotiate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("OWNER_ID",storyOwnerId);
                data.putExtra("NEGOTIATORID",MainActivity.currentUser.getUid());
                data.putExtra("NEGOTIATOR",MainActivity.username);
                data.putExtra("RESERVED",negotiateCondition.getText().toString());
                data.putExtra("ALL_RESERVED",String.valueOf(reserved[0]));
                data.putExtra("STORY_ID",storyId);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_negotiate);
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

    @Override public void onBackPressed() {
        finish();
    }
}
