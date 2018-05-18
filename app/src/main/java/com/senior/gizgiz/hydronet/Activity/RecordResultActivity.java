package com.senior.gizgiz.hydronet.Activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.GrowResultAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

public class RecordResultActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ListView historyList;
    private ArrayList<GrowHistory> growHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_result);
        historyList = findViewById(R.id.result_list);
        growHistories = new ArrayList<>();
        handleResult();
        setup();
        handleResult();
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_sub_record_result);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_confirm);
        }
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void handleResult() {
        growHistories = getIntent().getParcelableArrayListExtra("HISTORIES");
        GrowResultAdapter growResultAdapter = new GrowResultAdapter(getBaseContext(),growHistories);
        historyList.setAdapter(growResultAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(RecordResultActivity.this,R.style.myDialog));
        View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog,null);
        CustomTextView message = dialogCustomLayout.findViewById(R.id.dialog_message);
        message.setText("Record ");
        for(GrowHistory growHistory : growHistories) {
            if(growHistory.getFailedList()==null) message.append("none ");
            else message.append(String.valueOf(growHistory.getFailedList().size()).concat(" ").concat(growHistory.getPlantName()).concat("s "));
        }
        message.append("as failed?");
        dialogBuilder.setView(dialogCustomLayout);
        final AlertDialog dialog = dialogBuilder.create();
        CustomTextView positiveBTN = dialogCustomLayout.findViewById(R.id.btn_positive);
        CustomTextView negativeBTN = dialogCustomLayout.findViewById(R.id.btn_negative);
        positiveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                finish();
                for(GrowHistory growHistory : growHistories)
                    RealTimeDBManager.writeGrowResult(growHistory.getPlantId(),growHistory.getId(),growHistory.getFailedList(),growHistory.getHarvestList());
                Toast.makeText(getBaseContext(),"Failed plant statistic is recorded",Toast.LENGTH_SHORT).show();
            }
        });
        negativeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
        return false;
    }
}
