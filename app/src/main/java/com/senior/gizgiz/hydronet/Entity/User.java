package com.senior.gizgiz.hydronet.Entity;

import android.provider.ContactsContract.CommonDataKinds.Email;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class User {
    private String username, contactDetail;
    private List<Story> storyList;
    private List<Plant> plantList;
    private SensorData sensorData;

    public User(String username) {
        this.username = username;
    }

    public User(String username, Email email, String password) {
        this.username = username;
    }

    public String getUsername() { return username; }
    public List<Story> getStoryList() { return storyList; }
    public String getContactDetail() { return contactDetail; }
    public List<Plant> getPlantList() { return plantList; }
    public SensorData getSensorData() { return sensorData; }

    public void setUsername(String username) { this.username = username; }
    public void setStoryList(List<Story> storyList) { this.storyList = storyList; }
    public void setContactDetail(String contactDetail) { this.contactDetail = contactDetail; }
    public void setPlantList(List<Plant> plantList) { this.plantList = plantList; }
    public void setSensorData(SensorData sensorData) { this.sensorData = sensorData; }
}
