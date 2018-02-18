package com.senior.gizgiz.hydronet.ClassForList;

/**
 * Created by Admins on 018 18/02/2018.
 */

public class PlantOverviewCard {
    private String name;
    private int id,growing,harvested,failed,total;

    public PlantOverviewCard(int id, String plantName, int growing, int harvested, int failed) {
        this.id = id;
        this.name = plantName;
        this.growing = growing;
        this.harvested = harvested;
        this.failed = failed;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getGrowing() { return growing; }
    public int getHarvested() { return harvested; }
    public int getFailed() { return failed; }
    public int getTotal() { return total; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGrowing(int growing) { this.growing = growing; }
    public void setHarvested(int harvested) { this.harvested = harvested; }
    public void setFailed(int failed) { this.failed = failed; }
    public void setTotal(int total) { this.total = total; }
}
