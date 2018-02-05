package com.senior.gizgiz.hydronet.Entity;

import java.util.Date;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class SensorData {
    private Date dateTime;
    private float waterLevel, pHLevel, ECLevel;

    public SensorData() {  }
    public SensorData(Date dateTime, float waterLevel, float pHLevel, float ECLevel) {
        this.dateTime = dateTime;
        this.waterLevel = waterLevel;
        this.pHLevel = pHLevel;
        this.ECLevel = ECLevel;
    }

    public void fetchSensorData() {

    }
}
