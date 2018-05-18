package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

/**
 * Created by Admins on 009 09/02/2018.
 */

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.MaterialAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.MaterialOverviewAdapter;
import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.MaterialDetailFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;

public class MaterialOverviewFragment extends OverviewFragment implements OnBackPressListener,SwipeRefreshLayout.OnRefreshListener {
    private ListView materialOverviewList;
    private static MaterialOverviewAdapter materialOverviewAdapter;
    private static CustomTextView totalMaterialCost;

    private static float materialCost;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.content_plant_material_overview, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        materialOverviewList = view.findViewById(R.id.material_detail_list);
        materialOverviewAdapter = new MaterialOverviewAdapter(getContext(),MaterialAdapter.materials);
        materialOverviewList.setAdapter(materialOverviewAdapter);
        swipeRefreshLayout = view.findViewById(R.id.material_overview_swipe_layout);
        Log.e("material overview","onViewCreated");
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchMaterialData(swipeRefreshLayout);
            }
        });
        materialOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),MaterialAdapter.materials.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
        totalMaterialCost = view.findViewById(R.id.overall_material_cost);
        view.findViewById(R.id.btn_show_detail_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });
    }

    private void enterNextFragment() {
        MaterialDetailFragment detailFragment = new MaterialDetailFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_material, detailFragment);
        transaction.commit();
    }
    public static void fetchMaterialData(final SwipeRefreshLayout swipeRefreshLayout) {
        Log.e("fetch material",">>>");
        swipeRefreshLayout.setRefreshing(true);
        RealTimeDBManager.getDatabase().child("items").orderByChild("id").startAt("m").endAt("m\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MaterialAdapter.materials.clear();
                materialCost = 0;
//                Log.e("onDataChanged","material");
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Item item = childDataSnapshot.getValue(Item.class);
                    MaterialAdapter.materials.add(item);
                    materialCost+=item.getCost();
                }
                DecimalFormat decimalFormat = new DecimalFormat("฿###,###.###");
                totalMaterialCost.setText(decimalFormat.format(materialCost));
                materialOverviewAdapter.notifyDataSetChanged();
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
    @Override public void onRefresh() { fetchMaterialData(swipeRefreshLayout); }

}

