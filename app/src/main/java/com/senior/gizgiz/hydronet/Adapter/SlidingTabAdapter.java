package com.senior.gizgiz.hydronet.Adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Admins on 022 22/1/2018.
 */

public class SlidingTabAdapter extends FragmentPagerAdapter {
    private int tabCount;
    private List<Fragment> tabFragments;
    private List<String> tabTitles;

    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public SlidingTabAdapter(FragmentManager fm) { super(fm); }
    public SlidingTabAdapter(FragmentManager fm, List<Fragment> tabFragments) {
        super(fm);
        this.tabCount = tabFragments.size();
        this.tabFragments = tabFragments;
//        Log.e("SlidingTabAdapter","constructor");
//        Log.e("count",tabCount+"");
    }
    public SlidingTabAdapter(FragmentManager fm, List<Fragment> tabFragments, List<String> tabTitles) {
        super(fm);
        this.tabCount = tabFragments.size();
        this.tabFragments = tabFragments;
        this.tabTitles = tabTitles;
//        Log.e("SlidingTabAdapter","constructor");
//        Log.e("count",tabCount+"");
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTitles.get(position);
//    }

    @Override
    public Fragment getItem(int position) {
//        Log.e("tabadapter","getItem");
//        Fragment fragment;
//        Bundle bundle = new Bundle();
//        fragment = new PlantGridViewFragment();
//        if(position==0) bundle.putInt("TYPE",0);
//        else bundle.putInt("TYPE",1);
//        fragment.setArguments(bundle);
//        return fragment;
        return tabFragments.get(position);
    }

//    public View getTabView(int position) {
//         Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
//        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
//        TextView tv = (TextView) v.findViewById(R.id.textView);
//        tv.setText(tabTitles[position]);
//        ImageView img = (ImageView) v.findViewById(R.id.imgView);
//        img.setImageResource(imageResId[position]);
//        return v;
//    }

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
