package com.senior.gizgiz.hydronet.ClassForList;

import java.util.List;

/**
 * Created by Admins on 018 18/03/2018.
 */

public class PlantLocation {
    private String plantName;
    private List<String> locationList;

    public PlantLocation(String plantName, List<String> locationList) {
        this.plantName = plantName;
        this.locationList = locationList;
    }

    public String getPlantName() { return plantName; }
    public List<String> getLocationList() { return locationList; }

    public void setPlantName(String plantName) { this.plantName = plantName; }
    public void setLocationList(List<String> locationList) { this.locationList = locationList; }
}
