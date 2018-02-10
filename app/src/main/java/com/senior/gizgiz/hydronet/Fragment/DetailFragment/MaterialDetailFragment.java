package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantGridViewAdapter;
import com.senior.gizgiz.hydronet.Fragment.BackPressImpl;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class MaterialDetailFragment extends Fragment implements OnBackPressListener {
    private Fragment parentFragment;
    private ListView materialList;
    private PlantGridViewAdapter materialAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_plant_material_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        materialList = view.findViewById(R.id.my_material_list);
        materialAdapter = new PlantGridViewAdapter(getContext(),PlantGridViewAdapter.exampleUserPlants);
        materialList.setAdapter(materialAdapter);
        materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),PlantGridViewAdapter.exampleUserPlants.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }
}
