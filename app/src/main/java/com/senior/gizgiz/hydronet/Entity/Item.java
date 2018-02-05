package com.senior.gizgiz.hydronet.Entity;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class Item {
    private String name, detail;
    private String type;

    public Item(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    public String getDetail() { return detail; }
    public String getType() { return type; }

    public void setName(String name) { this.name = name; }
    public void setDetail(String detail) { this.detail = detail; }
    public void setType(String type) { this.type = type; }
}
