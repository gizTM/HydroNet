package com.senior.gizgiz.hydronet.Activity;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;
import java.text.ParseException;

import io.netpie.microgear.MicrogearEventListener;

public class MicrogearConsoleActivity extends MicrogearActivity {
    private String APPID = super.APPID; //APP_ID
    private String KEY = super.KEY; //KEY
    private String SECRET = super.SECRET; //SECRET
    private String ALIAS = super.ALIAS;
    private boolean auto=false;
    private MicrogearCallBack callBack = new MicrogearCallBack();

    private EditText appidET,keyET,secretET,aliasET, topicET,dataET, durationET;
    private Button connectBTN, publishBTN, autoBTN;
    protected TextView response;
    private ScrollView scrollResponse;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            response.append("\n"+string);
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
                    consoleMsg(error);
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
                                consoleMsg(error);
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

    protected void consoleMsg(String console) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("myKey", console);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    class MicrogearCallBack implements MicrogearEventListener{
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
