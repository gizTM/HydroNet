package com.senior.gizgiz.hydronet.Entity;

import java.util.List;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class User {
    private String username, email, password, contactDetail;
    private List<Story> storyList;
    private List<Plant> plantList;
    private SensorData sensorData;

    public User() {  }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public List<Story> getStoryList() { return storyList; }
    public String getContactDetail() { return contactDetail; }
    public List<Plant> getPlantList() { return plantList; }
    public SensorData getSensorData() { return sensorData; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setStoryList(List<Story> storyList) { this.storyList = storyList; }
    public void setContactDetail(String contactDetail) { this.contactDetail = contactDetail; }
    public void setPlantList(List<Plant> plantList) { this.plantList = plantList; }
    public void setSensorData(SensorData sensorData) { this.sensorData = sensorData; }
}
