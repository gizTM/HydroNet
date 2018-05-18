package com.senior.gizgiz.hydronet.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 022 22/03/2018.
 */


public class FetchSensorActivity extends MicrogearActivity {
    private String messageData = "";
    private static int waterLevels[] =new int[10], i=0;

    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            String message = bundle.getString("message");
            if(message != null) {
                messageData = message.substring(message.indexOf(":")+2).trim();
                if(messageData.length()>5 && messageData.substring(0, 5).equalsIgnoreCase("Ping:") && StringUtils.isNumeric(messageData.substring(6))) {
                    if(i<10) {
                        waterLevels[i] = Integer.valueOf(messageData.substring(6));
                        i++;
                    } else {
                        int sumWaterLevel = 0;
                        for(int waterLevel : waterLevels) sumWaterLevel+=waterLevel;
                        RealTimeDBManager.writeNewSensorData(String.valueOf(Calendar.getInstance().getTime().getTime()),sumWaterLevel/10,0,0);
                        finish();
                    }
                }
            }
            if(string != null) NavigationManager.showToast(getApplicationContext(), string, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        moveTaskToBack(true);
        microgear.connect(APPID+"",KEY+"",SECRET+"",ALIAS+"");
        microgear.setCallback(callback);
    }

    protected void consoleMsg(String key,String console) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString(key, console);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
