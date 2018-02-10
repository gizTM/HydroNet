package com.senior.gizgiz.hydronet.ClassForList;

import com.senior.gizgiz.hydronet.Entity.Plant;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class PlantBadge {
    private int count;
    private Plant plant;

    public PlantBadge(Plant plant, int count) {
        this.plant = plant;
        this.count = count;
    }

    public int getCount() { return count; }
    public Plant getPlant() { return plant; }

    public void setCount(int count) { this.count = count; }
    public void setPlant(Plant plant) { this.plant = plant; }
}
