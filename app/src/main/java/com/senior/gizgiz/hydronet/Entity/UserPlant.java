package com.senior.gizgiz.hydronet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 005 5/2/2018.
 */

public class UserPlant extends Plant {
    private List<GrowHistory> growHistories;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public UserPlant createFromParcel(Parcel in) {
            return new UserPlant(in);
        }

        public UserPlant[] newArray(int size) {
            return new UserPlant[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeList(growHistories);
    }

    public UserPlant(Parcel in) {
        super(in);
        this.growHistories = new ArrayList<>();
        in.readList(growHistories,GrowHistory.class.getClassLoader() );
    }

    public UserPlant() {}
    public UserPlant(String name,List<GrowHistory> growHistory) {
        super(name);
        this.growHistories = growHistory;
    }

    public UserPlant(String name, int growthDuration, float pHLow, float pHHigh, float ECLow, float ECHigh, List<GrowHistory> growHistory) {
        super(name, growthDuration,pHLow, pHHigh, ECLow, ECHigh);
        this.growHistories = growHistory;
    }

    public UserPlant(String name, int growthDuration,float pHLow, float pHHigh, float ECLow, float ECHigh) {
        super(name, growthDuration,pHLow, pHHigh, ECLow, ECHigh);
    }

    public UserPlant(String name, int growthDuration,float pHLow, float pHHigh, float ECLow, float ECHigh, String property, String otherInfo) {
        super(name, growthDuration,pHLow, pHHigh, ECLow, ECHigh, property, otherInfo);
//        this.growHistories = growHistory;
    }

    public List<GrowHistory> getGrowHistories() { return growHistories; }

    public void setGrowHistories(List<GrowHistory> growHistories) { this.growHistories = growHistories; }

    public void addGrowHistory(GrowHistory growHistory) { this.growHistories.add(growHistory); }
}
