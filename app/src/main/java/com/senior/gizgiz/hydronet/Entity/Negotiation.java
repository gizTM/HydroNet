package com.senior.gizgiz.hydronet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admins on 2018/02/06.
 */

public class Negotiation implements Parcelable {
    private String id,storyId, negotiatorId, condition, status, negotiator,timestamp;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Negotiation createFromParcel(Parcel in) {
            return new Negotiation(in);
        }

        public Negotiation[] newArray(int size) {
            return new Negotiation[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(storyId);
        dest.writeString(negotiatorId);
        dest.writeString(negotiator);
        dest.writeString(condition);
        dest.writeString(status);
        dest.writeString(timestamp);
    }

    public Negotiation(Parcel in) {
        this.id = in.readString();
        this.storyId = in.readString();
        this.negotiatorId = in.readString();
        this.negotiator = in.readString();
        this.condition = in.readString();
        this.status = in.readString();
        this.timestamp = in.readString();
    }

    public Negotiation() {}

    public Negotiation(String id, String storyId, String negotiatorId, String condition, String status, String negotiator, String timestamp) {
        this.id = id;
        this.storyId = storyId;
        this.negotiatorId = negotiatorId;
        this.condition = condition;
        this.status = status;
        this.negotiator = negotiator;
        this.timestamp = timestamp;
    }

    public String getNegotiator() { return negotiator; }
    public String getNegotiatorId() { return negotiatorId; }
    public String getCondition() { return condition; }
    public String getStatus() { return status; }
    public String getStoryId() { return storyId; }
    public String getTimestamp() { return timestamp; }
    public String getId() { return id; }

    public void setNegotiator(String negotiator) { this.negotiator = negotiator; }
    public void setNegotiatorId(String negotiatorId) { this.negotiatorId = negotiatorId; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setStatus(String status) { this.status = status; }
    public void setStoryId(String storyId) { this.storyId = storyId; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setId(String id) { this.id = id; }
}
