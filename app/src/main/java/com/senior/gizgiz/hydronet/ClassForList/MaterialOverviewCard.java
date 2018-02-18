package com.senior.gizgiz.hydronet.ClassForList;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class MaterialOverviewCard {
    private int id;
    private String name;
    private float cost;

    public MaterialOverviewCard(int id, String name, float cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public float getCost() { return this.cost; }

    public void setId(int id) { this.id = id; }
    public void  setName(String name) { this.name = name; }
    public void setCost(float cost) { this.cost = cost; }
}
