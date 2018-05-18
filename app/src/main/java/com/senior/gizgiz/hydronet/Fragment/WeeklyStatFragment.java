package com.senior.gizgiz.hydronet.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Adapter.RecyclerViewAdapter.GrowHistoryRecyclerViewAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Listener.RecyclerTouchListener;
import com.senior.gizgiz.hydronet.R;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeeklyStatFragment extends Fragment implements Refreshable {
    private List<CustomTextView> dateLabel = new ArrayList<>();
    private LinearLayout emptyState;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GraphView weeklyGraphView;
    private ImageView previousBTN,nextBTN;
    private CustomTextView avgWater,avgpH,avgEC,monthLabel;
    private GrowHistoryRecyclerViewAdapter growHistoryAdapter;
    private List<GrowHistory> plantHistories = new ArrayList<>();

    private int currentPage = 0;
    private RecyclerView grownPlantList;
    private boolean weeklyStatUpdated = false;
    private List<DataPoint[]> waterDataPointList = new ArrayList<>(),
                            pHDataPointList = new ArrayList<>(),
                            ecDataPointList = new ArrayList<>();
    private List<Long[]> timestampList = new ArrayList<>();
    private String[] days = new String[] { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly,viewGroup,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        emptyState = view.findViewById(R.id.empty_state_weekly);
        weeklyGraphView = view.findViewById(R.id.sensor_graph_view);
        previousBTN = view.findViewById(R.id.btn_previous);
        nextBTN = view.findViewById(R.id.btn_next);
        avgWater = view.findViewById(R.id.weekly_water_value);
        avgpH = view.findViewById(R.id.weekly_ph_value);
        avgEC = view.findViewById(R.id.weekly_ec_value);
        grownPlantList = view.findViewById(R.id.grown_plant_list);
        monthLabel = view.findViewById(R.id.label_month);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.CENTER;
                    }
                })
                .setRowBreaker(new IRowBreaker() {
                    @Override
                    public boolean isItemBreakRow(@IntRange(from = 0) int position) {
                        return position == 6 || position == 4 || position == 2;
                    }
                })
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .withLastRow(true)
                .build();
        grownPlantList.setLayoutManager(chipsLayoutManager);
        growHistoryAdapter = new GrowHistoryRecyclerViewAdapter(getContext(), plantHistories);
        grownPlantList.setAdapter(growHistoryAdapter);
        grownPlantList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), grownPlantList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        for (String aLabelKey : days) {
            dateLabel.add((CustomTextView) view.findViewById(ResourceManager.getID(getContext(), "label_".concat(aLabelKey.toLowerCase()))));
        }
        handleGraphViewBTN();
        fetchData();
        previousBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage>0) currentPage--;
                handleGraphViewBTN();
                fetchData();
            }
        });
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage<waterDataPointList.size()-1) currentPage++;
                handleGraphViewBTN();
                fetchData();
            }
        });
        view.findViewById(R.id.label_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestampList.get(0)[0]);
                YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(getContext(), new YearMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onYearMonthSet(int year, int month) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        handleMonthYearChange(month,year);
                    }
                }, R.style.myMonthYearDialog);
                yearMonthPickerDialog.show();
//                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
//                        new MonthPickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(int selectedMonth, int selectedYear) {
//                                // on date set
//                                handleMonthYearChange(selectedMonth,selectedYear);
//                            }
//                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
//
//                builder.setActivatedMonth(calendar.get(Calendar.MONTH))
//                       .setMinYear(calendar.get(Calendar.YEAR))
//                       .setActivatedYear(calendar.get(Calendar.YEAR))
//                       .setMaxYear(calendar.get(Calendar.YEAR)+10)
//                       .setTitle("Select time to view history")
//                        .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
//                            @Override
//                            public void onMonthChanged(int selectedMonth) {
//                                handleMonthYearChange(selectedMonth,0);
//                            }
//                        })
//                        .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
//                            @Override
//                            public void onYearChanged(int year) {
//                                handleMonthYearChange(0,year);
//                            }
//                        })
//                       .build().show();
            }
        });
    }

    private ValueEventListener sensorDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                int x=0;
                List<SensorData> points = new ArrayList<>();
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    x++;
                    SensorData sensorData = childSnapshot.getValue(SensorData.class);
