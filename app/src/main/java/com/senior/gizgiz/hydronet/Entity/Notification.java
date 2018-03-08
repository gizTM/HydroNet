package com.senior.gizgiz.hydronet.Entity;

import java.util.Date;

/**
 * Created by Admins on 002 02/03/2018.
 */

public class Notification {
    private String id,message;
    private Date timeStamp;
    private int type;
    private Item item;
    private Plant plant;
    private Story story;
    private SensorData sensorData;

    public Notification() {}
    public Notification(String id, int type, Date timeStamp) {
        this.id = id;
        this.type = type;
        this.timeStamp = timeStamp;
    }
    public Notification(String id, int type, Date timeStamp, Item item) {
        this.id = id;
        this.type = type;
        this.timeStamp = timeStamp;
        this.item = item;
    }
    public Notification(String id, int type, Date timeStamp, Plant plant) {
        this.id = id;
        this.type = type;
        this.timeStamp = timeStamp;
        this.plant = plant;
    }
    public Notification(String id, int type, Date timeStamp, Story story) {
        this.id = id;
        this.type = type;
        this.timeStamp = timeStamp;
        this.story = story;
    }
    public Notification(String id, int type, Date timeStamp, SensorData sensorData) {
        this.id = id;
        this.type = type;
        this.timeStamp = timeStamp;
        this.sensorData = sensorData;
    };

    public String getId() { return id; }
    public String getMessage() { return message; }
    public int getType() { return type; }
    public Date getTimeStamp() { return timeStamp; }
    public Item getItem() { return item; }
    public Plant getPlant() { return plant; }
    public Story getStory() { return story; }
    public SensorData getSensorData() { return sensorData; }

    public void setId(String id) { this.id = id; }
    public void setMessage(String message) { this.message = message; }
    public void setType(int type) { this.type = type; }
    public void setTimeStamp(Date timeStamp) { this.timeStamp = timeStamp; }
    public void setItem(Item item) { this.item = item; }
    public void setPlant(Plant plant) { this.plant = plant; }
    public void setStory(Story story) { this.story = story; }
    public void setSensorData(SensorData sensorData) { this.sensorData = sensorData; }
}
