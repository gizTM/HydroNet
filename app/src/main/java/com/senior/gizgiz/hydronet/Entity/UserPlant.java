package com.senior.gizgiz.hydronet.Entity;

/**
 * Created by Admins on 005 5/2/2018.
 */

public class UserPlant extends Plant {
    private String location;
    private GrowHistory growHistory;

    public UserPlant(String name,String location) {
        super(name);
        this.location = location;
    }

    public UserPlant(String name,GrowHistory growHistory) {
        super(name);
        this.growHistory = growHistory;
    }

    public String getLocation() { return location; }
    public GrowHistory getGrowHistory() { return growHistory; }

    public void setLocation(String location) { this.location = location; }
    public void setGrowHistory(GrowHistory growHistory) { this.growHistory = growHistory; }
}
