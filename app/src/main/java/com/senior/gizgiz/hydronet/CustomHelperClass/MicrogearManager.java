package com.senior.gizgiz.hydronet.CustomHelperClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.BulletSpan;
import android.util.Log;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Activity.MicrogearActivity;
import com.senior.gizgiz.hydronet.R;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

/**
 * Created by Admins on 018 18/1/2018.
 */

public class MicrogearManager extends Activity {
    private Context context = this;
    private Microgear microgear = new Microgear(this);
    private String appid = "HydroNet"; //APP_ID
    private String key = "VyAjCTBLdMkqDAx"; //KEY
    private String secret = "OZsrRwVddG7U2BDi4ksXUa7bS"; //SECRET
    private String alias = "Test Microgear";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            Toast.makeText(context, string+"",Toast.LENGTH_SHORT).show();
//                tvSerial.append(string+"\n");
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microgear);
        final MicrogearCallBack callback = new MicrogearCallBack();
        microgear = new Microgear(this);
        microgear.connect(appid, key, secret, alias);
        microgear.setCallback(callback);
        microgear.subscribe("TestAddTopic");
        microgear.publish("TestAddTopic", ResourceManager.getString(this,"msg_test_publish_microgear"));
    }
    protected void onDestroy() {
        super.onDestroy();
        microgear.disconnect();
    }

    protected void onResume() {
        super.onResume();
        microgear.bindServiceResume();
    }

    class MicrogearCallBack implements MicrogearEventListener {
        @Override
        public void onConnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Now I'm connected with netpie");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Connected","Now I'm connected with netpie");
        }

        @Override
        public void onMessage(String topic, String message) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", topic+" : "+message);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Message",topic+" : "+message);
        }

        @Override
        public void onPresent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "New friend Connect :"+token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("present","New friend Connect :"+token);
        }

        @Override
        public void onAbsent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Friend lost :"+token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("absent","Friend lost :"+token);
        }

        @Override
        public void onDisconnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Disconnected");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("disconnect","Disconnected");
        }

        @Override
        public void onError(String error) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : "+error);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("exception","Exception : "+error);
        }

        @Override
        public void onInfo(String info) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : "+info);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("info","Info : "+info);
        }
    }
}
