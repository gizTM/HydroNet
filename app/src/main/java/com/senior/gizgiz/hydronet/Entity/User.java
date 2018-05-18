package com.senior.gizgiz.hydronet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class User implements Parcelable {
    private String username, email, password, contactDetail;
    private int coin;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
    }

    public User(Parcel in) {
        this.username = in.readString();
    }

    public User() { }

    public User(String username) { this.username = username; }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getContactDetail() { return contactDetail; }
    public int getCoin() { return coin; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setContactDetail(String contactDetail) { this.contactDetail = contactDetail; }
    public void setCoin(int coin) { this.coin = coin; }
}
