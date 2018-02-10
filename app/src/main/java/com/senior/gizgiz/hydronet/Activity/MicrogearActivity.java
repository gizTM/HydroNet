package com.senior.gizgiz.hydronet.Activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;

import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

public class MicrogearActivity extends Activity {
    protected Microgear microgear = new Microgear(this);
    protected MicrogearCallBack callback = new MicrogearCallBack();
    protected String APPID = "HydroNet"; //APP_ID
    protected String KEY = "nfd68H1XcIGwoo4";   //KEY
    protected String SECRET = "lZWJRzMSIrfOuiDt4C68VKjlb";  //SECRET
    protected String ALIAS = "android";

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            String message = bundle.getString("message");
            if(string == null) NavigationManager.showToast(getApplicationContext(),message,1000);
            else NavigationManager.showToast(getApplicationContext(),string,1000);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void onDestroy() {
        super.onDestroy();
        microgear.disconnect();
    }

    protected void onResume() {
        super.onResume();
        microgear.bindServiceResume();
    }

    protected void consoleMsg(String key, String console) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("myKey", console);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    protected class MicrogearCallBack implements MicrogearEventListener{
        @Override
        public void onConnect() {
            String console = "Now I'm connected with netpie";
            consoleMsg("myKey",console);
            Log.i("Connected",console+"");
        }

        @Override
        public void onMessage(String topic, String message) {
            consoleMsg("message",topic+" : "+message);
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
