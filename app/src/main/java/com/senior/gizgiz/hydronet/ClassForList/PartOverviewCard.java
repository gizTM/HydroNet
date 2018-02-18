package com.senior.gizgiz.hydronet.ClassForList;

/**
 * Created by Admins on 018 18/02/2018.
 */

public class PartOverviewCard {
    private int id,cost;
    private String name;

    public PartOverviewCard(int id, String name, int cost) {
        this.id = id;
        this.cost = cost;
        this.name = name;
    }

    public int getId() { return id; }
    public int getCost() { return cost; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setCost(int cost) { this.cost = cost; }
    public void setName(String name) { this.name = name; }
}
