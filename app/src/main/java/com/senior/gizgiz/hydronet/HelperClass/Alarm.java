package com.senior.gizgiz.hydronet.HelperClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.senior.gizgiz.hydronet.Activity.FetchSensorActivity;

/**
 * Created by Admins on 022 22/03/2018.
 */

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        MediaPlayer mp = MediaPlayer.create(context, R.raw.ferry_sound);
//        mp.start();
        Log.e("receive alarm",">>>");
        context.startActivity(new Intent(context,FetchSensorActivity.class));
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
//        wl.acquire();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] s = { 0, 100, 10, 500, 10, 100, 0, 500, 10, 100, 10, 500 };
        vibrator.vibrate(s, -1);
    }
}
