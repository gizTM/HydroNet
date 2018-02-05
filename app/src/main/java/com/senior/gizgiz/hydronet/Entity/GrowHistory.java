package com.senior.gizgiz.hydronet.Entity;

import java.util.Date;
import java.util.List;

/**
 * Created by Admins on 005 5/2/2018.
 */

public class GrowHistory {
    private Plant plant;
    private Date startDate,harvestDate;
    private int count;
    private List<String> locationList;
    private String result,remark;

    public GrowHistory() {}
    public GrowHistory(List<String> locationList) { this.locationList = locationList; }
    public GrowHistory(Date startDate) { this.startDate = startDate; }
    public GrowHistory(Plant plant, List<String> locationList) {
        this.plant = plant;
        this.locationList = locationList;
        this.count = locationList.size();
    }
}
