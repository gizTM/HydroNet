package com.senior.gizgiz.hydronet.Activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.R;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

public class MicrogearActivity extends Activity {
    protected Microgear microgear = new Microgear(this);
    protected MicrogearCallBack callback = new MicrogearCallBack();
    protected String APPID = "HydroNet"; //APP_ID
    protected String KEY = "tJXFyNtF1qNMSlN"; //KEY
    protected String SECRET = "Qf9u5SmyW3W61hQdf7MmFGtT6"; //SECRET
    protected String ALIAS = "";

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
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

    protected void consoleMsg(String console) {
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
            consoleMsg(console);
            Log.i("Connected",console+"");
        }

        @Override
        public void onMessage(String topic, String message) {
            consoleMsg(topic+" : "+message);
            Log.i("Message",topic+" : "+message);
        }

        @Override
        public void onPresent(String token) {
            String console = "New friend connect : "+token;
            consoleMsg(console);
            Log.i("present",console+"");
        }

        @Override
        public void onAbsent(String token) {
            String console = "Friend lost :"+token;
            consoleMsg(console);
            Log.i("absent",console+"");
        }

        @Override
        public void onDisconnect() {
            String console = "Disconnected";
            consoleMsg(console);
            Log.i("disconnect",console+"");
        }

        @Override
        public void onError(String error) {
            String console = "Exception : "+error;
            consoleMsg(console);
            Log.i("exception",console+"");
        }

        @Override
        public void onInfo(String info) {
            String console = "Info : "+info;
            consoleMsg(console);
            Log.i("info",console+"");
        }
    }
}
