package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.HomeDetailFragment;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 010 10/02/2018.
 */

public class HomeOverviewFragment extends OverviewFragment implements OnBackPressListener {
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_home_overview,viewGroup,false);
        rootView.findViewById(R.id.btn_show_detail_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });
        return rootView;
    }

    public void onViewCreated(final View parentView, Bundle savedInstanceState) {
        super.onViewCreated(parentView,savedInstanceState);
        parentView.findViewById(R.id.farm_bed_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customView = LayoutInflater.from(getContext()).inflate(R.layout.popup_sensor_table,null);
                final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                popup.setOutsideTouchable(true);
                popup.showAtLocation(parentView, Gravity.CENTER,0,0);
                customView.findViewById(R.id.dim_popup_overlay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { popup.dismiss(); }
                });
            }
        });
    }

    private void enterNextFragment() {
        HomeDetailFragment detailFragment = new HomeDetailFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_home, detailFragment);
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
