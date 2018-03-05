package com.senior.gizgiz.hydronet.Entity;


/**
 * Created by Admins on 005 5/2/2018.
 */

public class Plant {
    private String id,name,property,otherInfo;

    public Plant(String id,String name) {
        this.id = id;
        this.name = name;
    }
    public Plant(String id,String name, String property, String otherInfo) {
        this.id = id;
        this.name = name;
        this.property = property;
        this.otherInfo = otherInfo;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getProperty() { return property; }
    public String getOtherInfo() { return otherInfo; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setProperty(String property) { this.property = property; }
    public void setOtherInfo(String otherInfo) { this.otherInfo = otherInfo; }
}
