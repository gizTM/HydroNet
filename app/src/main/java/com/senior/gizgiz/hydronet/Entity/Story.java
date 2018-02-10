package com.senior.gizgiz.hydronet.Entity;

import java.util.List;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class Story {
    private User owner;
    private String type, detail;
    private int likedCount, sharedCount;
    private List<User> likedUser, sharedUser;
    private Plant mentionedPlant;

    public Story(User owner, String type, String detail, Plant mentionedPlant) {
        this.owner = owner;
        this.type = type;
        this.detail = detail;
        this.mentionedPlant = mentionedPlant;
    }

    public User getOwner() { return owner; }
    public String getType() { return type; }
    public String getDetail() { return detail; }
    public int getLikedCount() { return likedCount; }
    public int getSharedCount() { return sharedCount; }
    public List<User> getLikedUser() { return likedUser; }
    public List<User> getSharedUser() { return sharedUser; }
    public Plant getMentionedPlant() { return mentionedPlant; }

    public void setOwner(User owner) { this.owner = owner; }
    public void setType(String type) { this.type = type; }
    public void setDetail(String detail) { this.detail = detail; }
    public void setLikedCount(int likedCount) { this.likedCount = likedCount; }
    public void setSharedCount(int sharedCount) { this.sharedCount = sharedCount; }
    public void setLikedUser(List<User> likedUser) { this.likedUser = likedUser; }
    public void setSharedUser(List<User> sharedUser) { this.sharedUser = sharedUser; }
    public void setMentionedPlant(Plant mentionedPlant) { this.mentionedPlant = mentionedPlant; }
}
