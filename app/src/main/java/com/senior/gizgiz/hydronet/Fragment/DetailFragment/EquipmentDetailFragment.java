package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.EquipmentAdapter;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.EquipmentOverviewFragment;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class EquipmentDetailFragment extends Fragment implements OnBackPressListener,SwipeRefreshLayout.OnRefreshListener {
    private ListView partList;
    private EquipmentAdapter partAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_plant_part_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        partList = view.findViewById(R.id.my_part_list);
        partAdapter = new EquipmentAdapter(getContext(), EquipmentAdapter.equipments);
        partList.setAdapter(partAdapter);
        swipeRefreshLayout = view.findViewById(R.id.part_detail_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                EquipmentOverviewFragment.fetchEquipmentData(swipeRefreshLayout);
            }
        });
        partList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),EquipmentAdapter.equipments.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
    @Override public void onRefresh() { EquipmentOverviewFragment.fetchEquipmentData(swipeRefreshLayout); }
}
