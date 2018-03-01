package com.senior.gizgiz.hydronet.Entity;

import java.util.ArrayList;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class Story {
    private User owner;
    private String  detail;
    private int type,likedCount, sharedCount;
    private ArrayList<User> likedUser, sharedUser;
    private Plant mentionedPlant;

    private boolean liked;

    public Story(User owner, int type, String detail, Plant mentionedPlant) {
        this.owner = owner;
        this.type = type;
        this.detail = detail;
        this.likedUser = new ArrayList<>();
        this.sharedUser = new ArrayList<>();
        this.mentionedPlant = mentionedPlant;
        this.liked = false;
    }

    public User getOwner() { return owner; }
    public int getType() { return type; }
    public String getDetail() { return detail; }
    public int getLikedCount() { return likedUser.size(); }
    public int getSharedCount() { return sharedUser.size(); }
    public ArrayList<User> getLikedUser() { return likedUser; }
    public ArrayList<User> getSharedUser() { return sharedUser; }
    public Plant getMentionedPlant() { return mentionedPlant; }
    public boolean getLiked() { return liked; }

    public void setOwner(User owner) { this.owner = owner; }
    public void setType(int type) { this.type = type; }
    public void setDetail(String detail) { this.detail = detail; }
    public void addLikedUser(User user) { likedUser.add(user); }
    public void addSharedUser(User user) { sharedUser.add(user); }
    public void setLikedUser(ArrayList<User> likedUser) { this.likedUser = likedUser; }
    public void setSharedUser(ArrayList<User> sharedUser) { this.sharedUser = sharedUser; }
    public void setMentionedPlant(Plant mentionedPlant) { this.mentionedPlant = mentionedPlant; }
    public void setLiked(boolean liked) { this.liked = liked; }
}
