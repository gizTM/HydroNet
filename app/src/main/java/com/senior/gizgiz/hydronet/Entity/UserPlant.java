package com.senior.gizgiz.hydronet.Entity;

import java.util.List;

/**
 * Created by Admins on 005 5/2/2018.
 */

public class UserPlant extends Plant {
    private List<GrowHistory> growHistory;

    public UserPlant() {}
    public UserPlant(String name,List<GrowHistory> growHistory) {
        super(name);
        this.growHistory = growHistory;
    }

    public List<GrowHistory> getGrowHistory() { return growHistory; }

    public void setGrowHistory(List<GrowHistory> growHistory) { this.growHistory = growHistory; }

    public void addGrowHistory(GrowHistory growHistory) { this.growHistory.add(growHistory); }
}
