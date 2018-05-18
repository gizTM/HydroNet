package com.senior.gizgiz.hydronet.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Activity.MicrogearConsoleActivity;
import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.HomeDetailFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.util.Date;

/**
 * Created by Admins on 010 10/02/2018.
 */

public class DailyStatFragment extends Fragment implements OnBackPressListener,SwipeRefreshLayout.OnRefreshListener,Refreshable {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TableLayout dailyLayout;
    private TableRow waterGroup,pHGroup,ecGroup;
    private CustomTextView waterValue,pHValue,ecValue,timestamp;
    private LinearLayout emptyState;
    private ImageView waterBTN,pHBTN,ecBTN;

    private float water,pH,ec;
    private String timeStamp;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(getArguments() != null) {
            water = getArguments().getFloat("WATER");
            pH = getArguments().getFloat("PH");
            ec = getArguments().getFloat("EC");
            timeStamp = getArguments().getString("TIMESTAMP");
        }
        return inflater.inflate(R.layout.stat_today,viewGroup,false);
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        view.findViewById(R.id.btn_test_netpie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MicrogearConsoleActivity.class));
            }
        });
        dailyLayout = view.findViewById(R.id.daily_layout);
        emptyState = view.findViewById(R.id.empty_state_daily);
        waterGroup = view.findViewById(R.id.daily_water_group);
        pHGroup = view.findViewById(R.id.daily_ph_group);
        ecGroup = view.findViewById(R.id.daily_ec_group);
        waterValue = view.findViewById(R.id.daily_water_value);
        pHValue = view.findViewById(R.id.daily_ph_value);
        ecValue = view.findViewById(R.id.daily_ec_value);
        timestamp = view.findViewById(R.id.timestamp);
        waterBTN = view.findViewById(R.id.btn_info_water_daily);
        pHBTN = view.findViewById(R.id.btn_info_ph_daily);
        ecBTN = view.findViewById(R.id.btn_info_ec_daily);
        swipeRefreshLayout = view.findViewById(R.id.daily_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        handleRightActionBTN();
        Log.e("values",water+","+pH+","+ec+","+timeStamp);
    }

    private void fetchDailyData() {
        swipeRefreshLayout.setRefreshing(true);
        ((MainActivity)getActivity()).fetchFromNetPie();
        swipeRefreshLayout.setRefreshing(false);
        String uid = MainActivity.currentUser.getUid();
        if(water==9999 && pH==9999 && ec==9999) {
            dailyLayout.setVisibility(View.GONE);
            timestamp.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
        RealTimeDBManager.getDatabase().child("sensorData/"+uid).orderByChild("timestamp").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && water!=9999 && pH!=9999 && ec!=9999) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        SensorData sensorData = childSnapshot.getValue(SensorData.class);
                        water = sensorData.getWaterLevel();
                        pH = sensorData.getpHLevel();
                        ec = sensorData.getECLevel();
                        timeStamp = sensorData.getTimestamp();
                        dailyLayout.setVisibility(View.VISIBLE);
                        timestamp.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                        Animation revealAnimation = new TranslateAnimation(-500, 0, 0, 0);
                        revealAnimation.setDuration(1000);
                        waterGroup.startAnimation(revealAnimation);
                        pHGroup.startAnimation(revealAnimation);
                        ecGroup.startAnimation(revealAnimation);
                        waterValue.setText(String.valueOf(water).concat(" cm"));
                        pHValue.setText(String.valueOf(pH));
                        ecValue.setText(String.valueOf(ec).concat(" mS/cm"));
                        timestamp.setText("Retrieved at ".concat(
                                ResourceManager.shortDateTimeFormat.format(new Date(Long.parseLong(timeStamp)))));
                        handleSensorWarning();
                    }
                } else {
                    dailyLayout.setVisibility(View.GONE);
                    timestamp.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void handleRightActionBTN() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        final View customView = LayoutInflater.from(getContext()).inflate(R.layout.popup_info_sensor,null);
        final PopupWindow popup = new PopupWindow(customView,width-2*ResourceManager.getDim(getContext(),R.dimen.padding_medium),
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        final View.OnClickListener infoPopupDismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        };
        waterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setOutsideTouchable(true);
                popup.showAtLocation(customView, Gravity.CENTER, 0, 0);
                NavigationManager.dimBehind(popup);
                String spannableString = ResourceManager.getSeparateString(ResourceManager.getString(getContext(),"water_level_description"));
                SpannableStringBuilder bulletBuilder = new SpannableStringBuilder(spannableString);
                ResourceManager.showBullet(bulletBuilder,spannableString);
                ((CustomTextView)customView.findViewById(R.id.sensor_description)).setText(bulletBuilder);
                ((CustomTextView)customView.findViewById(R.id.label_popup_title)).setText(ResourceManager.getString(getContext(),"label_sensor_water"));
                ((ImageView)customView.findViewById(R.id.ic_sensor)).setImageResource(ResourceManager.getDrawableID(getContext(),"ic_sensor_water"));
                ((CustomTextView)customView.findViewById(R.id.ideal_sensor_value)).setText("10cm");
                customView.findViewById(R.id.btn_got_it).setOnClickListener(infoPopupDismissListener);
            }
        });
        pHBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setOutsideTouchable(true);
                popup.showAtLocation(customView, Gravity.CENTER, 0, 0);
                NavigationManager.dimBehind(popup);
                String spannableString = ResourceManager.getSeparateString(ResourceManager.getString(getContext(),"ph_level_description"));
                SpannableStringBuilder bulletBuilder = new SpannableStringBuilder(spannableString);
                ResourceManager.showBullet(bulletBuilder,spannableString);
                ((CustomTextView)customView.findViewById(R.id.sensor_description)).setText(bulletBuilder);
                ((CustomTextView)customView.findViewById(R.id.label_popup_title)).setText(ResourceManager.getString(getContext(),"label_sensor_ph"));
                ((ImageView)customView.findViewById(R.id.ic_sensor)).setImageResource(ResourceManager.getDrawableID(getContext(),"ic_sensor_ph_color"));
                ((CustomTextView)customView.findViewById(R.id.ideal_sensor_value)).setText("5 ~ 6");
                customView.findViewById(R.id.btn_got_it).setOnClickListener(infoPopupDismissListener);
            }
        });
        ecBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setOutsideTouchable(true);
                popup.showAtLocation(customView, Gravity.CENTER, 0, 0);
                NavigationManager.dimBehind(popup);
                String spannableString = ResourceManager.getSeparateString(ResourceManager.getString(getContext(),"ec_level_description"));
                SpannableStringBuilder bulletBuilder = new SpannableStringBuilder(spannableString);
                ResourceManager.showBullet(bulletBuilder,spannableString);
                ((CustomTextView)customView.findViewById(R.id.sensor_description)).setText(bulletBuilder);
                ((CustomTextView)customView.findViewById(R.id.label_popup_title)).setText(ResourceManager.getString(getContext(),"label_sensor_ec"));
                ((ImageView)customView.findViewById(R.id.ic_sensor)).setImageResource(ResourceManager.getDrawableID(getContext(),"ic_sensor_ec"));
                ((CustomTextView)customView.findViewById(R.id.ideal_sensor_value)).setText("2 ~ 3");
                customView.findViewById(R.id.btn_got_it).setOnClickListener(infoPopupDismissListener);
            }
        });
    }
    private void handleSensorWarning() {
        if(water<12) waterBTN.setImageResource(ResourceManager.getDrawableID(getContext(),"ic_danger"));
        if(pH<5 || pH>7) pHBTN.setImageResource(ResourceManager.getDrawableID(getContext(),"ic_danger"));
        if(ec<1 || ec>4) ecBTN.setImageResource(ResourceManager.getDrawableID(getContext(),"ic_danger"));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("OnPause","home daily");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("OnResume","home daily");
        fetchDailyData();
    }

    @Override public void onRefresh() {
        fetchDailyData();
    }

    @Override public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }

    @Override public void fetchData() {
        fetchDailyData();
    }

    @Override public void removeFirebaseListener() {

    }
}
