package com.senior.gizgiz.hydronet.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Admins on 022 22/1/2018.
 */

public class SlidingTabAdapter extends FragmentPagerAdapter {
    int tabCount;
    List<Fragment> tabFragments;

    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public SlidingTabAdapter(FragmentManager fm, List<Fragment> tabFragments) {
        super(fm);
        this.tabCount = tabFragments.size();
        this.tabFragments = tabFragments;
    }

    @Override
    public Fragment getItem(int position) {
//        if(position==0) {}
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}
