package com.senior.gizgiz.hydronet.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.Fragment.FeedFragment;
import com.senior.gizgiz.hydronet.Fragment.HomeCarouselFragment;
import com.senior.gizgiz.hydronet.Fragment.NotificationFragment;
import com.senior.gizgiz.hydronet.Fragment.UserFragment;
import com.senior.gizgiz.hydronet.HelperClass.BottomNavigationViewHelper;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

public class MainActivity extends MicrogearActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static String username;
    public static FirebaseUser currentUser;
    public static User user;

    private static String APPID = "HydroNet"; //APP_ID
    private static String KEY = "42QfFe0kb6hR8X6"; //KEY
    private static String SECRET = "a9z4FIDHURM7s4frn8iNldRYb"; //SECRET
    private static String ALIAS = "android";
    private String topic = "testApi";
    private static String FEEDID = "HydroFeed";
    private static String APIKEY = "CpFTkVSGXyxKV8EVwybGHziRmDMxlSHX";
    private MicrogearCallBack callback = new MicrogearCallBack();
    private RequestQueue queue;
    private View badge;

    private static final String ENDPOINT = "https://api.netpie.io/feed/"+FEEDID+"?apikey="+APIKEY+"&granularity=24hours&since=24hours";

    private HomeCarouselFragment homeSubmenuFragment;
    private UserFragment userSubmenuFragment;

    private boolean nowHomeCarousel = false;
    private boolean nowUserCarousel = false;

    private BottomNavigationView bottomNavigationView;
    private Bundle userBundle;
    private static Bundle sensorBundle;

    private ArrayList<GrowHistory> growHistories;
    private long historySize;

    @SuppressLint("HandlerLeak")
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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_nav);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            final String[] user = new String[1];
            RealTimeDBManager.getDatabase().child("users").orderByChild("email").equalTo(currentUser.getEmail()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                                Log.d("key", childSnapshot.getKey());
                        user[0] = childSnapshot.child("username").getValue(String.class);
//                        Log.d("onDataChange", "inside orderByChild('email')" + user[0]);
                        username = user[0];
                    }
                }
                @Override public void onCancelled(DatabaseError databaseError) {}
            });

//            NavigationManager.setAlarm(this,2,17,0);

