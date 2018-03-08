package com.senior.gizgiz.hydronet.Activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.HelperClass.CustomEditText;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 007 07/03/2018.
 */

public class EditDBActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private View contentPage, editDBContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // use recyclable main xml w/ ViewStub content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup toolbar drawer
        setup();
        // define basic recyclable element
        contentPage = findViewById(R.id.page_content);
        ViewStub contentStub = contentPage.findViewById(R.id.layout_stub);
        contentStub.setLayoutResource(R.layout.activity_edit_db);
        editDBContent = contentStub.inflate();
        findViewById(R.id.fab_layout).setVisibility(View.GONE);

        final CustomEditText child = editDBContent.findViewById(R.id.child);
        final CustomEditText data = editDBContent.findViewById(R.id.input_data);

//        child.setText("systemplants");
//        data.append("tomato//5.5-6.5//2.0-5.0\n\n");
//        data.append("cucumber//5.8-6.0//1.7-2.5\n\n");
//        data.append("spinach//5.5-6.6//1.8-2.3\n\n");
//        data.append("salad//6.0-7.0//1.1-1.7\n\n");
//        data.append("strawberry//5.5-6.5//1.8-2.2\n\n");
//        data.append("celery//6.5-6.5//1.8-2.4\n\n");
//        data.append("carrot//6.3-6.3//1.6-2.0\n\n");

//        child.setText("items");
//        data.setText("m3//A,B fertilizer for hydroponics//200//mix A for 4-6 hours then mix B;5cc per 1L water each");

        child.setText("stories");
        data.setText("gizgiz//0//remark1//4//99//99\n\n");
        data.append("gizgiz//1//remark2//3//99//99\n\n");
        data.append("gizgiz//2//remark3//0//0//100\n\n");

//        child.setText("growhistories");
//        data.setText("");

        editDBContent.findViewById(R.id.btn_confirm_edit_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputData[] = data.getText().toString().split("\n\n");
                switch (child.getText().toString()) {
                    case "items" :
                        for(String item : inputData) {
                            String bundle[] = item.split("//");
                            RealTimeDBManager.writeNewItem(bundle[0], bundle[1], Float.valueOf(bundle[2]), bundle[3]);
                        }
                        break;
                    case "stories" :
                        for(String story : inputData) {
                            String bundle[] = story.split("//");
                            RealTimeDBManager.writeNewStory(bundle[0],Integer.valueOf(bundle[1]),
                                    bundle[2],Integer.valueOf(bundle[3]),bundle[4],bundle[5]);
                        }
                        break;
                    case "systemplants" :
                        for(String plant : inputData) {
                            String bundle[] = plant.split("//");
                            String pH1,pH2,EC1,EC2;
                            pH1 = bundle[1].substring(0,bundle[1].indexOf("-"));
                            pH2 = bundle[1].substring(bundle[1].indexOf("-")+1);
                            EC1 = bundle[2].substring(0,bundle[2].indexOf("-"));
                            EC2 = bundle[2].substring(bundle[2].indexOf("-")+1);
                            RealTimeDBManager.writeNewPlant(bundle[0],Float.valueOf(pH1),Float.valueOf(pH2),
                                    Float.valueOf(EC1),Float.valueOf(EC2));
                        }
                        break;
                }
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
        }
        toolbar.findViewById(R.id.action_quick_user).setVisibility(View.GONE);
        toolbar.findViewById(R.id.action_quick_notification).setVisibility(View.GONE);
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
