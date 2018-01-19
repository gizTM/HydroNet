package com.senior.gizgiz.hydronet.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.senior.gizgiz.hydronet.CustomHelperClass.TwoPageFlipperLayout;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 017 17/1/2018.
 */

public class TwoPageFragment extends Fragment {
    private TwoPageFlipperLayout flipperLayout;
    private int inflatedLayoutId;
    private int flipperId;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.inflatedLayoutId,container,false);
        flipperLayout = view.findViewById(this.flipperId);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
    }

    public void setViewFirstPage() { flipperLayout.setViewFirstPage(); }

    public void setInflatedLayoutId(int inflatedLayoutId) { this.inflatedLayoutId = inflatedLayoutId; }

    public void setFlipperId(int flipperId) { this.flipperId = flipperId; }
}
