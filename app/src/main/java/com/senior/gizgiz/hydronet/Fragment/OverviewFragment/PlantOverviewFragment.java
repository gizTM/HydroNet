package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.PlantOverviewAdapter;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.PlantDetailFragment;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class PlantOverviewFragment extends OverviewFragment implements OnBackPressListener {
    private ListView plantOverviewList;
    private PlantOverviewAdapter plantOverviewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_plant_plant_overview, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        plantOverviewList = view.findViewById(R.id.plant_detail_list);
        plantOverviewAdapter = new PlantOverviewAdapter(getContext(), PlantAdapter.exampleUserPlants);
        plantOverviewList.setAdapter(plantOverviewAdapter);
        plantOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), PlantOverviewAdapter.exampleCards.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
                Plant plant = PlantAdapter.exampleUserPlants.get(i);
                Toast.makeText(getContext(), plant.getName()+" is selected!\n" +
                        "planted "+((UserPlant) plant).getGrowHistory().size()+" times!",Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.btn_show_detail_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });
    }

    private void enterNextFragment() {
        PlantDetailFragment detailFragment = new PlantDetailFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_plant, detailFragment);
        transaction.commit();
    }

    @Override
    public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }

    @Override
    public boolean setViewOverview() {
        return new BackPressHandler(this).onBackPressed();
    }
}
