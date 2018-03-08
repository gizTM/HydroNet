package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.MaterialAdapter;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.MaterialOverviewFragment;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class MaterialDetailFragment extends Fragment implements OnBackPressListener,SwipeRefreshLayout.OnRefreshListener {
    private ListView materialList;
    private MaterialAdapter materialAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_plant_material_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        materialList = view.findViewById(R.id.my_material_list);
        materialAdapter = new MaterialAdapter(getContext(),MaterialAdapter.materials);
        materialList.setAdapter(materialAdapter);
        swipeRefreshLayout = view.findViewById(R.id.material_detail_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                MaterialOverviewFragment.fetchMaterialData(swipeRefreshLayout);
            }
        });
        materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),MaterialAdapter.materials.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
    @Override public void onRefresh() { MaterialOverviewFragment.fetchMaterialData(swipeRefreshLayout); }
}
