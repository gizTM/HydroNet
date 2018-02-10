package com.senior.gizgiz.hydronet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantGridViewAdapter;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 007 07/02/2018.
 */

public class MyPlantFragment extends FlipperFragment {
    private GridView plantList;
    private PlantGridViewAdapter plantAdapter;

    public MyPlantFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater,container,savedInstanceState);

        return inflater.inflate(R.layout.fragment_plant_plant, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
//    @Override public void setViewFirstPage() { flipperLayout.setViewFirstPage(); }
//    @Override public void setViewSecondPage() { flipperLayout.setViewSecondPage(); }
//    @Override public void resetFlipper() { flipperLayout.resetFlipper(); }
}
