package com.senior.gizgiz.hydronet.Entity;

/**
 * Created by Admins on 006 6/2/2018.
 */

public class SystemDefaultPlant extends Plant {
    public SystemDefaultPlant() {}
    public SystemDefaultPlant(String name) { super(name); }
    public SystemDefaultPlant(String name,float pHLow,float pHHigh,float ECLow,float ECHigh) {
        super(name,pHLow,pHHigh,ECLow,ECHigh);
    }
    public SystemDefaultPlant(String name, String property, String otherInfo,float pHLow,float pHHigh,float ECLow,float ECHigh) {
        super(name, pHLow, pHHigh, ECLow, ECHigh, property, otherInfo);
    }
    public void update(String name, String property, String otherInfo) {
        super.setName(name);
        super.setProperty(property);
        super.setOtherInfo(otherInfo);
    }
}
