package com.senior.gizgiz.hydronet;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by Admins on 003 03/04/2018.
 */

public class HydroNetApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
