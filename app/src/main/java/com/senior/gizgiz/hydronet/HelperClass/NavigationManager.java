package com.senior.gizgiz.hydronet.HelperClass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Activity.SensorManagerActivity;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admins on 017 17/1/2018.
 */

public class NavigationManager {
//    private static final int hour = 4, minute = 11, second = 0;

    public static boolean navigateTo (Context context, int item) {
        switch (item) {
            case R.id.home :
//                context.startActivity(new Intent(context,HomeActivity.class));
                Toast.makeText(context,"Home",Toast.LENGTH_SHORT).show();
                break;
//            case R.id.my_plant :
//                context.startActivity(new Intent(context,MyProfileActivity.class));
//                Toast.makeText(context,"My Plant",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.community :
//                Toast.makeText(context,"Community",Toast.LENGTH_SHORT).show();
//                break;
            case R.id.feed :
//                context.startActivity(new Intent(context,FeedActivity.class));
                Toast.makeText(context,"Feed",Toast.LENGTH_SHORT).show();
                break;
//            case R.id.trading :
//                context.startActivity(new Intent(context,TradingActivity.class));
//                Toast.makeText(context,"Trading",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.netpie_console :
//                context.startActivity(new Intent(context, SensorManagerActivity.class));
//                Toast.makeText(context,"NetPie Console",Toast.LENGTH_SHORT).show();
//                break;
            default: break;
        }
        return false;
    }
    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }
    public static String timeAgo(Date time) {
        String result = "";
        return result;
    }
    public static String calculateTimeDiff(String type,Date time1,Date time2) {
        String result = ""; long timeInMillis = 0L;
        Calendar calendar1 = Calendar.getInstance(),calendar2 = Calendar.getInstance();
        if(time1!=null) {
            calendar1.setTime(time1);
            calendar2.setTime(time2);
            Map<TimeUnit, Long> timeDiffMap = computeDiff(calendar2.getTime(), calendar1.getTime());
//            for(Map.Entry entry : timeDiffMap.entrySet()) Log.e(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            long day = timeDiffMap.get(TimeUnit.DAYS), hour = timeDiffMap.get(TimeUnit.HOURS),
                    minute = timeDiffMap.get(TimeUnit.MINUTES), second = timeDiffMap.get(TimeUnit.SECONDS);
            switch (type) {
                case "string" :
                    if (calendar1.getTime().after(calendar2.getTime())) {
                        result = "+";
                        if (day>0) result += day+"d";
                        else result += hour+"h:"+minute+"m:"+second+"s";
                    } else {
                        result = "-";
                        if (Math.abs(day)>0) result += Math.abs(day)+"d";
                        else result += Math.abs(hour)+"h:"+Math.abs(minute)+"m:"+Math.abs(second)+"s";
                    }
                    break;
                case "long" :
                    TimeUnit key[] = {TimeUnit.DAYS,TimeUnit.HOURS,TimeUnit.MINUTES,TimeUnit.SECONDS,TimeUnit.MILLISECONDS};
                    int unitConvert[] = {24,60,60,1000,1};
                    for(int i=0; i<5; i++) {
//                        Log.e(key[i]+"",timeDiffMap.get(key[i])+"");
                        long millis = timeDiffMap.get(key[i]);
                        for(int j=i; j<5; j++) millis *= unitConvert[j];
                        timeInMillis += millis;
//                        Log.e(key[i]+"",millis+"");
                    }
                    result = String.valueOf(timeInMillis);
                    break;
            }
        } else Log.e("null date",">>>");
//        Log.e("result",result);
        return result;
    }
    public static String calculateTimeDiff(String type,Date time) {
        String result = ""; long timeInMillis = 0L;
        Calendar calendar = Calendar.getInstance();
        Date now = Calendar.getInstance().getTime();
        if(time!=null) {
            calendar.setTime(time);
            Map<TimeUnit, Long> timeDiffMap = computeDiff(now, calendar.getTime());
//            for(Map.Entry entry : timeDiffMap.entrySet()) Log.e(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            long day = timeDiffMap.get(TimeUnit.DAYS), hour = timeDiffMap.get(TimeUnit.HOURS),
                    minute = timeDiffMap.get(TimeUnit.MINUTES), second = timeDiffMap.get(TimeUnit.SECONDS);
            switch (type) {
                case "string" :
                    if (calendar.getTime().after(now)) {
                        result = "+";
                        if (day>0) result += day+"d";
                        else result += hour+"h:"+minute+"m:"+second+"s";
                    } else {
                        result = "-";
                        if (Math.abs(day)>0) result += Math.abs(day)+"d";
                        else result += Math.abs(hour)+"h:"+Math.abs(minute)+"m:"+Math.abs(second)+"s";
                    }
                    break;
                case "long" :
                    TimeUnit key[] = {TimeUnit.DAYS,TimeUnit.HOURS,TimeUnit.MINUTES,TimeUnit.SECONDS,TimeUnit.MILLISECONDS};
                    int unitConvert[] = {24,60,60,1000,1};
                    for(int i=0; i<5; i++) {
//                        Log.e(key[i]+"",timeDiffMap.get(key[i])+"");
                        long millis = timeDiffMap.get(key[i]);
                        for(int j=i; j<5; j++) millis *= unitConvert[j];
                        timeInMillis += millis;
//                        Log.e(key[i]+"",millis+"");
                    }
                    result = String.valueOf(timeInMillis);
                    break;
            }
        } else Log.e("null date",">>>");
//        Log.e("result",result);
        return result;
    }
    public static void showToast(Context context, String msg, int duration) {
        final Toast toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        toast.show();
        (new CountDownTimer(duration, 500) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }
            public void onFinish() {
                toast.cancel();
            }

        }).start();
    }
    public static void handleSingleExpand(View customView, int toggleBTN, int expandLayout, boolean isExpanded) {
        if (isExpanded) {
            expand(customView.findViewById(expandLayout));
            customView.findViewById(toggleBTN).setBackgroundResource(R.drawable.ic_expand_less);
        } else {
            collapse(customView.findViewById(expandLayout));
            customView.findViewById(toggleBTN).setBackgroundResource(R.drawable.ic_expand_more);
        }
    }
    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);
    }
    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
    public static void setAlarm(Context context,int hour,int minute,int second) {
        Calendar cal = Calendar.getInstance();
        Intent activate = new Intent(context, Alarm.class);
        AlarmManager alarms;
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, activate, 0);
        alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
    }
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
