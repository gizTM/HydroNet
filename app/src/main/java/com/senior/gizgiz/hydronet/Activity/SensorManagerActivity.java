package com.senior.gizgiz.hydronet.Activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidplot.xy.XYPlot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.senior.gizgiz.hydronet.HelperClass.Alarm;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netpie.microgear.MicrogearEventListener;
import vn.luongvo.widget.iosswitchview.SwitchView;

/**
 * Created by Admins on 008 08/02/2018.
 */

public class SensorManagerActivity extends MicrogearActivity {
    private SwitchView pumpToggle;
    private CustomTextView netPieLogPump,netPieLogWater,netPieLogConnection;

    private String messageData="",pumpStatus ="",waterLevel="";
    private boolean pumpOn = false;

    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 5d;
    private Date lastXValue = Calendar.getInstance().getTime();
    private LineGraphSeries<DataPoint> realtimeWaterSeries;

    private Map<Calendar,List<DataPoint>> waterLevelValue = new HashMap<>();

    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            String message = bundle.getString("message");
            if(message != null) {
                messageData = message.substring(message.indexOf(":")+2).trim();
                if(messageData.length()>5) {
                    if (messageData.substring(0, 3).equalsIgnoreCase("DPM") &&
                            (messageData.substring(4).equalsIgnoreCase("on") || messageData.substring(4).equalsIgnoreCase("off")))
                        pumpStatus = messageData.substring(4).toUpperCase();
                    else if (messageData.substring(0, 5).equalsIgnoreCase("Ping:") && StringUtils.isNumeric(messageData.substring(6)))
                        waterLevel = messageData.substring(6);
                }
//                ResourceManager.writeToFile(messageData,SensorManagerActivity.this,"farmLog"+ResourceManager.shortDateFormatForFileName.format(Calendar.getInstance().getTime()));
            }
            if(string != null) {
                NavigationManager.showToast(getApplicationContext(), string, 1000);
                netPieLogConnection.append("\n".concat(string));
            }
            netPieLogPump.setText("pump is now: ".concat(pumpStatus));
            netPieLogWater.setText("water level: ".concat(waterLevel));
            if(pumpOn && pumpStatus.equalsIgnoreCase("off")) {
                NavigationManager.showToast(getApplicationContext(),"starting pump",1000);
                microgear.chat("esp8266","on");
            }
            if(!pumpOn && pumpStatus.equalsIgnoreCase("on")) {
                NavigationManager.showToast(getApplicationContext(),"stopping pump",1000);
                microgear.chat("esp8266","off");
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_manager);
        pumpToggle = findViewById(R.id.switch_pump);
        netPieLogPump = findViewById(R.id.netpie_log_pump);
        netPieLogWater = findViewById(R.id.netpie_log_water);
        netPieLogConnection = findViewById(R.id.netpie_log_connection);

        microgear.connect(APPID+"",KEY+"",SECRET+"",ALIAS+"");
        microgear.setCallback(callback);
        pumpToggle.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {
                pumpOn = isChecked;
                microgear.chat("esp8266",pumpOn?"on":"off");
            }
        });

//        NavigationManager.setAlarm(this,4,18,0);

        GraphView realTimeWaterGraph = findViewById(R.id.realtime_water_graph);
        initGraph(realTimeWaterGraph);
    }

    public void initGraph(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(4);
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        graph.getGridLabelRenderer().setLabelVerticalWidth(100);

        realtimeWaterSeries = new LineGraphSeries<>();
        realtimeWaterSeries.setDrawDataPoints(true);
        realtimeWaterSeries.setDrawBackground(true);
        graph.addSeries(realtimeWaterSeries);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 0.25d;
                Calendar lastX = Calendar.getInstance();
                lastX.setTime(lastXValue);
                Calendar now = Calendar.getInstance();
                if(!waterLevel.equals("") && lastX.get(Calendar.YEAR)==now.get(Calendar.YEAR)&&lastX.get(Calendar.DAY_OF_YEAR)==now.get(Calendar.DAY_OF_YEAR)) {
                    final Calendar dateKey = Calendar.getInstance();
                    dateKey.set(Calendar.DAY_OF_YEAR,now.get(Calendar.DAY_OF_YEAR));
                    final Date key = dateKey.getTime();
                    if (waterLevelValue.get(dateKey) != null) waterLevelValue.get(dateKey).add(new DataPoint(key,Integer.valueOf(waterLevel)));
                    else waterLevelValue.put(dateKey,new ArrayList<DataPoint>(){{add(new DataPoint(key,Integer.valueOf(waterLevel)));}});
                }
                lastXValue = Calendar.getInstance().getTime();
                realtimeWaterSeries.appendData(new DataPoint(graphLastXValue, (waterLevel.equals(""))?0:Integer.valueOf(waterLevel)), true, 22);
//                mHandler.postDelayed(this, 300000);
                mHandler.postDelayed(this,1000);
            }
        };
        mHandler.postDelayed(mTimer, 1500);
        super.onResume();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer);
//        data.stopThread();
        super.onPause();
    }

    protected void consoleMsg(String key,String console) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString(key, console);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
