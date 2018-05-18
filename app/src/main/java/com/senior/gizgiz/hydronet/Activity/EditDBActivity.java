package com.senior.gizgiz.hydronet.Activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.HelperClass.CustomEditText;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admins on 007 07/03/2018.
 */

public class EditDBActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // use recyclable main xml w/ ViewStub content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_db);
        setup();

        final CustomEditText child = findViewById(R.id.child);
        final CustomEditText data = findViewById(R.id.input_data);

//        child.setText("systemplants");
//        data.append("tomato//5.5-6.5//2.0-5.0\n\n");
//        data.append("cucumber//5.8-6.0//1.7-2.5\n\n");
//        data.append("spinach//5.5-6.6//1.8-2.3\n\n");
//        data.append("salad//6.0-7.0//1.1-1.7\n\n");
//        data.append("strawberry//5.5-6.5//1.8-2.2\n\n");
//        data.append("celery//6.5-6.5//1.8-2.4\n\n");
//        data.append("carrot//6.3-6.3//1.6-2.0\n\n");

//        child.setText("items");
//        data.setText("e3//A,B fertilizer for hydroponics//200//mix A for 4-6 hours then mix B;5cc per 1L water each\n\n");
//        data.append("m3//A,B fertilizer for hydroponics//200//mix A for 4-6 hours then mix B;5cc per 1L water each");

//        child.setText("stories");
//        data.setText("gizgiz//0//remark1//4//99//99\n\n");
//        data.append("gizgiz//1//remark2//3//99//99\n\n");
//        data.append("gizgiz//2//remark3//0//0//100\n\n");

        child.setText("grow harvest date");
        data.setText(Calendar.getInstance().getTime().toString());

        findViewById(R.id.btn_confirm_edit_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputData[] = data.getText().toString().split("\n\n");
                if(!data.getText().toString().trim().equalsIgnoreCase("")) {
                    switch (child.getText().toString()) {
                        case "items":
                            for (String item : inputData) {
                                String bundle[] = item.split("//");
                                RealTimeDBManager.writeNewItem(bundle[0], bundle[1], Float.valueOf(bundle[2]), bundle[3]);
                            }
                            break;
                        case "stories":
                            for (String story : inputData) {
                                String bundle[] = story.split("//");
//                                RealTimeDBManager.writeNewSaleStory(bundle[0], Integer.valueOf(bundle[1]),
//                                        bundle[2], bundle[3], Integer.valueOf(bundle[4]), Integer.valueOf(bundle[5]), bundle[6]);
                            }
                            break;
                        case "systemplants":
                            for (String plant : inputData) {
                                String bundle[] = plant.split("//");
                                String pH1, pH2, EC1, EC2;
                                pH1 = bundle[2].substring(0, bundle[1].indexOf("-"));
                                pH2 = bundle[2].substring(bundle[1].indexOf("-") + 1);
                                EC1 = bundle[3].substring(0, bundle[2].indexOf("-"));
                                EC2 = bundle[3].substring(bundle[2].indexOf("-") + 1);
                                RealTimeDBManager.writeNewSystemPlant(bundle[0], Integer.valueOf(bundle[1]),Float.valueOf(pH1), Float.valueOf(pH2),
                                        Float.valueOf(EC1), Float.valueOf(EC2));
                            }
                            break;
                        case "grow harvest date":
                            Date harvestDate = Calendar.getInstance().getTime();
                            RealTimeDBManager.getDatabase().child("growHistories/-L7XjmVYiDwbIgcyApdi/-L7XjmVao1kYDA5Y5C52/harvestDate").setValue(harvestDate);
                            break;
                    }
                } else if(((CheckBox)findViewById(R.id.push_id)).isChecked()) RealTimeDBManager.pushWithUID(child.getText().toString());
                else RealTimeDBManager.push(child.getText().toString());
            }
        });
        findViewById(R.id.btn_remove_from_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealTimeDBManager.remove(child.getText().toString());
            }
        });
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_edit_db);
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
