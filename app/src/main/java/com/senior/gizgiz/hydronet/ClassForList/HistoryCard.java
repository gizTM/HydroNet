package com.senior.gizgiz.hydronet.ClassForList;

import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admins on 018 18/03/2018.
 */

public class HistoryCard {
    private String title;
    private List<GrowHistory> histories;
    private SensorData sensorData;

    public HistoryCard(String title, List<GrowHistory> histories) {
        this.title = title;
        this.histories = histories;
//        this.sensorData = histories.get(0).getSensorDataList().get(0);
    }

    public String getTitle() { return title; }
    public List<GrowHistory> getHistories() { return histories; }
    public SensorData getSensorData() { return sensorData; }

    public void setTitle(String title) { this.title = title; }
    public void setHistories(List<GrowHistory> histories) { this.histories = histories; }
    public void setSensorData(SensorData sensorData) { this.sensorData = sensorData; }
}