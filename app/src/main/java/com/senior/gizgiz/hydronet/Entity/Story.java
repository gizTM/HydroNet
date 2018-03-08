package com.senior.gizgiz.hydronet.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class Story {
    private User owner;
    private String remark;
    private int type,likedCount, sharedCount;
    private ArrayList<User> likedUser, sharedUser;
    private UserPlant mentionedPlant;

    private boolean liked;

    public Story() {}
    public Story(User owner, int type, String remark, UserPlant mentionedPlant) {
        this.owner = owner;
//        this.id = id;
        this.type = type;
        this.remark = remark;
        this.likedUser = new ArrayList<>();
        this.sharedUser = new ArrayList<>();
        this.mentionedPlant = mentionedPlant;
        this.liked = false;
    }

    public Map<String, Object> toMap() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("owner",owner.getUsername());
        result.put("liked",likedCount);
        result.put("mentionedplant",mentionedPlant.getName());
        result.put("remark",remark);
        return result;
    }

//    public String getId() { return id; }
    public User getOwner() { return owner; }
    public int getType() { return type; }
    public String getRemark() { return remark; }
    public int getLikedCount() { return likedUser.size(); }
    public int getSharedCount() { return sharedUser.size(); }
    public ArrayList<User> getLikedUser() { return likedUser; }
    public ArrayList<User> getSharedUser() { return sharedUser; }
    public UserPlant getMentionedPlant() { return mentionedPlant; }
    public boolean getLiked() { return liked; }

//    public void setId(String id) { this.id = id; }
    public void setOwner(User owner) { this.owner = owner; }
    public void setType(int type) { this.type = type; }
    public void setRemark(String remark) { this.remark = remark; }
    public void addLikedUser(User user) { likedUser.add(user); }
    public void addSharedUser(User user) { sharedUser.add(user); }
    public void setLikedUser(ArrayList<User> likedUser) { this.likedUser = likedUser; }
    public void setSharedUser(ArrayList<User> sharedUser) { this.sharedUser = sharedUser; }
    public void setMentionedPlant(UserPlant mentionedPlant) { this.mentionedPlant = mentionedPlant; }
    public void setLiked(boolean liked) { this.liked = liked; }
}
