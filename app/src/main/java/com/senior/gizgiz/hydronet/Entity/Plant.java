package com.senior.gizgiz.hydronet.Entity;


/**
 * Created by Admins on 005 5/2/2018.
 */

public class Plant {
    private String name,property,otherInfo;
    private float pHLow, pHHigh, ECLow, ECHigh;

    public Plant() { }

    public Plant(String name) {
        this.name = name;
    }

    public Plant(String name, float pHLow, float pHHigh, float ECLow, float ECHigh) {
        this.name = name;
        this.pHLow = pHLow;
        this.pHHigh = pHHigh;
        this.ECLow = ECLow;
        this.ECHigh = ECHigh;
    }
    public Plant(String name,float pHLow,float pHHigh,float ECLow,float ECHigh, String property, String otherInfo) {
        this.name = name;
        this.property = property;
        this.otherInfo = otherInfo;
        this.pHLow = pHLow;
        this.pHHigh = pHHigh;
        this.ECLow = ECLow;
        this.ECHigh = ECHigh;
    }

    public String getName() { return name; }
    public String getProperty() { return property; }
    public String getOtherInfo() { return otherInfo; }
    public float getpHLow() { return pHLow; }
    public float getpHHigh() {return pHHigh; }
    public float getECLow() { return ECLow; }
    public float getECHigh() { return ECHigh; }

    public void setName(String name) { this.name = name; }
    public void setProperty(String property) { this.property = property; }
    public void setOtherInfo(String otherInfo) { this.otherInfo = otherInfo; }
    public void setpHLow(float pHLow) { this.pHLow = pHLow; }
    public void setpHHigh(float pHHigh) { this.pHHigh = pHHigh; }
    public void setECLow(float ECLow) { this.ECLow = ECLow; }
    public void setECHigh(float ECHigh) { this.ECHigh = ECHigh; }
}
