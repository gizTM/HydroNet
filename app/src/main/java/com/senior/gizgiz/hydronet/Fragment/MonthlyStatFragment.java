package com.senior.gizgiz.hydronet.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.CalendarAdapter;
import com.senior.gizgiz.hydronet.ClassForList.CalendarDate;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.Y;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE;

public class MonthlyStatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,Refreshable {
    private Map<String,GrowHistory> weeklyStatMap = new HashMap<>();
    private LinearLayout emptyState;
    private ImageView previousBTN,nextBTN;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean weeklyStatUpdated = false;
    private GridView calendarGridView;
    private static CustomTextView monthLabel,sensorLabel;
    private List<CustomTextView> dateLabel = new ArrayList<>();
    private static LinearLayout eventLayout;
//    private MaterialCalendarView calendarView;

    private List<CalendarDate> selectedDates = new ArrayList<>();
    private static List<CalendarDate> calendarDates = new ArrayList<>();
    private static CalendarAdapter calendarAdapter;
    private static int currentMonth = Calendar.getInstance().get(Calendar.MONTH), currentYear = 2018;
    private String[] days = new String[] { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
    private String[] labelKey = new String[] { "sun","mon","tue","wed","thu","fri","sat" };

    public MonthlyStatFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stat_monthly, viewGroup, false);
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyState = view.findViewById(R.id.empty_state_weekly);
        eventLayout = view.findViewById(R.id.event_layout);
        sensorLabel = view.findViewById(R.id.label_sensor);
        swipeRefreshLayout = view.findViewById(R.id.weekly_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        calendarGridView = view.findViewById(R.id.calendar_gridview);
        monthLabel = view.findViewById(R.id.label_month);
        previousBTN = view.findViewById(R.id.btn_previous);
        nextBTN = view.findViewById(R.id.btn_next);
        monthLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupEvent(getContext(),null);
            }
        });
        previousBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarAdapter.resetAdapter();
                if(currentMonth==1) generateCalendar(12,--currentYear);
                else generateCalendar(--currentMonth,currentYear);
                fetchData();
            }
        });
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarAdapter.resetAdapter();
                if(currentMonth==12) generateCalendar(1,++currentYear);
                else generateCalendar(++currentMonth,currentYear);
                fetchData();
            }
        });
        calendarAdapter = new CalendarAdapter(getContext(),calendarDates);
        calendarGridView.setAdapter(calendarAdapter);
        generateCalendar(currentMonth,2018);
        for (int b=0; b<labelKey.length; b++) {
            ((CustomTextView) view.findViewById(ResourceManager.getID(getContext(), "label_".concat(labelKey[b])))).setText(days[b]);
        }
    }

    public void onResume() {
        super.onResume();
    }

    @Override public void fetchData() {
        Log.e("fetch weekly stat",">>>");
        if(swipeRefreshLayout!=null) swipeRefreshLayout.setRefreshing(true);
        eventLayout.removeAllViews();
        final String uid = MainActivity.currentUser.getUid();
        RealTimeDBManager.getDatabase().child("userPlants/" + uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        final String upid = childSnapshot.getKey();
                        final UserPlant userPlant = childSnapshot.getValue(UserPlant.class);
                        RealTimeDBManager.getDatabase().child("growHistories/" + upid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        final String ghid = childSnapshot.getKey();
                                        final GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(Long.valueOf(growHistory.getStartDate()));
                                        calendarAdapter.setSelectedDate(true,calendar.getTime(),String.valueOf(growHistory.getCount())
                                                .concat(" ").concat(growHistory.getPlantName())
                                                .concat(growHistory.getCount()>1?"s are planted":" is planted"));
                                        calendarAdapter.notifyDataSetChanged();
                                        if(!growHistory.isHarvested()) {
//                                            calendar.setTimeInMillis(Long.valueOf(growHistory.getExpectedHarvestDate()));
                                            Date expected = new Date(Long.valueOf(growHistory.getStartDate())+(userPlant.getGrowthDuration()*86400000L));
                                            calendar.setTime(expected);
                                            Log.e("start date",growHistory.getStartDate()+","+ResourceManager.shortDateFormat.format(new Date(Long.valueOf(growHistory.getStartDate()))));
                                            Log.e("expected date",(userPlant.getGrowthDuration()*86400000L)+","+userPlant.getGrowthDuration());
                                            Log.e("addition",Long.valueOf(growHistory.getStartDate())+(userPlant.getGrowthDuration()*86400000L)+"");
                                            calendarAdapter.setSelectedDate(true,calendar.getTime(),String.valueOf(growHistory.getCount())
                                                    .concat(" ").concat(growHistory.getPlantName())
                                                    .concat(growHistory.getCount()>1?"s":"").concat(" should be harvested"));
                                            calendarAdapter.notifyDataSetChanged();
                                        }
                                        else {
                                            calendar.setTime(new Date(Long.valueOf(growHistory.getHarvestDate())));
                                            calendarAdapter.setSelectedDate(true,calendar.getTime(),String.valueOf(growHistory.getCount())
                                                    .concat(" ").concat(growHistory.getPlantName())
                                                    .concat(growHistory.getCount()>1?"s are harvested":" is harvested"));
                                            calendarAdapter.notifyDataSetChanged();
                                        }
                                        for(Map.Entry sensorKey : growHistory.getSensorData().entrySet()) {
                                            RealTimeDBManager.getDatabase().child("sensorData/"+uid).orderByKey().equalTo(String.valueOf(sensorKey.getKey())).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                            SensorData sensorData = childSnapshot.getValue(SensorData.class);
                                                            Calendar calendar = Calendar.getInstance();
                                                            calendar.setTime(new Date(Long.valueOf(sensorData.getTimestamp())));
                                                            for (CalendarDate calendarDate : calendarDates) {
                                                                Calendar cal = Calendar.getInstance();
                                                                cal.setTime(calendarDate.getDate());
                                                                if (cal.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                                                                    calendarAdapter.setSelectedDate(false,calendarDate.getDate(), "Planting ".concat(growHistory.getCount() + "")
                                                                            .concat(" ").concat(growHistory.getPlantName() + "s"));
                                                                    calendarDate.setSensorData(sensorData);
                                                                    calendarAdapter.notifyDataSetChanged();
                                                                    setupEvent(getContext(), calendarDate.getDate());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    }
                                } else Log.e("growHistories", "not exist");
                            }
                            @Override public void onCancelled(DatabaseError databaseError) { }
                        });
                    }
                } else Log.e("userPlants", "not exist");
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }
    public static void setupEvent(Context context,Date date) {
        float water=0,ph=0,ec=0;
        int count=0;
        for(CalendarDate calendarDate : calendarDates) {
            if(date==null) eventLayout.removeAllViews();
            if(calendarDate.getDate().equals(date) && calendarDate.getEvents()!=null) {
                eventLayout.removeAllViews();
                for (String ev : calendarDate.getEvents()) {
                    CustomTextView tv = new CustomTextView(context);
                    tv.setText(ev);
                    eventLayout.addView(tv);
                }
            }
            if (calendarDate.getSensorData() != null) {
                count++;
                water+=calendarDate.getSensorData().getWaterLevel();
                ph+=calendarDate.getSensorData().getpHLevel();
                ec+=calendarDate.getSensorData().getECLevel();
            }
        }
        water/=count; ph/=count; ec/=count;
        if(count>0) sensorLabel.setText("\t\t\t\t\t\t\t\tAvg value this month - water:".concat(ResourceManager.twoDecimalPlaceFormat.format(water)
                .concat(" cm, \t\tpH:").concat(ResourceManager.twoDecimalPlaceFormat.format(ph).concat(", \t\tEC:")
                        .concat(ResourceManager.twoDecimalPlaceFormat.format(ec)).concat(" mS/cm"))));
        else sensorLabel.setText("");
        Paint textPaint = sensorLabel.getPaint();
        String text = sensorLabel.getText().toString();//get text
        int width = Math.round(textPaint.measureText(text));//measure the text size
        ViewGroup.LayoutParams params =  sensorLabel.getLayoutParams();
        params.width = width;
        sensorLabel.setLayoutParams(params); //refine
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getRealMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;

        //this is optional. do not scroll if text is shorter than screen width
        //remove this won't effect the scroll
        if (width <= screenWidth) {
            //All text can fit in screen.
            return;
        }
        //set the animation
        TranslateAnimation slide = new TranslateAnimation(0, -width, 0, 0);
        slide.setDuration(20000);
        slide.setRepeatCount(Animation.INFINITE);
        slide.setRepeatMode(Animation.RESTART);
        slide.setInterpolator(new LinearInterpolator());
        sensorLabel.startAnimation(slide);
    }
    private void generateCalendar(int month,int year) {
        // months[i] = name of month i
        String[] months = {
                "",                               // leave empty so that months[1] = "January"
                "January", "February", "March",
                "April", "May", "June",
                "July", "August", "September",
                "October", "November", "December"
        };
        // days[i] = number of days in month i
        int[] days = {
                0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        };
        // check for leap year
        if (month == 2 && isLeapYear(year)) days[month] = 29;
        // print calendar header
//        StdOut.println("   " + months[month] + " " + year);
//        StdOut.println(" S  M Tu  W Th  F  S");
        monthLabel.setText(new DateFormatSymbols().getMonths()[month-1].concat(" ").concat(String.valueOf(year)));
        // starting day
        int d = day(month, 1, year);
        calendarDates.clear();
        calendarAdapter.notifyDataSetChanged();
        // print the calendar
        for (int i = 0; i < d; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(-0,0,0);
            calendarDates.add(new CalendarDate(calendar));
            calendarAdapter.notifyDataSetChanged();
        }
        for (int i = 1; i <= days[month]; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month-1,i);
            calendarDates.add(new CalendarDate(calendar));
            calendarAdapter.notifyDataSetChanged();
//            StdOut.printf("%2d ", i);
            if (((i + d) % 7 == 0) || (i == days[month])) {
//                StdOut.println();
            }
        }
        while(calendarDates.size()%7!=0) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(-0,0,0);
            calendarDates.add(new CalendarDate(calendar));
            calendarAdapter.notifyDataSetChanged();
        }
        fetchData();
        setupEvent(getContext(),null);
    }
    private int day(int month, int day, int year) {
        int y = year - (14 - month) / 12;
        int x = y + y/4 - y/100 + y/400;
        int m = month + 12 * ((14 - month) / 12) - 2;
        int d = (day + x + (31*m)/12) % 7;
        return d;
    }
    private boolean isLeapYear(int year) {
        if  ((year % 4 == 0) && (year % 100 != 0)) return true;
        if  (year % 400 == 0) return true;
        return false;
    }
    private void handleGraphViewBTN() {
//        if(currentPage==0) {
//            previousBTN.setClickable(false);
//            previousBTN.setColorFilter(ResourceManager.getColor(getContext(),R.color.wild_sand_gray),
//                    android.graphics.PorterDuff.Mode.MULTIPLY);
//
//        } else {
//            previousBTN.setClickable(true);
//            previousBTN.setColorFilter(ResourceManager.getColor(getContext(),R.color.gray),
//                    android.graphics.PorterDuff.Mode.MULTIPLY);
//        }
//        if(currentPage==waterDataPointList.size()-1) {
//            nextBTN.setClickable(false);
//            nextBTN.setColorFilter(ResourceManager.getColor(getContext(),R.color.wild_sand_gray),
//                    android.graphics.PorterDuff.Mode.MULTIPLY);
//        } else {
//            nextBTN.setClickable(true);
//            nextBTN.setColorFilter(ResourceManager.getColor(getContext(),R.color.gray),
//                    android.graphics.PorterDuff.Mode.MULTIPLY);
//        }
    }

    @Override public void removeFirebaseListener() {

    }

    @Override public void onRefresh() {
        fetchData();
    }
}
