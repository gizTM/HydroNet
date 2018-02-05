package com.senior.gizgiz.hydronet.CustomClassForList;

/**
 * Created by Admins on 029 29/1/2018.
 */

public class HomeCard {
    private int id,count,date;
    private String name,location;
    private float health;

    public HomeCard(int id,String name,int count,String location,int date,float health) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.location = location;
        this.date = date;
        this.health = health;
    }

    public int getId() { return id; }
    public int getCount() { return count; }
    public int getDate() { return date; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public float getHealth() { return health; }

    public void setId(int id) { this.id = id; }
    public void setCount(int count) { this.count = count; }
    public void setDate(int date) { this.date = date; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setHealth(float health) { this.health = health; }
}
