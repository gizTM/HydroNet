package com.senior.gizgiz.hydronet.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

import io.netpie.microgear.MicrogearEventListener;

public class MicrogearConsoleActivity extends MicrogearActivity {
//    private String APPID = "HydroNet"; //APP_ID
//    private String KEY = "42QfFe0kb6hR8X6"; //KEY
//    private String SECRET = "a9z4FIDHURM7s4frn8iNldRYb"; //SECRET
    private String APPID = "dht22project";
    private String KEY = "EM9SneXxbpBaoWK";
    private String SECRET = "bMDtXwIWtOA914XhSVtqWdfcX";
    private String ALIAS = "android";
    private MicrogearCallBack callback = new MicrogearCallBack();
    private boolean auto=false;

    private EditText appidET,keyET,secretET,aliasET, topicET,dataET, durationET;
    private Button connectBTN, publishBTN, autoBTN;
    protected TextView response;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("other");
            if(string != null) response.append("\n"+string);
            String message = bundle.getString("msg");
            String waterLevel="",pHLevel="",ecLevel="";

            SensorData sensorData = new SensorData();
            if(message != null) {
                response.append("\n"+message);
//                sensorData.setDateTime(Calendar.getInstance().getTime());
//                sensorData.setWaterLevel(Float.valueOf(message.substring(message.indexOf(":")+1).trim()));
//                RealTimeDBManager.getDatabase().child("sensorData").child(MainActivity.currentUser.getUid()).push().setValue(sensorData);
//                response.append("\n\nsaved to database\ngo back to check @ home page");
            }

            final int scrollAmount = response.getLayout().getLineTop(response.getLineCount())-response.getHeight();
            if(scrollAmount>0) response.scrollTo(0,scrollAmount);
            else response.scrollTo(0,0);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console_microgear);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        response = findViewById(R.id.response);
        response.setMovementMethod(new ScrollingMovementMethod());

        appidET = findViewById(R.id.appid);
        appidET.setText(APPID);
        keyET = findViewById(R.id.key);
        keyET.setText(KEY);
        secretET = findViewById(R.id.secret);
        secretET.setText(SECRET);
        aliasET =  findViewById(R.id.alias);
        aliasET.setText(ALIAS);
        topicET =  findViewById(R.id.topic);
        dataET = findViewById(R.id.data);
        durationET = findViewById(R.id.duration);

        connectBTN = findViewById(R.id.connect);
        publishBTN = findViewById(R.id.publish);
        autoBTN = findViewById(R.id.auto);

        connectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                microgear.connect(appidET.getText().toString()+"",
                        keyET.getText().toString()+"",
                        secretET.getText().toString()+"",
                        aliasET.getText().toString()+"");
                microgear.setCallback(callback);
                microgear.subscribe(topicET.getText().toString()+"");
            }
        });

        publishBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                microgear.publish(topicET.getText().toString()+"",dataET.getText().toString()+"");
                microgear.chat("esp8266",dataET.getText().toString());
            }
        });
        autoBTN.setOnClickListener(new View.OnClickListener() {
            DecimalFormat decimalFormat = new DecimalFormat("#");
            long duration;
            @Override
            public void onClick(View view) {
                auto = !auto;
                autoBTN.setText(auto?"stop":"auto");
                try {
                    duration = decimalFormat.parse(durationET.getText().toString()).longValue();
                } catch (ParseException e) {
                    String error = durationET.getText().toString()+"is not in long format.";
                    consoleMsg(error,"other");
                    Log.i("Connected",error+"");
                }
                (new Thread(new Runnable() {
                    int count = 1;
                    @Override
                    public void run() {
                        while (!Thread.interrupted() && auto)
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        microgear.publish(topicET.getText().toString(), (count == 1) ? "on" : "off");
                                        count = (count == 1) ? 0 : 1;
                                    }
                                });
                                Thread.sleep(duration);
                            } catch (InterruptedException e) {
                                String error = "Thread stopped";
                                consoleMsg(error,"other");
                                Log.i("Connected",error+"");
                            }
                    }
                })).start();
            }
        });

    }


    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void consoleMsg(String console,String key) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString(key, console);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    class MicrogearCallBack implements MicrogearEventListener{
        @Override
        public void onConnect() {
            String console = "Now I'm connected with netpie";
            consoleMsg(console,"other");
            Log.i("Connected",console+"");
        }

        @Override
        public void onMessage(String topic, String message) {
            consoleMsg(topic+" : "+message,"msg");
            Log.i("Message",topic+" : "+message);
        }

        @Override
        public void onPresent(String token) {
            String console = "New friend connect : "+token;
            consoleMsg(console,"other");
            Log.i("present",console+"");
        }

        @Override
        public void onAbsent(String token) {
            String console = "Friend lost :"+token;
            consoleMsg(console,"other");
            Log.i("absent",console+"");
        }

        @Override
        public void onDisconnect() {
            String console = "Disconnected";
            consoleMsg(console,"other");
            Log.i("disconnect",console+"");
        }

        @Override
        public void onError(String error) {
            String console = "Exception : "+error;
            consoleMsg(console,"other");
            Log.i("exception",console+"");
        }

        @Override
        public void onInfo(String info) {
            String console = "Info : "+info;
            consoleMsg(console,"other");
            Log.i("info",console+"");
        }
    }
}
