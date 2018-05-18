package com.senior.gizgiz.hydronet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class SensorData implements Parcelable {
    private String timestamp;
    private float waterLevel, pHLevel, ecLevel;


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SensorData createFromParcel(Parcel in) {
            return new SensorData(in);
        }

        public SensorData[] newArray(int size) {
            return new SensorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(waterLevel);
        dest.writeFloat(pHLevel);
        dest.writeFloat(ecLevel);
        dest.writeString(timestamp);
    }

    public SensorData(Parcel in) {
        waterLevel = in.readFloat();
        pHLevel = in.readFloat();
        ecLevel = in.readFloat();
        timestamp = in.readString();
    }

    public SensorData() {  }
    public SensorData(String timestamp, float waterLevel, float pHLevel, float ecLevel) {
        this.timestamp = timestamp;
        this.waterLevel = waterLevel;
        this.pHLevel = pHLevel;
        this.ecLevel = ecLevel;
    }

    public float getWaterLevel() { return waterLevel; }
    public float getpHLevel() { return pHLevel; }
    public float getECLevel() { return ecLevel; }
    public String getTimestamp() { return timestamp; }

    public void setWaterLevel(float waterLevel) { this.waterLevel = waterLevel; }
    public void setpHLevel(float pHLevel) { this.pHLevel = pHLevel; }
    public void setECLevel(float ecLevel) { this.ecLevel = ecLevel; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
