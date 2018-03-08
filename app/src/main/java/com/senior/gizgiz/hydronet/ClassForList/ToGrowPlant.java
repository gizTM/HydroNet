package com.senior.gizgiz.hydronet.ClassForList;

import com.senior.gizgiz.hydronet.Entity.Plant;

import java.util.List;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class ToGrowPlant {
    private int count;
    private Plant plant;
    private boolean pHOK,ECOK;
    private List<String> locationList;

    public ToGrowPlant(Plant plant, int count, List<String> locationList) {
        this.plant = plant;
        this.count = count;
        this.locationList = locationList;
        this.pHOK = true;
        this.ECOK = true;
    }

    public int getCount() { return count; }
    public Plant getPlant() { return plant; }
    public List<String> getLocationList() { return locationList; }
    public boolean ispHOK() { return pHOK; }
    public boolean isECOK() { return ECOK; }

    public void setCount(int count) { this.count = count; }
    public void setPlant(Plant plant) { this.plant = plant; }
    public void setLocationList(List<String> locationList) { this.locationList = locationList; }
    public void setpHOK(boolean pHOK) { this.pHOK = pHOK; }
    public void setECOK(boolean ECOK) { this.ECOK = ECOK; }

    public void addLocation(String location) { this.locationList.add(location); }
    public void addLocation(List<String> locations) { this.locationList.addAll(locations); }
    public void addCount(int count) { this.count+=count; }
    public boolean containLocation(String location) {
        for (String unit : this.locationList) {
            if(unit.equalsIgnoreCase(location)) return true;
        }
        return false;
    }
}
