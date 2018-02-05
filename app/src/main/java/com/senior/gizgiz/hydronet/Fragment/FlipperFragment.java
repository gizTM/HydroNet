package com.senior.gizgiz.hydronet.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.senior.gizgiz.hydronet.CustomHelperClass.CustomFlipperLayout;

/**
 * Created by Admins on 017 17/1/2018.
 */

public class FlipperFragment extends Fragment {
    private CustomFlipperLayout flipperLayout;
    private int inflatedLayoutId;
    private int flipperId;
    private int firstPageLayout,secondPageLayout;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.inflatedLayoutId,container,false);
//        flipperLayout = new TwoPageFlipperLayout(getContext());
        flipperLayout = view.findViewById(this.flipperId);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
    }

    public void setViewFirstPage() { flipperLayout.setViewFirstPage(); }

//    public void setFirstPageLayout(String firstPageLayout) { flipperLayout.setFirstPageLayout(firstPageLayout); }
//    public void setSecondPageLayout(String secondPageLayout) { flipperLayout.setSecondPageLayout(secondPageLayout); }

    public void setInflatedLayoutId(int inflatedLayoutId) { this.inflatedLayoutId = inflatedLayoutId; }
    public void setFlipperId(int flipperId) { this.flipperId = flipperId; }
}
