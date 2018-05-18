package com.senior.gizgiz.hydronet.Entity;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admins on 005 5/2/2018.
 */

public class Plant implements Parcelable {
    private String id,name,property,otherInfo;
    private float pHLow, pHHigh, eCLow, eCHigh;
    private int growthDuration;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Plant createFromParcel(Parcel in) {
            return new Plant(in);
        }

        public Plant[] newArray(int size) {
            return new Plant[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(property);
        dest.writeString(otherInfo);
        dest.writeInt(growthDuration);
        dest.writeFloat(pHLow);
        dest.writeFloat(pHHigh);
        dest.writeFloat(eCLow);
        dest.writeFloat(eCHigh);
    }

    public Plant(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.property = in.readString();
        this.otherInfo = in.readString();
        this.growthDuration = in.readInt();
        this.pHLow = in.readFloat();
        this.pHHigh = in.readFloat();
        this.eCLow = in.readFloat();
        this.eCHigh = in.readFloat();
    }

    public Plant() { }

    public Plant(String name) {
        this.name = name;
    }

    public Plant(String name, int growthDuration, float pHLow, float pHHigh, float ECLow, float ECHigh) {
        this.name = name;
        this.growthDuration = growthDuration;
        this.pHLow = pHLow;
        this.pHHigh = pHHigh;
        this.eCLow = ECLow;
        this.eCHigh = ECHigh;
    }
    public Plant(String name,int growthDuration,float pHLow,float pHHigh,float ECLow,float ECHigh, String property, String otherInfo) {
        this.name = name;
        this.growthDuration = growthDuration;
        this.property = property;
        this.otherInfo = otherInfo;
        this.pHLow = pHLow;
        this.pHHigh = pHHigh;
        this.eCLow = ECLow;
        this.eCHigh = ECHigh;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getProperty() { return property; }
    public String getOtherInfo() { return otherInfo; }
    public float getpHLow() { return pHLow; }
    public float getpHHigh() {return pHHigh; }
    public float geteCLow() { return eCLow; }
    public float geteCHigh() { return eCHigh; }
    public int getGrowthDuration() { return growthDuration; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setProperty(String property) { this.property = property; }
    public void setOtherInfo(String otherInfo) { this.otherInfo = otherInfo; }
    public void setpHLow(float pHLow) { this.pHLow = pHLow; }
    public void setpHHigh(float pHHigh) { this.pHHigh = pHHigh; }
    public void seteCLow(float eCLow) { this.eCLow = eCLow; }
    public void seteCHigh(float eCHigh) { this.eCHigh = eCHigh; }
    public void setGrowthDuration(int growthDuration) { this.growthDuration = growthDuration; }


}
