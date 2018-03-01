package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

/**
 * Created by Admins on 009 09/02/2018.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.MaterialAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.MaterialOverviewAdapter;
import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.MaterialDetailFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;

public class MaterialOverviewFragment extends OverviewFragment implements OnBackPressListener {
    private ListView materialOverviewList;
    private MaterialOverviewAdapter materialOverviewAdapter;
    private CustomTextView totalMaterialCost;

    private float materialCost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.content_plant_material_overview, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        materialOverviewList = view.findViewById(R.id.material_detail_list);
        materialOverviewAdapter = new MaterialOverviewAdapter(getContext(),MaterialAdapter.exampleCards);
        materialOverviewList.setAdapter(materialOverviewAdapter);
        materialOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),MaterialAdapter.exampleCards.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
        materialCost = 0;
        for (Item card : MaterialAdapter.exampleCards) materialCost += card.getCost();
        totalMaterialCost = view.findViewById(R.id.overall_material_cost);
        DecimalFormat decimalFormat = new DecimalFormat("฿###,###.###");
        totalMaterialCost.setText(decimalFormat.format(materialCost));
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

    @Override
    public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }

    @Override
    public boolean setViewOverview() {
        return new BackPressHandler(this).onBackPressed();
    }
}

