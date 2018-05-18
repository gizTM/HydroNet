package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.senior.gizgiz.hydronet.Fragment.DetailFragment.TradeSummaryDetailFragment;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 026 26/02/2018.
 */

public class TradeSummaryOverviewFragment extends OverviewFragment implements OnBackPressListener {
    private CustomTextView totalMaterialCost;

    private float materialCost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.content_trade_summary_overview, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.btn_show_detail_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });
    }

    private void enterNextFragment() {
        TradeSummaryDetailFragment detailFragment = new TradeSummaryDetailFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_trade, detailFragment);
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