//            queue = Volley.newRequestQueue(this);
//            fetchNetPieData(queue,microgear,callback);

            bottomNavigationView = findViewById(R.id.bottom_navigation);
            BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);

            BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
            View v = bottomNavigationMenuView.getChildAt(2);
            BottomNavigationItemView itemView = (BottomNavigationItemView) v;
            badge = LayoutInflater.from(this).inflate(R.layout.notification_badge, bottomNavigationMenuView, false);
            itemView.addView(badge);
            
            userBundle = new Bundle();
            userBundle.putString("USERNAME",username);
            userBundle.putString("EMAIL",currentUser.getEmail());

            HomeCarouselFragment homeCarouselFragment = new HomeCarouselFragment();
            if(sensorBundle != null) homeCarouselFragment.setArguments(sensorBundle);
            getFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, homeCarouselFragment).commit();

            findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplication(),AddPlantActivity.class);
                    intent.putExtra("TYPE",3);
                    startActivity(intent);
                }
            });
        } else {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(MainActivity.this,R.style.myDialog));
            View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog,null);
            ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message))
                    .setText("You are not logged in!\nPlease log in to the system to continue.");
            dialogBuilder.setView(dialogCustomLayout);
            final AlertDialog dialog = dialogBuilder.create();
            dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });
            dialogCustomLayout.findViewById(R.id.btn_negative).setClickable(false);
            dialog.show();
        }
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NegotiateActivity.ADD_NEGOTIATION :
                FeedFragment.addNegotiation(resultCode, data);
                Log.e("onActivityResult","ADD_NEGOTIATION");
                break;
            case NegotiateActivity.UPDATE_NEGOTIATION :
                FeedFragment.updateNegotiation(resultCode, data);
                Log.e("onActivityResult","UPDATE_NEGOTIATION");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (nowHomeCarousel && !homeSubmenuFragment.onBackPressed()) super.onBackPressed();
        else if (nowUserCarousel && !userSubmenuFragment.onBackPressed()) super.onBackPressed();
        FragmentManager fm = getFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (Fragment frag : fm.getFragments()) {
                if (frag.isVisible()) {
                    FragmentManager childFm = frag.getChildFragmentManager();
                    if (childFm.getBackStackEntryCount() > 0) {
                        childFm.popBackStack();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.home :
                nowHomeCarousel = true;
                nowUserCarousel = false;
                HomeCarouselFragment homeCarouselFragment = new HomeCarouselFragment();
                if(sensorBundle != null) homeCarouselFragment.setArguments(sensorBundle);
                selectedFragment = homeCarouselFragment;
                break;
            case R.id.feed :
                nowHomeCarousel = false;
                nowUserCarousel = false;
                selectedFragment = new FeedFragment();
                break;
            case R.id.notification :
                nowHomeCarousel = false;
                nowUserCarousel = false;
                selectedFragment = new NotificationFragment();
                break;
            case R.id.user :
                nowHomeCarousel = false;
                nowUserCarousel = true;
                userSubmenuFragment = new UserFragment();
                userSubmenuFragment.setArguments(userBundle);
                selectedFragment = userSubmenuFragment;
                break;
        }
//        getSupportFragmentManager().beginTransaction()
//                .addToBackStack("HOME")
//                .replace(R.id.container, selectedFragment,"HOME").commit();

        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, selectedFragment).commit();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        microgear.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity","onResume");
        microgear.bindServiceResume();
        getUnreadNotificationCount();
        handleNotifyWeekly();
    }

    protected void consoleMsg(String key, String console) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("myKey", console);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    protected class MicrogearCallBack implements MicrogearEventListener {
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
    public void fetchFromNetPie() { fetchSensorData(); }
    private void fetchSensorData() { }
    private void fetchNetPieData(RequestQueue queue, Microgear microgear, MicrogearCallBack callback) {
        microgear.connect(APPID,KEY,SECRET,ALIAS);
        microgear.setCallback(callback);
        Log.e("url",ENDPOINT);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ENDPOINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response is: ",response);
                        try {
                            JSONObject sensorDataJSON = new JSONObject(response);
                            JSONArray dataJSONArray = sensorDataJSON.getJSONArray("lastest_data");
                            Log.e("dataJSONArray",dataJSONArray.toString());
                            final SensorData sensorData = new SensorData();
                            Date writeFeedDate = new Date();
                            String timeStamp = "";
                            final float value[] = new float[3];
                            for(int i=0; i<dataJSONArray.length(); i++) {
                                JSONObject data = dataJSONArray.getJSONObject(i);
                                String values = data.getJSONArray("values").getString(0);
                                timeStamp = values.substring(1,values.indexOf(","));
                                value[i] = Float.valueOf(values.substring(values.indexOf(",")+1,values.length()-1));
                            }
                            sensorData.setTimestamp(timeStamp);
                            sensorData.setECLevel(value[0]);
                            sensorData.setWaterLevel(value[1]);
                            sensorData.setpHLevel(value[2]);
                            final String finalWriteFeedDate = timeStamp;
                            final String finalTimeStamp = timeStamp;
//                            for(float data : value) Log.e("values",data+"");
                            Log.e("sensorData",sensorData.getWaterLevel()+","+sensorData.getpHLevel()+","+sensorData.getECLevel());
                            Log.e("send to daily frag", ">>>");
                            sensorBundle = new Bundle();
                            sensorBundle.putFloat("WATER", sensorData.getWaterLevel());
                            sensorBundle.putFloat("PH", sensorData.getpHLevel());
                            sensorBundle.putFloat("EC", sensorData.getECLevel());
                            sensorBundle.putString("TIMESTAMP", finalTimeStamp);
                            sensorBundle.putBoolean("CONTINUE",false);
                            sensorBundle.putBoolean("TERMINATE",false);
                            RealTimeDBManager.getDatabase().child("sensorData/"+currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        int i = 0;
                                        boolean existed = false;
                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            SensorData sensorData = childSnapshot.getValue(SensorData.class);
                                            if (sensorData.getTimestamp()!=null && sensorData.getTimestamp().equals(finalWriteFeedDate))
                                                existed = true;
                                            i++;
                                            if (i == dataSnapshot.getChildrenCount()) {
                                                if (sensorData.getWaterLevel()!=0&&sensorData.getpHLevel()!=0
                                                        &&sensorData.getECLevel()!=0&&!existed) {
                                                    Log.e("sensorData","new data");
                                                    RealTimeDBManager.writeNewSensorData(finalWriteFeedDate,value[0],value[1],value[2]);
                                                }
                                            }
                                        }
                                    } else {
                                        // just first time - handle with feedback from farm ***
                                        Log.e("sensorData","not exist");
                                        RealTimeDBManager.writeNewSensorData(finalWriteFeedDate, sensorData.getWaterLevel(), sensorData.getpHLevel(), sensorData.getECLevel());
                                    }
                                }
                                @Override public void onCancelled(DatabaseError databaseError) { }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("fetch from netpie","error");
            }
        });
        queue.add(stringRequest);
    }
    private void getUnreadNotificationCount() {
        RealTimeDBManager.getDatabase().child("notifications/"+currentUser.getUid()).orderByChild("read").equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    ((CustomTextView)badge.findViewById(R.id.notification_count)).setText(String.valueOf(dataSnapshot.getChildrenCount()));
                    badge.findViewById(R.id.notification_count).setVisibility(View.VISIBLE);
                } else {
                    badge.findViewById(R.id.notification_count).setVisibility(View.GONE);
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void handleNotifyWeekly() {
        RealTimeDBManager.getDatabase().child("weeklyNotifications/"+currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    notifyWeekly();
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void notifyWeekly() {
        final View customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_weekly_notice,null);
        final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(false);
        popup.showAtLocation(findViewById(R.id.container), Gravity.CENTER, 0, 0);
        NavigationManager.dimBehind(popup);
        customView.findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                microgear.connect(APPID,KEY,SECRET,ALIAS);
//                microgear.setCallback(callback);
//                microgear.subscribe("user");
//                microgear.publish("user","terminate");
                HomeCarouselFragment homeCarouselFragment = new HomeCarouselFragment();
                homeCarouselFragment.setArguments(sensorBundle);
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, homeCarouselFragment).commit();
                RealTimeDBManager.getDatabase().child("weeklyNotifications/"+currentUser.getUid()).setValue(null);
                popup.dismiss();
            }
        });
        customView.findViewById(R.id.btn_terminate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                microgear.connect(APPID,KEY,SECRET,ALIAS);
//                microgear.setCallback(callback);
//                microgear.subscribe("user");
//                microgear.publish("user","terminate");
                sensorBundle = new Bundle();
                sensorBundle.putFloat("WATER",9999);
                sensorBundle.putFloat("PH",9999);
                sensorBundle.putFloat("EC",9999);
                sensorBundle.putString("TIMESTAMP",String.valueOf(new Date().getTime()));
                HomeCarouselFragment homeCarouselFragment = new HomeCarouselFragment();
                homeCarouselFragment.setArguments(sensorBundle);
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        new ContextThemeWrapper(MainActivity.this,R.style.myDialog));
                View dialogCustomLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_dialog,null);
                ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Terminate farm?\nWhat's the result?");
                dialogBuilder.setView(dialogCustomLayout);
                final AlertDialog dialog = dialogBuilder.create();
                CustomTextView positiveBTN = dialogCustomLayout.findViewById(R.id.btn_positive);
                CustomTextView negativeBTN = dialogCustomLayout.findViewById(R.id.btn_negative);
                positiveBTN.setText("ALL SUCCESS");
                negativeBTN.setText("FAIL");
                positiveBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        popup.dismiss();
                    }
                });
                negativeBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        popup.dismiss();
                        recordResult();
                    }
                });
                dialog.show();
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, homeCarouselFragment).commit();
                RealTimeDBManager.getDatabase().child("weeklyNotifications/"+currentUser.getUid()).setValue(null);
                popup.dismiss();
            }
        });
    }
    private void recordResult() {
        growHistories = new ArrayList<>();
        historySize = 0;
        RealTimeDBManager.getDatabase().child("userPlants/"+currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String upid = childSnapshot.getKey();
                        RealTimeDBManager.getDatabase().child("growHistories/"+upid).orderByChild("harvested").equalTo(false).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    historySize = dataSnapshot.getChildrenCount();
                                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                        growHistory.setId(childSnapshot.getKey());
                                        growHistories.add(growHistory);
                                        if(growHistories.size()==historySize) {
                                            Intent intent = new Intent(getBaseContext(),RecordResultActivity.class);
                                            intent.putParcelableArrayListExtra("HISTORIES",growHistories);
                                            startActivity(intent);
                                            Log.e("startActivity","RecordResultActivity");
                                        }
                                    }
                                }
                            }
                            @Override public void onCancelled(DatabaseError databaseError) { }
                        });
                    }
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
