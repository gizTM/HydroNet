package com.senior.gizgiz.hydronet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 005 5/2/2018.
 */

public class GrowHistory implements Parcelable {
    private String id,startDate,harvestDate,expectedHarvestDate,plantName,plantId,result,remark;
    private boolean harvested;
    private int count;
    private List<String> locationList,failedList,harvestList;
    private List<SensorData> sensorDataList;
    private Map sensorData = new HashMap<>();
    private UserPlant userPlant;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GrowHistory createFromParcel(Parcel in) {
            return new GrowHistory(in);
        }

        public GrowHistory[] newArray(int size) {
            return new GrowHistory[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(count);
        dest.writeString(startDate);
        dest.writeString(expectedHarvestDate);
        dest.writeByte((byte) (harvested?1:0));
        if(harvested) dest.writeString(harvestDate);
        dest.writeString(remark);
        dest.writeString(result);
        dest.writeString(plantName);
        dest.writeMap(sensorData);
        dest.writeList(locationList);
        dest.writeList(failedList);
        dest.writeList(harvestList);
        dest.writeList(sensorDataList);
        dest.writeString(plantId);
    }

    public GrowHistory(Parcel in) {
        this.id = in.readString();
        this.count = in.readInt();
        this.startDate = in.readString();
        this.expectedHarvestDate = in.readString();
        this.harvested = in.readByte() != 0;
        if(harvested) this.harvestDate = in.readString();
        this.remark = in.readString();
        this.result = in.readString();
        this.plantName = in.readString();
        this.sensorData = new HashMap();
        in.readMap(sensorData,null);
        this.locationList = new ArrayList<>();
        in.readList(locationList,null);
        this.failedList = new ArrayList<>();
        in.readList(failedList,null);
        this.harvestList = new ArrayList<>();
        in.readList(harvestList,null);
        this.sensorDataList = new ArrayList<>();
        in.readList(sensorDataList,null);
        this.plantId = in.readString();
    }

    public GrowHistory() {
        this.harvested = false;
        this.count = 0;
        this.result = "growing";
    }
    public GrowHistory(String plantName) {
        this.plantName = plantName;
    }
    public GrowHistory(String startDate,List<String> locationList) {
        this.startDate = startDate;
        this.locationList = locationList;
        this.count = locationList.size();
        this.result = "growing";
        this.harvested = false;
    }

    public GrowHistory(String plantName,String startDate, boolean harvested, int count, List<String> locationList) {
        this.plantName = plantName;
        this.startDate = startDate;
        this.harvested = harvested;
        this.count = count;
        this.result = "growing";
        this.locationList = locationList;
    }

    public String getPlantName() { return plantName; }
    public String getStartDate() { return startDate; }
    public String getHarvestDate() { return harvestDate; }
    public boolean isHarvested() { return harvested; }
    public int getCount() { return count; }
    public List<String> getLocationList() { return locationList; }
    public String getResult() { return result; }
    public String getRemark() { return remark; }
    public List<SensorData> getSensorDataList() { return sensorDataList; }
    public Map<String, Boolean> getSensorData() { return sensorData; }
    public UserPlant getUserPlant() { return userPlant; }
    public String getExpectedHarvestDate() { return expectedHarvestDate; }
    public List<String> getFailedList() { return failedList; }
    public String getPlantId() { return plantId; }
    public String getId() { return id; }
    public List<String> getHarvestList() { return harvestList; }

    public void setPlantName(String plantName) { this.plantName = plantName; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setHarvestDate(String harvestDate) { this.harvestDate = harvestDate; }
    public void setHarvested(boolean harvested) { this.harvested = harvested; }
    public void setCount(int count) { this.count = count; }
    public void setLocationList(List<String> locationList) { this.locationList = locationList; }
    public void setResult(String result) { this.result = result; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setSensorDataList(List<SensorData> sensorDataList) { this.sensorDataList = sensorDataList; }
    public void setSensorData(Map<String, Boolean> sensorData) { this.sensorData = sensorData; }
    public void setUserPlant(UserPlant userPlant) { this.userPlant = userPlant; }
    public void setExpectedHarvestDate(String expectedHarvestDate) { this.expectedHarvestDate = expectedHarvestDate; }
    public void setFailedList(List<String> failedList) { this.failedList = failedList; }
    public void setPlantId(String plantId) { this.plantId = plantId; }
    public void setId(String id) { this.id = id; }
    public void setHarvestList(List<String> harvestList) { this.harvestList = harvestList; }

    public void addLocation(String location) { this.locationList.add(location); }
    public void addSensorData(SensorData sensorData) {
        if(this.sensorDataList == null) this.sensorDataList = new ArrayList<>();
        this.sensorDataList.add(sensorData);
    }
}
