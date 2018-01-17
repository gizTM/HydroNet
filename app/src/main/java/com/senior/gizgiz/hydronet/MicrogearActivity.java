package com.senior.gizgiz.hydronet;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

public class MicrogearActivity extends Activity {

    private Microgear microgear = new Microgear(this);
    private String APPID = "HydroNet"; //APP_ID
    private String KEY = "PDsYSPrLaeRx1y6"; //KEY
    private String SECRET = "Nx2FzLsbAHnwk22OtRr6seoG1"; //SECRET
    private String ALIAS = "android";

    private EditText appidET,keyET,secretET,aliasET,subET,dataET,readET;
    private ImageButton appidIB,keysecretIB;
    private ImageButton subIB,pubIB,readIB;
    protected TextView tvSerial;
    private ScrollView mSvText;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            tvSerial.append(string+"\n");
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microgear);
        final MicrogearCallBack callback = new MicrogearCallBack();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mSvText = (ScrollView) findViewById(R.id.svText);
        tvSerial = (TextView) findViewById(R.id.tvSerial);
        tvSerial.setMovementMethod(new ScrollingMovementMethod());

        appidET = (EditText) findViewById(R.id.editTextAPPID);
        appidET.setText(APPID);
        keyET = (EditText) findViewById(R.id.editTextKEY);
        keyET.setText(KEY);
        secretET = (EditText) findViewById(R.id.editTextSECRET);
        secretET.setText(SECRET);
        aliasET = (EditText) findViewById(R.id.editTextAlias);
        aliasET.setText(ALIAS);
        subET = (EditText) findViewById(R.id.editTextSub);
        dataET = (EditText) findViewById(R.id.editTextData);
//        readET = (EditText) findViewById(R.id.editTextRead);

        appidIB = (ImageButton) findViewById(R.id.imageButtonEditId);
        keysecretIB = (ImageButton) findViewById(R.id.imageButtonEditKey);
        subIB = (ImageButton) findViewById(R.id.imageButtonSub);
        pubIB = (ImageButton) findViewById(R.id.imageButtonPub);
//        readIB = (ImageButton) findViewById(R.id.imageButtonRead);

        subIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                microgear.connect(appidET.getText().toString()+"",
                        keyET.getText().toString()+"",
                        secretET.getText().toString()+"",
                        aliasET.getText().toString()+"");
                microgear.setCallback(callback);
                microgear.subscribe(subET.getText().toString()+"");
            }
        });

        pubIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                microgear.publish(subET.getText().toString()+"",dataET.getText().toString()+"");
            }
        });
//        readIB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                microgear.subscribe(subET.getText().toString()+"");
//            }
//        });

        /*
        (new Thread(new Runnable()
        {
            int count = 1;
            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run(){
                                microgear.publish("Topictest", String.valueOf(count)+".  Test message");
                                count++;
                            }
                        });
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        })).start();
        */

    }


    protected void onDestroy() {
        super.onDestroy();
        microgear.disconnect();
    }

    protected void onResume() {
        super.onResume();
        microgear.bindServiceResume();
    }

    class MicrogearCallBack implements MicrogearEventListener{
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
