package com.senior.gizgiz.hydronet.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class Item {
    private String id, name, detail;
    private float cost;

    public Item() { }

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

    public Map<String, Object> toMap() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("id",id);
        result.put("name",name);
        result.put("cost",cost);
        result.put("detail",detail);
        return result;
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
