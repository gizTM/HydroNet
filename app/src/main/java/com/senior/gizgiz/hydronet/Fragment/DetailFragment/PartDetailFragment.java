package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantGridViewAdapter;
import com.senior.gizgiz.hydronet.Fragment.BackPressImpl;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class PartDetailFragment extends Fragment implements OnBackPressListener {
    private ListView partList;
    private PlantGridViewAdapter partAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_plant_part_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        partList = view.findViewById(R.id.my_part_list);
        partAdapter = new PlantGridViewAdapter(getContext(),PlantGridViewAdapter.exampleUserPlants);
        partList.setAdapter(partAdapter);
    }

    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }
}
