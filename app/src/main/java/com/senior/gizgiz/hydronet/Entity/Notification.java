package com.senior.gizgiz.hydronet.Entity;

import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.Date;

/**
 * Created by Admins on 002 02/03/2018.
 */

public class Notification {
    private String id,message,senderId,storyId,timeStamp,title="HydroNet";
    private boolean read,notifiedTransaction;
    private int type,storyType;
    private Item item;
    private Plant plant;
    private UserPlant userPlant;
    private Story story;
    private ProgressStory progressStory;
    private ProductAnnouncementStory saleStory;
    private SensorData sensorData;

    public Notification() {}

    public String getId() { return id; }
    public String getMessage() { return message; }
    public int getType() { return type; }
    public String getTimeStamp() { return timeStamp; }
    public Item getItem() { return item; }
    public Plant getPlant() { return plant; }
    public Story getStory() { return story; }
    public SensorData getSensorData() { return sensorData; }
    public String getSenderId() { return senderId; }
    public String getTitle() { return title; }
    public boolean isRead() { return read; }
    public String getStoryId() { return storyId; }
    public int getStoryType() { return storyType; }
    public UserPlant getUserPlant() { return userPlant; }
    public ProductAnnouncementStory getSaleStory() { return saleStory; }
    public ProgressStory getProgressStory() { return progressStory; }
    public boolean isNotifiedTransaction() { return notifiedTransaction; }

    public void setId(String id) { this.id = id; }
    public void setMessage(String message) { this.message = message; }
    public void setType(int type) { this.type = type; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }
    public void setItem(Item item) { this.item = item; }
    public void setPlant(Plant plant) { this.plant = plant; }
    public void setStory(Story story) { this.story = story; }
    public void setSensorData(SensorData sensorData) { this.sensorData = sensorData; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setTitle(String title) { this.title = title; }
    public void setRead(boolean read) { this.read = read; }
    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }
    public void setStoryType(int storyType) {
        this.storyType = storyType;
    }
    public void setUserPlant(UserPlant userPlant) { this.userPlant = userPlant; }
    public void setSaleStory(ProductAnnouncementStory saleStory) { this.saleStory = saleStory; }
    public void setProgressStory(ProgressStory progressStory) { this.progressStory = progressStory; }
    public void setNotifiedTransaction(boolean notifiedTransaction) { this.notifiedTransaction = notifiedTransaction; }
}
