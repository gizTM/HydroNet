package com.senior.gizgiz.hydronet.CustomClassForList;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class DropdownItem {
    private boolean selected;
    private String info;

    public DropdownItem(String location, boolean selected) {
        this.selected = selected;
        this.info = location;
    }

    public boolean isSelected() { return selected; }
    public String getInfo() { return info; }

    public void setSelected(boolean selected) { this.selected = selected; }
    public void setInfo(String info) { this.info = info; }
}
