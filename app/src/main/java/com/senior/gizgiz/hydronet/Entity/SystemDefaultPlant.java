package com.senior.gizgiz.hydronet.Entity;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class SystemDefaultPlant extends Plant {
    public SystemDefaultPlant(String id,String name) { super(id,name); }
    public SystemDefaultPlant(String id,String name, String property, String otherInfo) { super(id,name,property,otherInfo); }

    public void update(String name, String property, String otherInfo) {
        super.setName(name);
        super.setProperty(property);
        super.setOtherInfo(otherInfo);
    }
}
