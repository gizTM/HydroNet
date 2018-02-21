package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.EquipmentAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.PartOverviewAdapter;
import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.PartDetailFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class PartOverviewFragment extends OverviewFragment implements OnBackPressListener {
    private ListView partOverviewList;
    private PartOverviewAdapter partOverviewAdapter;
    private CustomTextView totalPartCost;

    private float partCost;

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
        partOverviewAdapter = new PartOverviewAdapter(getContext(),EquipmentAdapter.exampleCards);
        partOverviewList.setAdapter(partOverviewAdapter);
        partOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),EquipmentAdapter.exampleCards.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
        for (Item card : EquipmentAdapter.exampleCards) partCost += card.getCost();
        totalPartCost = view.findViewById(R.id.overall_part_cost);
        DecimalFormat decimalFormat = new DecimalFormat("฿###,###.###");
        totalPartCost.setText(decimalFormat.format(partCost));
        view.findViewById(R.id.btn_show_detail_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });
    }

    private void enterNextFragment() {
        PartDetailFragment detailFragment = new PartDetailFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_part, detailFragment);
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
