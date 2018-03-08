package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.EquipmentAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.EquipmentOverviewAdapter;
import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.EquipmentDetailFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class EquipmentOverviewFragment extends OverviewFragment implements OnBackPressListener,SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView partOverviewList;
    private static EquipmentOverviewAdapter partOverviewAdapter;
    private static CustomTextView totalPartCost;

    private static float partCost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.content_plant_part_overview, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        partOverviewList = view.findViewById(R.id.part_detail_list);
        partOverviewAdapter = new EquipmentOverviewAdapter(getContext(),EquipmentAdapter.equipments);
        partOverviewList.setAdapter(partOverviewAdapter);
        swipeRefreshLayout = view.findViewById(R.id.part_overview_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchEquipmentData(swipeRefreshLayout);
            }
        });
        partOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = EquipmentAdapter.equipments.get(i);
                Toast.makeText(getContext(),item.getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
        totalPartCost = view.findViewById(R.id.overall_part_cost);
        view.findViewById(R.id.btn_show_detail_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });
    }

    private void enterNextFragment() {
        EquipmentDetailFragment detailFragment = new EquipmentDetailFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_part, detailFragment);
        transaction.commit();
    }

    public static void fetchEquipmentData(final SwipeRefreshLayout swipeRefreshLayout) {
        partCost=0;
        swipeRefreshLayout.setRefreshing(true);
        RealTimeDBManager.getDatabase().child("items").orderByChild("id").startAt("e").endAt("e\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EquipmentAdapter.equipments.clear();
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                    Log.e("onDataChange",childDataSnapshot.getKey());
                    Item item = childDataSnapshot.getValue(Item.class);
                    EquipmentAdapter.equipments.add(item);
                    partCost+=item.getCost();
                }
                DecimalFormat decimalFormat = new DecimalFormat("฿###,###.###");
                totalPartCost.setText(decimalFormat.format(partCost));
                partOverviewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
    @Override public boolean setViewOverview() { return new BackPressHandler(this).onBackPressed(); }
    @Override public void onRefresh() { fetchEquipmentData(swipeRefreshLayout); }

}
