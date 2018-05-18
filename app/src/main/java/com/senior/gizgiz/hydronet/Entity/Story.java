package com.senior.gizgiz.hydronet.Entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class Story implements Parcelable {
    private User owner;
    private String id, remark, mentionedPlantId, ownerId;
    private int type,mentionedPlantType;
    private ArrayList<User> likedUser, sharedUser;
    private Map likedUsers = new HashMap<>();
    private UserPlant mentionedUserPlant;
    private Plant mentionedPlant;
    private String postDate;

    private boolean liked;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        public Story[] newArray(int size) {
            return new Story[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mentionedPlantId);
        dest.writeInt(mentionedPlantType);
        dest.writeParcelable(owner,flags);
        if(mentionedPlantType==0) dest.writeParcelable(mentionedPlant,flags);
        else dest.writeParcelable(mentionedUserPlant,flags);
        dest.writeString(remark);
        dest.writeString(ownerId);
        dest.writeMap(likedUsers);
        dest.writeString(postDate);
        dest.writeInt(type);
    }

    public Story(Parcel in) {
        this.id = in.readString();
        this.mentionedPlantId = in.readString();
        this.mentionedPlantType = in.readInt();
        this.owner = in.readParcelable(User.class.getClassLoader());
        if(mentionedPlantType==0) this.mentionedPlant = in.readParcelable(Plant.class.getClassLoader());
        else this.mentionedUserPlant = in.readParcelable(UserPlant.class.getClassLoader());
        this.remark = in.readString();
        this.ownerId = in.readString();
        this.likedUsers = new HashMap<String,Boolean>();
        in.readMap(likedUsers,Map.class.getClassLoader());
        this.postDate = in.readString();
        this.type = in.readInt();
    }

    public Story() {}

//    public Story(String id, String ownerId, String ) {
//
//    }

    public Story(int type, String remark, String postDate) {
        this.type = type;
        this.remark = remark;
        this.likedUser = new ArrayList<>();
        this.sharedUser = new ArrayList<>();
        this.liked = false;
        this.postDate = postDate;
    }

    public Story(User owner, int type, String remark,String postDate) {
        this.owner = owner;
        this.type = type;
        this.remark = remark;
        this.likedUser = new ArrayList<>();
        this.sharedUser = new ArrayList<>();
        this.liked = false;
        this.postDate = postDate;
    }

    public Story(User owner, int type, String remark, Plant mentionedPlant, String postDate) {
        this.owner = owner;
        this.type = type;
        this.remark = remark;
        this.likedUser = new ArrayList<>();
        this.sharedUser = new ArrayList<>();
        this.mentionedPlant = mentionedPlant;
        this.liked = false;
        this.postDate = postDate;
    }
    public Story(User owner, int type, String remark, UserPlant mentionedUserPlant, String postDate) {
        this.owner = owner;
        this.type = type;
        this.remark = remark;
        this.likedUser = new ArrayList<>();
        this.sharedUser = new ArrayList<>();
        this.mentionedUserPlant = mentionedUserPlant;
        this.liked = false;
        this.postDate = postDate;
    }

    public String getId() { return id; }
    public String getMentionedPlantId() { return mentionedPlantId; }
    public User getOwner() { return owner; }
    public int getType() { return type; }
    public String getRemark() { return remark; }
    public int getLikedCount() { return likedUser.size(); }
    public int getSharedCount() { return sharedUser.size(); }
    public ArrayList<User> getLikedUser() { return likedUser; }
    public ArrayList<User> getSharedUser() { return sharedUser; }
    public Plant getMentionedPlant() { return mentionedPlant; }
    public boolean getLiked() { return liked; }
    public String getPostDate() { return postDate; }
    public UserPlant getMentionedUserPlant() { return mentionedUserPlant; }
    public int getMentionedPlantType() { return mentionedPlantType; }
    public String getOwnerId() { return ownerId; }
    public Map<String, Boolean> getLikedUsers() { return likedUsers; }

    public void setId(String id) { this.id = id; }
    public void setMentionedPlantId(String mentionedPlantId) { this.mentionedPlantId = mentionedPlantId; }
    public void setOwner(User owner) { this.owner = owner; }
    public void setType(int type) { this.type = type; }
    public void setRemark(String remark) { this.remark = remark; }
    public void addLikedUser(User user) { likedUser.add(user); }
    public void addSharedUser(User user) { sharedUser.add(user); }
    public void setLikedUser(ArrayList<User> likedUser) { this.likedUser = likedUser; }
    public void setSharedUser(ArrayList<User> sharedUser) { this.sharedUser = sharedUser; }
    public void setMentionedPlant(UserPlant mentionedPlant) { this.mentionedPlant = mentionedPlant; }
    public void setLiked(boolean liked) { this.liked = liked; }
    public void setPostDate(String postDate) { this.postDate = postDate; }
    public void setMentionedUserPlant(UserPlant mentionedUserPlant) { this.mentionedUserPlant = mentionedUserPlant; }
    public void setMentionedPlantType(int mentionedPlantType) { this.mentionedPlantType = mentionedPlantType; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public void setLikedUsers(Map<String,Boolean> likedUsers) { this.likedUsers = likedUsers; }
}
