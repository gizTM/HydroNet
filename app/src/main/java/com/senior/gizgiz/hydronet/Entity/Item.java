package com.senior.gizgiz.hydronet.Entity;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class Item {
    private String id, name, detail;
    private float cost;

    public Item(String id, String name, float cost, String detail) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.detail = detail;
    }

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDetail() { return detail; }
    public float getCost() { return cost; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDetail(String detail) { this.detail = detail; }
    public void setCost(float cost) { this.cost = cost; }
}
