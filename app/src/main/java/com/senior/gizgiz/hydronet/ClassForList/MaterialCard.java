package com.senior.gizgiz.hydronet.ClassForList;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class MaterialCard {
    private int id;
    private String name,detail;

    public MaterialCard(int id, String name, String detail) {
        this.id = id;
        this.name = name;
        this.detail = detail;
    }

    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public String getDetail() { return this.detail; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDetail(String detail) { this.detail = detail; }
}
