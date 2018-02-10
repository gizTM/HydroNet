package com.senior.gizgiz.hydronet.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.R;

import io.netpie.microgear.MicrogearEventListener;
import vn.luongvo.widget.iosswitchview.SwitchView;

/**
 * Created by Admins on 008 08/02/2018.
 */

public class SensorManagerActivity extends MicrogearActivity {
    private String APPID = super.APPID; //APP_ID
    private String KEY = super.KEY; //KEY
    private String SECRET = super.SECRET; //SECRET
    private String ALIAS = super.ALIAS;
    private MicrogearCallBack callBack = new MicrogearCallBack();

    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            String message = bundle.getString("message");
            if(message != null) pumpStatus = message.substring(message.indexOf(":")+2).trim().toUpperCase();
            if(string != null) NavigationManager.showToast(getApplicationContext(), string, 1000);
            netPieLog.setText("pump is now: "+pumpStatus);
        }
    };

    private SwitchView pumpToggle;
    private CustomTextView netPieLog;
    private String pumpStatus ="";
    private boolean pumpOn = false;
    private boolean persist = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_manager);
        pumpToggle = findViewById(R.id.switch_pump);
        netPieLog = findViewById(R.id.netpie_log);

        microgear.connect(APPID+"",KEY+"",SECRET+"",ALIAS+"");
        microgear.setCallback(callback);
        pumpToggle.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {
                pumpOn = isChecked;
//                NavigationManager.showToast(getApplicationContext(),"!!!pumpOn: '"+pumpOn+"'",1000);
                microgear.chat("esp8266",pumpOn?"on":"off");
            }
        });

//        if(pumpStatus.equalsIgnoreCase("on")) pumpToggle.setChecked(true);
//        else  pumpToggle.setChecked(false);
//
//        if(pumpOn && pumpStatus.equalsIgnoreCase("off")) {
//            NavigationManager.showToast(getApplicationContext(),"!!!start pump!!!",1000);
//            microgear.chat("esp8266","on");
//        }
//        if(!pumpOn && pumpStatus.equalsIgnoreCase("on")) {
//            NavigationManager.showToast(getApplicationContext(),"!!!stop pump!!!",1000);
//            microgear.chat("esp8266","off");
//        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void consoleMsg(String key,String console) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString(key, console);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    class MicrogearCallBack implements MicrogearEventListener {
        @Override
        public void onConnect() {
            String console = "Now I'm connected with netpie";
            consoleMsg("myKey",console);
            Log.i("Connected",console+"");
        }

        @Override
        public void onMessage(String topic, String message) {
            consoleMsg("message",topic+" : "+message);
//            pumpStatus = message.trim().toLowerCase();
            Log.i("Message",topic+" : "+message);
        }

        @Override
        public void onPresent(String token) {
            String console = "New friend connect : "+token;
            consoleMsg("myKey",console);
            Log.i("present",console+"");
        }

        @Override
        public void onAbsent(String token) {
            String console = "Friend lost :"+token;
            consoleMsg("myKey",console);
            Log.i("absent",console+"");
        }

        @Override
        public void onDisconnect() {
            String console = "Disconnected";
            consoleMsg("myKey",console);
            Log.i("disconnect",console+"");
        }

        @Override
        public void onError(String error) {
            String console = "Exception : "+error;
            consoleMsg("myKey",console);
            Log.i("exception",console+"");
        }

        @Override
        public void onInfo(String info) {
            String console = "Info : "+info;
            consoleMsg("myKey",console);
            Log.i("info",console+"");
        }
    }
}
