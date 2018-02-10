package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.senior.gizgiz.hydronet.Fragment.BackPressImpl;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.PartDetailFragment;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class PartOverviewFragment extends Fragment implements OnBackPressListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.content_plant_part_overview, container, false);
        rootView.findViewById(R.id.btn_show_detail_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });

        return rootView;
    }

    private void enterNextFragment() {
        PartDetailFragment detailFragment = new PartDetailFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_part, detailFragment);
        transaction.commit();
    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }
}