//                        Log.e("sensorData",sensorData.getWaterLevel()+","+sensorData.getpHLevel()+","
//                                +sensorData.getECLevel()+","+sensorData.getTimestamp());
                    points.add(sensorData);
                    long prevTimestamp = 0;
                    if(x==dataSnapshot.getChildrenCount()) {
                        Log.e("sensorData count",x+"");
                        long count = dataSnapshot.getChildrenCount();
                        Long[] timestamps = new Long[7];
                        DataPoint[] waterDataPoints = new DataPoint[7];
                        DataPoint[] pHDataPoints = new DataPoint[7];
                        DataPoint[] ecDataPoints = new DataPoint[7];
                        int n = 0;
                        for (int i = 0; i < (int) count; i++) {
                            SensorData data = points.get(i);
                            String timestamp = data.getTimestamp();
                            Calendar date = Calendar.getInstance();
                            date.setTimeInMillis(Long.valueOf(timestamp));
                            if(i==0 && date.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
                                for(int a=0; a<date.get(Calendar.DAY_OF_WEEK); a++) {
                                    waterDataPoints[a] = new DataPoint(a,0);
                                    pHDataPoints[a] = new DataPoint(a,0);
                                    ecDataPoints[a] = new DataPoint(a,0);
                                    prevTimestamp = Long.valueOf(timestamp)-86400000;
                                    n=a;
                                }
                                timestamps[Math.max(n - 1, 0)] = Long.valueOf(timestamp) - 86400000;
                                for (int a = n - 2; a >= 0; a--)
                                    timestamps[a] = timestamps[a + 1] - 86400000;  //1day
                            }
                            timestamps[n] = Long.valueOf(timestamp);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(timestamps[n]);
                            Date t = new Date(Long.valueOf(timestamp));
                            Date p = new Date(prevTimestamp);
                            Long diff = Long.valueOf(NavigationManager.calculateTimeDiff("long",t,p));
//                            Log.e("ts",ResourceManager.noYearFormat.format(t));
//                            Log.e("pts",ResourceManager.noYearFormat.format(p));
//                            Log.e("diff",diff+"");
                            if(n!=7 && diff>86500000) {        //1day=86,400,000 2day=172,800,000
                                int q=n;
                                Long from = prevTimestamp;
                                Long to = Long.valueOf(timestamp);
                                while(new Date(from).before(new Date(to))) {
//                                    Log.e("============","============");
//                                    Log.e("from",""+from);
//                                    Log.e("to","\t"+to);
//                                    Log.e("============","============");
                                    timestamps[q] = from + 86400000;
                                    waterDataPoints[q] = new DataPoint(q,0);
                                    pHDataPoints[q] = new DataPoint(q,0);
                                    ecDataPoints[q] = new DataPoint(q,0);
                                    from += 86400000;
                                    q++;
                                    if(q%7==0) {
                                        timestampList.add(timestamps);
                                        waterDataPointList.add(waterDataPoints);
                                        pHDataPointList.add(pHDataPoints);
                                        ecDataPointList.add(ecDataPoints);
                                        timestamps = new Long[7];
                                        waterDataPoints = new DataPoint[7];
                                        pHDataPoints = new DataPoint[7];
                                        ecDataPoints = new DataPoint[7];
                                        q = 0;
                                    }
                                }
                                n=q-1;
//                                Log.e("nnnnn",n+"");
                            }
                            waterDataPoints[n] = new DataPoint(n, data.getWaterLevel());
                            pHDataPoints[n] = new DataPoint(n, data.getpHLevel());
                            ecDataPoints[n] = new DataPoint(n, data.getECLevel());
                            n++;
//                            printDebug(waterDataPoints,pHDataPoints,ecDataPoints,n);
                            if (i != 0 && n % 7 == 0) {
                                timestampList.add(timestamps);
                                waterDataPointList.add(waterDataPoints);
                                pHDataPointList.add(pHDataPoints);
                                ecDataPointList.add(ecDataPoints);
                                timestamps = new Long[7];
                                waterDataPoints = new DataPoint[7];
                                pHDataPoints = new DataPoint[7];
                                ecDataPoints = new DataPoint[7];
                                n = 0;
                                Log.i("list size", waterDataPointList.size() + "");
                            }
                            if (i == (int) count - 1 && n % 7 != 0) {
                                for (int s = n; s < 7; s++) {
                                    timestamps[s] = timestamps[s - 1] + 86400000;
                                    waterDataPoints[s] = new DataPoint(s, 0);
                                    pHDataPoints[s] = new DataPoint(s, 0);
                                    ecDataPoints[s] = new DataPoint(s, 0);
                                }
                                timestampList.add(timestamps);
                                waterDataPointList.add(waterDataPoints);
                                pHDataPointList.add(pHDataPoints);
                                ecDataPointList.add(ecDataPoints);
                                timestamps = new Long[7];
                                waterDataPoints = new DataPoint[7];
                                pHDataPoints = new DataPoint[7];
                                ecDataPoints = new DataPoint[7];
                                n = 0;
                                Log.i("list size", waterDataPointList.size() + "");
                            }
                            prevTimestamp = Long.valueOf(timestamp);
                        }
                        float water=0,ph=0,ec=0; int c=0;
                        for (int b=0; b<days.length; b++) {
                            dateLabel.get(b).setText(ResourceManager.noYearFormat.format(
                                    new Date(timestampList.get(currentPage)[b])));
                            if(waterDataPointList.get(currentPage)[b].getY()!=0) c++;
                            water += waterDataPointList.get(currentPage)[b].getY();
                            ph += pHDataPointList.get(currentPage)[b].getY();
                            ec += ecDataPointList.get(currentPage)[b].getY();
                        }
                        Calendar cal1 = Calendar.getInstance(),cal2 = Calendar.getInstance();
                        cal1.setTimeInMillis(timestampList.get(currentPage)[0]);
                        cal2.setTimeInMillis(timestampList.get(currentPage)[6]);
                        monthLabel.setText(new DateFormatSymbols().getMonths()[cal1.get(Calendar.MONTH)]);
                        if(cal1.get(Calendar.MONTH)!=cal2.get(Calendar.MONTH))
                            monthLabel.append("-".concat(new DateFormatSymbols().getMonths()[cal2.get(Calendar.MONTH)]));
                        monthLabel.append(" ".concat(String.valueOf(cal1.get(Calendar.YEAR))));
                        avgWater.setText(ResourceManager.twoDecimalPlaceFormat.format(water/c));
                        avgpH.setText(ResourceManager.twoDecimalPlaceFormat.format(ph/c));
                        avgEC.setText(ResourceManager.twoDecimalPlaceFormat.format(ec/c));
                        weeklyGraphView.removeAllSeries();
                        LineGraphSeries<DataPoint> waterSeries = new LineGraphSeries<>(waterDataPointList.get(currentPage));
                        waterSeries.setColor(ResourceManager.getColor(getContext(),R.color.blue_ribbon));
                        waterSeries.setDrawDataPoints(true);
                        waterSeries.setDataPointsRadius(10f);
                        weeklyGraphView.addSeries(waterSeries);
                        LineGraphSeries<DataPoint> pHSeries = new LineGraphSeries<>(pHDataPointList.get(currentPage));
                        pHSeries.setColor(ResourceManager.getColor(getContext(),R.color.light_green));
                        pHSeries.setDrawDataPoints(true);
                        pHSeries.setDataPointsRadius(10f);
                        weeklyGraphView.addSeries(pHSeries);
                        LineGraphSeries<DataPoint> ecSeries = new LineGraphSeries<>(ecDataPointList.get(currentPage));
                        ecSeries.setColor(ResourceManager.getColor(getContext(),R.color.btn_gg));
                        ecSeries.setDrawDataPoints(true);
                        ecSeries.setDataPointsRadius(10f);
                        weeklyGraphView.addSeries(ecSeries);
                        weeklyGraphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                            @Override
                            public String formatLabel(double value, boolean isValueX) {
                                if (isValueX) return days[(int)value];
                                else return super.formatLabel(value,isValueX);
                            }
                        });
                        weeklyGraphView.getGridLabelRenderer().setNumHorizontalLabels(7);
                        emptyState.setVisibility(View.GONE);
                    }
                }
            } else {
                emptyState.setVisibility(View.VISIBLE);
            }
        }
        @Override public void onCancelled(DatabaseError databaseError) { }
    };
    private ValueEventListener historyListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists() && timestampList.size()!=0) {
                plantHistories.clear();
                growHistoryAdapter.notifyDataSetChanged();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final String upid = childSnapshot.getKey();
                    final Date firstDateInGraph = new Date(timestampList.get(currentPage)[0]);
                    final Date lastDateInGraph = new Date(timestampList.get(currentPage)[6]);
                    RealTimeDBManager.getDatabase().child("growHistories/"+upid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.e("history","exist");
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    final String ghid = childSnapshot.getKey();
                                    final GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                    Date startDate = new Date(Long.valueOf(growHistory.getStartDate()));
                                    if(startDate.after(firstDateInGraph)) {
                                        if(startDate.before(lastDateInGraph)) {
                                            Log.e("history","added");
                                            plantHistories.add(growHistory);
                                            growHistoryAdapter.notifyDataSetChanged();
                                        }
                                    } else if(!growHistory.isHarvested()) {
                                        plantHistories.add(growHistory);
                                        growHistoryAdapter.notifyDataSetChanged();
                                    } else if(new Date(Long.valueOf(growHistory.getHarvestDate())).before(lastDateInGraph)) {
                                        plantHistories.add(growHistory);
                                        growHistoryAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else Log.e("growHistories", "not exist");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            } else Log.e("userPlants", "not exist");
        }
        @Override public void onCancelled(DatabaseError databaseError) { }
    };

    @Override
    public void fetchData() {
        timestampList.clear(); waterDataPointList.clear(); pHDataPointList.clear(); ecDataPointList.clear();
        final String uid = MainActivity.currentUser.getUid();
        RealTimeDBManager.getDatabase().child("sensorData/"+uid).addValueEventListener(sensorDataListener);
        RealTimeDBManager.getDatabase().child("userPlants/" + uid).addValueEventListener(historyListener);
    }
    private void printDebug(DataPoint[] waterDataPoints,DataPoint[] pHDataPoints,DataPoint[] ecDataPoints,int n) {
        String water = "", ph = "", ec = "";
        for (int j = 0; j < waterDataPoints.length; j++) {
            water += waterDataPoints[j] + "\t";
            ph += pHDataPoints[j] + "\t";
            ec += ecDataPoints[j] + "\t";
        }
        Log.i("n", n + "");
        Log.i("==================", "=====================");
        Log.i("water", water);
        Log.i("ph", ph);
        Log.i("ec", ec);
        Log.i("==================", "=====================");
    }
    private void handleGraphViewBTN() {
        Log.e("graph page count",waterDataPointList.size()+"");
        Log.e("page",currentPage+1+"");
        if(currentPage==0) {
            previousBTN.setClickable(false);
            previousBTN.setColorFilter(ResourceManager.getColor(getContext(),R.color.wild_sand_gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);

        } else {
            previousBTN.setClickable(true);
            previousBTN.setColorFilter(ResourceManager.getColor(getContext(),R.color.gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        if(currentPage==waterDataPointList.size()-1) {
            nextBTN.setClickable(false);
            nextBTN.setColorFilter(ResourceManager.getColor(getContext(),R.color.wild_sand_gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            nextBTN.setClickable(true);
            nextBTN.setColorFilter(ResourceManager.getColor(getContext(),R.color.gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }
    private void handleMonthYearChange(int selectedMonth, int selectedYear) {
        String selectedMonthName = new DateFormatSymbols().getMonths()[selectedMonth];
        int i=0; boolean outOfRange = true;
        for(Long[] timestamp : timestampList) {
            Calendar firstDate = Calendar.getInstance(), lastDate = Calendar.getInstance();
            firstDate.setTimeInMillis(timestamp[0]);
            lastDate.setTimeInMillis(timestamp[6]);
            if(selectedYear == firstDate.get(Calendar.YEAR)) {
                if(selectedMonth == firstDate.get(Calendar.MONTH) || selectedMonth == lastDate.get(Calendar.MONTH)) {
                    Log.e("selected date","in range");
                    currentPage = i;
                    handleGraphViewBTN();
                    fetchData();
                    outOfRange = false;
                    break;
                }
            } else outOfRange = true;
            i++;
        }
        if(outOfRange) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(getActivity(), R.style.myDialog));
            View dialogCustomLayout = LayoutInflater.from(getContext()).inflate(R.layout.confirm_dialog, null);
            ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("No sensor data to show for "
                    .concat(selectedMonthName).concat(String.valueOf(selectedYear)));
            dialogBuilder.setView(dialogCustomLayout);
            final AlertDialog dialog = dialogBuilder.create();
            ((CustomTextView)dialogCustomLayout.findViewById(R.id.btn_positive)).setText("GOT IT");
            dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialogCustomLayout.findViewById(R.id.btn_negative).setVisibility(View.GONE);
            dialog.show();
        }
    }

    @Override
    public void removeFirebaseListener() {

    }
}
