package com.senior.gizgiz.hydronet.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.senior.gizgiz.hydronet.Activity.AddStoryActivity;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.ChangePlantAdapter;
import com.senior.gizgiz.hydronet.R;

public class PlantGridViewFragment extends Fragment {
    private GridView gridView;
    private ChangePlantAdapter adapter;

    private int plantType;

    public PlantGridViewFragment() { }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Bundle args = getArguments();
        plantType = args.getInt("TYPE");
        Log.e("PlantGridViewFragment","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_gridview_layout,viewGroup,false);
        gridView = view.findViewById(R.id.simple_grid_view);
        Log.e("PlantGridViewFragment","onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        if(plantType == 0) adapter = AddStoryActivity.systemPlantAdapter;
        else adapter = AddStoryActivity.userPlantAdapter;
        gridView.setAdapter(adapter);
        Log.e("PlantGridViewFragment","onViewCreated");
    }
}
