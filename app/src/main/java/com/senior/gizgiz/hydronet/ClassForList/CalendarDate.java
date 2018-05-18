package com.senior.gizgiz.hydronet.ClassForList;

import com.senior.gizgiz.hydronet.Entity.SensorData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarDate {
    private Date date;
    private List<String> events;
    private SensorData sensorData;
    private boolean displayDot = false;

    public CalendarDate() { }
    public CalendarDate(Date date) { this.date = date; }
    public CalendarDate(Calendar calendar) { this.date = calendar.getTime(); }

    public Date getDate() {
        return date;
    }
    public List<String> getEvents() { return events; }
    public SensorData getSensorData() { return sensorData; }

    public void setDate(Date date) {
        this.date = date;
    }
    public void setEvents(List<String> event) { this.events = event; }
    public void setSensorData(SensorData sensorData) { this.sensorData = sensorData; }

    public void addEvents(String event) {
        if (this.events == null) this.events = new ArrayList<>();
        else this.events.add(event);
    }

    public boolean isDisplayDot() {
        return displayDot;
    }

    public void setDisplayDot(boolean displayDot) {
        this.displayDot = displayDot;
    }
}
