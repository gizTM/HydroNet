package com.senior.gizgiz.hydronet.CustomClassAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Admins on 022 22/1/2018.
 */

public class SlidingTabAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    ArrayList<Fragment> tabFragments;

    public SlidingTabAdapter(FragmentManager fm, int tabCount, ArrayList<Fragment> tabFragments) {
        super(fm);
        this.tabCount = tabCount;
        this.tabFragments = tabFragments;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) {}
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
