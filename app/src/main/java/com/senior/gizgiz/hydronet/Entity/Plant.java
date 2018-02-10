package com.senior.gizgiz.hydronet.Entity;

import org.parceler.Parcel;

/**
 * Created by Admins on 005 5/2/2018.
 */

@Parcel
public class Plant {
    int id;
    String name,property,otherInfo;

    public Plant() {}
    public Plant(String name) { this.name = name; }
    public Plant(String name, String property, String otherInfo) {
        this.name = name;
        this.property = property;
        this.otherInfo = otherInfo;
    }


    public String getName() { return name; }
    public String getProperty() { return property; }
    public String getOtherInfo() { return otherInfo; }

    public void setName(String name) { this.name = name; }
    public void setProperty(String property) { this.property = property; }
    public void setOtherInfo(String otherInfo) { this.otherInfo = otherInfo; }
}
