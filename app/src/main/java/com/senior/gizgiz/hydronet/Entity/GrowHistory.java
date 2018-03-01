package com.senior.gizgiz.hydronet.Entity;

import java.util.Date;
import java.util.List;

/**
 * Created by Admins on 005 5/2/2018.
 */

public class GrowHistory {
    private Date startDate,harvestDate;
    private boolean harvested;
    private int count;
    private List<String> locationList;
    private String result,remark;

    public GrowHistory() {
        this.harvested = false;
        this.count = 0;
    }
    public GrowHistory(Date startDate,List<String> locationList) {
        this.startDate = startDate;
        this.locationList = locationList;
        this.count = locationList.size();
    }

    public Date getStartDate() { return startDate; }
    public Date getHarvestDate() { return harvestDate; }
    public boolean isHarvested() { return harvested; }
    public int getCount() { return count; }
    public List<String> getLocationList() { return locationList; }
    public String getResult() { return result; }
    public String getRemark() { return remark; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setHarvestDate(Date harvestDate) { this.harvestDate = harvestDate; }
    public void setHarvested(boolean harvested) { this.harvested = harvested; }
    public void setCount(int count) { this.count = count; }
    public void setLocationList(List<String> locationList) { this.locationList = locationList; }
    public void setResult(String result) { this.result = result; }
    public void setRemark(String remark) { this.remark = remark; }

    public void addLocation(String location) { this.locationList.add(location); }
}
