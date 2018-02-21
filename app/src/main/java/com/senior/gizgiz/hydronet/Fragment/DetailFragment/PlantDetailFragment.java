package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class PlantDetailFragment extends Fragment implements OnBackPressListener {
    private GridView plantList;
    private PlantAdapter plantAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_plant_plant_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        plantList = view.findViewById(R.id.my_plant_list);
        plantAdapter = new PlantAdapter(getContext(),PlantAdapter.exampleUserPlants);
        plantList.setAdapter(plantAdapter);
        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),PlantAdapter.exampleUserPlants.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
}
