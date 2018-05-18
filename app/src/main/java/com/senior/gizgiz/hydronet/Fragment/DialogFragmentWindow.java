package com.senior.gizgiz.hydronet.Fragment;

import android.support.design.widget.TabLayout;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.senior.gizgiz.hydronet.Adapter.SlidingTabAdapter;
import com.senior.gizgiz.hydronet.HelperClass.ViewPagerParallax;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

public class DialogFragmentWindow extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_plant_select, container);
        TabLayout tabLayout = view.findViewById(R.id.sliding_tab);
        ViewPagerParallax viewPager = view.findViewById(R.id.plant_viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("System plant"));
        tabLayout.addTab(tabLayout.newTab().setText("User plant"));
        List<Fragment> fragments = getFragments();
        SlidingTabAdapter adapter = new SlidingTabAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    private List<Fragment> getFragments(){
        List<Fragment> plantFragments = new ArrayList<>();
        PlantGridViewFragment plantGridViewFragment = new PlantGridViewFragment();
        Bundle bundle = new Bundle(); bundle.putInt("TYPE",0); plantGridViewFragment.setArguments(bundle);
        plantFragments.add(plantGridViewFragment);
        bundle.putInt("TYPE",1); plantGridViewFragment.setArguments(bundle);
        plantFragments.add(plantGridViewFragment);
        return plantFragments;
    }
}
