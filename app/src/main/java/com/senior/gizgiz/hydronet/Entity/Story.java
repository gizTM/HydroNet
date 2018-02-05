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
}
